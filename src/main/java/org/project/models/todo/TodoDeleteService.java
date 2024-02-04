package org.project.models.todo;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.project.entities.TodoData;
import org.project.repositories.TodoDataRepository;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class TodoDeleteService {
    private final TodoInfoService infoService;
    private final TodoDataRepository repository;
    public void delete(Long seq) {

        TodoData data = infoService.get(seq);

        // 게시글 삭제
        repository.delete(data);

        repository.flush();
    }
}
