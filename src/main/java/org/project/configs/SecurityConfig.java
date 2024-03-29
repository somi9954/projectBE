package org.project.configs;

import jakarta.servlet.http.HttpServletResponse;
import org.project.configs.jwt.CustomJwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;




@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private CustomJwtFilter customJwtFilter;

    @Autowired
    private CorsFilter corsFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(customJwtFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.exceptionHandling(c -> {
            c.authenticationEntryPoint((req,  res,  e) -> {
                res.sendError(HttpServletResponse.SC_UNAUTHORIZED); //  401
            });

            c.accessDeniedHandler((req,res, e) -> {
                res.sendError(HttpServletResponse.SC_FORBIDDEN); // 403
            });
        });
        http.authorizeHttpRequests(c -> {
            c.requestMatchers(
                    "/api/v1/member", // 회원가입
                    "/api/v1/member/token", // 로그인
                    "/api/v1/member/info",
                        "/api/v1/member/memberlist",
                    "/api/v1/member/exists/**").permitAll().anyRequest().authenticated(); // 나머지 URL은 모두 회원 인증(토큰 인증)
        });

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
