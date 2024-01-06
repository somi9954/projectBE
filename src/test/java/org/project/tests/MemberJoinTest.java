package org.project.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.project.api.controllers.members.RequestJoin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(properties = "spring.profiles.active=test")
public class MemberJoinTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("회원 가입 테스트")
    void JoinTest() throws Exception {
        RequestJoin form = RequestJoin.builder()
                //.email("user01@test.org")
                //.password("_aA123456")
                //.confirmPassword("_aA123456")
                //.name("사용자01")
                .mobile("010-0000-0000")
                //.agree(true)
                .build();

        ObjectMapper om = new ObjectMapper();
        String params = om.writeValueAsString(form);

        mockMvc.perform(
                post("/api/v1/member").with(csrf())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8")
                        .content(params)
        ).andDo(print());
    }
}
