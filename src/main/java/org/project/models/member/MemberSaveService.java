package org.project.models.member;

import lombok.RequiredArgsConstructor;
import org.project.api.controllers.members.JoinValidator;
import org.project.api.controllers.members.RequestJoin;
import org.project.commons.contansts.MemberType;
import org.project.entities.Member;
import org.project.repositories.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

@Service
@RequiredArgsConstructor
public class MemberSaveService {
    private final MemberRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JoinValidator joinValidator;

    public void save(RequestJoin form, Errors errors) {
        joinValidator.validate(form, errors);

        if (errors.hasErrors()) {
            return;
        }

        // 회원 가입 처리
        String hash = passwordEncoder.encode(form.password());
        Member member = Member.builder()
                .email(form.email())
                .name(form.name())
                .password(hash)
                .mobile(form.mobile())
                .type(MemberType.USER)
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
}
