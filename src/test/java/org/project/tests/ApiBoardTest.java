package org.project.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.project.api.controllers.todo.TodoForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = "spring.profiles.active=test")
public class ApiBoardTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    @DisplayName("글 작성 성공시 201 응답")
    void boardSaveTest() throws Exception {

        TodoForm form = new TodoForm();
        form.setSubject("제목");
        form.setContent("내용");

        ObjectMapper om = new ObjectMapper();
        String params = om.writeValueAsString(form);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/board/write/notice")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8")
                        .content(params)
                //.with(csrf().asHeader())
        ).andDo(print());
    }
}