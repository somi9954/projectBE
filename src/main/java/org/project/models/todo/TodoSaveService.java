package org.project.models.todo;

import lombok.RequiredArgsConstructor;
import org.project.api.controllers.todo.TodoForm;
import org.project.configs.jwt.CustomJwtFilter;
import org.project.entities.TodoData;
import org.project.models.todo.config.TodoConfigInfoService;
import org.project.repositories.TodoDataRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TodoSaveService {
    private final TodoDataRepository todoDataRepository;

    private final CustomJwtFilter jwtFilter;
    private final TodoConfigInfoService infoService;


    public void save(TodoForm form) {
        Long seq = form.getSeq();
        String mode = Objects.requireNonNullElse(form.getMode(), "write");

        // 게시판 설정 조회 + 글쓰기 권한 체크
        TodoData data = null;
        if (mode.equals("update") && seq != null) {
            data = todoDataRepository.findById(seq).orElseThrow(TodoDataNotFoundException::new);
        } else {
            data = new TodoData();
        }

        data.setContent(form.getContent());

        todoDataRepository.saveAndFlush(data);
    }
}
