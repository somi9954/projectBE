package org.project.api.controllers.members;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.project.commons.Utils;
import org.project.commons.exceptions.BadRequestExeption;
import org.project.commons.rests.JSONData;
import org.project.models.member.MemberLoginService;
import org.project.models.member.MemberSaveService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.session.InMemoryWebSessionStore;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberSaveService saveService;

    private final MemberLoginService loginService;

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
         * 2. 응답 헤더 - Authorization : Bearer 토큰
         */

        JSONData data = new JSONData(accessToken);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        return ResponseEntity.status(data.getStatus()).headers(headers).body(data);
    }

    public void errorProcess(Errors errors) {
        if (errors.hasErrors()) {
            throw new BadRequestExeption(Utils.getMessages(errors));
        }


    }
}
