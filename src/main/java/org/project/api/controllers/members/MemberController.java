package org.project.api.controllers.members;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import lombok.extern.log4j.Log4j2;
import org.project.commons.Utils;
import org.project.commons.exceptions.BadRequestException;
import org.project.commons.rests.JSONData;
import org.project.entities.Member;
import org.project.models.member.MemberInfo;
import org.project.models.member.MemberLoginService;
import org.project.models.member.MemberSaveService;
import org.project.repositories.MemberRepository;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
@Log4j2
public class MemberController {

    private final MemberSaveService saveService;

    private final MemberLoginService loginService;

    private final MemberRepository repository;

    @PostMapping
    public ResponseEntity<JSONData> join(@RequestBody @Valid RequestJoin form, Errors errors) {
        saveService.save(form, errors);

        errorProcess(errors);

        JSONData data = new JSONData();
        data.setStatus(HttpStatus.CREATED);

        return ResponseEntity.status(data.getStatus()).body(data);
    }


    @PostMapping("/token")
    public ResponseEntity<JSONData> token(@RequestBody @Valid RequestLogin form, Errors errors) {
        errorProcess(errors);

        String accessToken = loginService.login(form);

        /**
         * 1. 응답 body - JSONData 형식으로
         * 2. 응답 헤더 - Authorization: Bearer 토큰
         */

        JSONData data = new JSONData(accessToken);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        return ResponseEntity.status(data.getStatus()).headers(headers).body(data);
    }

    @GetMapping("/info")
    public JSONData info(@AuthenticationPrincipal MemberInfo memberInfo) {
        if (memberInfo != null) {
            // 사용자 정보가 있는 경우
            Member member = memberInfo.getMember();
            return new JSONData(member);
        } else {

            return new JSONData(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/memberlist")
    public JSONData memberList(@RequestParam String email) {

        Optional<Member> memberList = repository.findByEmail(email);
        return new JSONData(memberList);
    }

    @PostMapping("/kakao")
    public ResponseEntity<JSONData> getMemberFromKakako(String accessToken) {

        log.info("accessToken: " + accessToken);

        Member member = saveService.getKakaoMember(accessToken);

        // 회원 정보를 기반으로 응답 데이터 생성
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("member", member);


        // 이미 생성된 액세스 토큰을 사용하여 응답 구성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        JSONData data = new JSONData();
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(data);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String admin() {
        return "관리자 페이지 접속....";
    }

    private void errorProcess(Errors errors) {
        if (errors.hasErrors()) {
            throw new BadRequestException(Utils.getMessages(errors));
        }
    }
}
