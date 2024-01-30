package org.project.configs.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.project.entities.Member;
import org.project.models.member.MemberInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomJwtFilter extends GenericFilterBean {

    private final TokenProvider tokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)request;


        /** 요청 헤더 Authorization 항목의 JWT 토큰 추출 S*/
        String header = req.getHeader("Authorization");
        String jwt = null;
        if (StringUtils.hasText(header)) { // Bearer .... 토큰 추출
            jwt = header.substring(7);
        }
        /** 요청 헤더 Authorization 항목의 JWT 토큰 추출 E*/

        /** 로그인 유지 처리 S */
        if (StringUtils.hasText(jwt)){ // StringUtils.hastext() -> null인지 isblank인지 확인하는 메서드
            tokenProvider.validateToken(jwt); // 토큰 이상시 -> 예외 발생

            Authentication authentication = tokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        /** 로그인 유지 처리 E */


        chain.doFilter(request,response);
    }

    public boolean isUserAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            // 관리자 여부
            return authentication.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals("MemberType_ADMIN"));
        }

        return false;
    }
    public MemberInfo getMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            return (MemberInfo) authentication.getPrincipal();
        }

        return null;
    }

    public Member getEntity() {
        if (isUserLoggedIn()) {
            Member member = new ModelMapper().map(getMember(), Member.class);
            return member;
        }
        return null;
    }

    public boolean isUserLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }
}
