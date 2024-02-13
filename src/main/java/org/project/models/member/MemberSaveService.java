package org.project.models.member;

import lombok.RequiredArgsConstructor;
import org.project.api.controllers.members.JoinValidator;
import org.project.api.controllers.members.RequestJoin;
import org.project.commons.contansts.MemberType;
import org.project.configs.jwt.CustomJwtFilter;
import org.project.entities.Member;
import org.project.repositories.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberSaveService {
    private final MemberRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JoinValidator joinValidator;
    private final CustomJwtFilter customJwtFilter;

    public void save(RequestJoin form, Errors errors) {
        joinValidator.validate(form, errors);

        if (errors.hasErrors()) {
            return;
        }

        // 회원 가입 처리
        String hash = passwordEncoder.encode(form.password());
        Member member = Member.builder()
                .email(form.email())
                .nickname(form.name())
                .password(hash)
                .mobile(form.mobile())
                .type(MemberType.USER)
                .social(false)
                .build();

        save(member);
    }


    public void save(Member member) {
        String mobile = member.getMobile();
        if (member != null) {
            mobile = mobile.replaceAll("\\D", "");
            member.setMobile(mobile);
        }

        repository.saveAndFlush(member);
    }

    public Member getKakaoMember(String accessToken) {

        // 카카오 연동 닉네임 -- 이메일 주소에 해당
        String nickname = customJwtFilter.getEmailFromKakaoAccessToken(accessToken);

        // 기존에 DB에 회원 정보가 있는 경우 & 없는 경우
        Optional<Member> result = repository.findByEmail(nickname);

        if (result.isPresent()) {

            Member member = result.get();
            //return
            return member;
        }

        Member socialMember = makeKakaoMember(nickname);

        repository.save(socialMember);

        return socialMember;
    }

    public Member makeKakaoMember(String email) {
        String hash = passwordEncoder.encode("_aA123456"); // 예시로 임시 비밀번호 생성
        Member kakaoMember = Member.builder()
                .email(email)
                .nickname("social") // 카카오 연동 닉네임으로 설정
                .password(hash)
                .type(MemberType.USER)
                .mobile("01033334444") // 예시로 임시 휴대폰 번호 생성
                .social(true) // 받아온 파라미터 값으로 설정
                .build();

        return kakaoMember;
    }
}
