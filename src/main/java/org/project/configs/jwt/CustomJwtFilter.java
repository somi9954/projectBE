package org.project.configs.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.project.entities.Member;
import org.project.models.member.MemberInfo;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Log4j2
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

    public String getEmailFromKakaoAccessToken(String accessToken) {
        String kakaoGetUserURL = "https://kapi.kakao.com/v2/user/me";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        // 카카오 API 응답을 ResponseEntity<Map>으로 받아옵니다.
        ResponseEntity<Map> response = restTemplate.exchange(kakaoGetUserURL, HttpMethod.GET, entity, Map.class);

        // 응답 데이터를 확인합니다.
        log.info("카카오 API 응답: " + response);

        // 응답 데이터에서 사용자 정보를 추출합니다.
        Map<String, Object> responseBody = response.getBody();
        if (responseBody != null) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) responseBody.get("properties");
            if (kakaoAccount != null) {
                String nickname = (String) kakaoAccount.get("nickname");
                log.info("카카오 사용자 닉네임: " + nickname);
                return nickname;
            } else {
                log.error("카카오 응답에서 사용자 정보를 찾을 수 없습니다.");
            }
        } else {
            log.error("카카오 API 응답에서 응답 바디를 가져올 수 없습니다.");
        }

        // 사용자 닉네임을 찾을 수 없는 경우 null을 반환합니다.
        return null;
    }
}
