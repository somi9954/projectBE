package org.project.api.controllers.todo;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.project.commons.ListData;
import org.project.commons.Utils;
import org.project.commons.exceptions.BadRequestException;
import org.project.commons.rests.JSONData;
import org.project.entities.TodoData;
import org.project.models.todo.TodoDeleteService;
import org.project.models.todo.TodoInfoService;
import org.project.models.todo.TodoSaveService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/v1/todo")
@RequiredArgsConstructor
public class TodoController {
    private final TodoSaveService saveService;
    private final TodoInfoService infoService;
    private final TodoDeleteService deleteService;

    @PostMapping("/write/{tId}")
    public ResponseEntity<JSONData> write(@PathVariable("tId") String bId, @RequestBody @Valid TodoForm form, Errors errors) {

        if (errors.hasErrors()) {
            String message = errors.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage).collect(Collectors.joining(","));
            throw new BadRequestException(Utils.getMessage("Not_Blank_write", "validation"));
        }

        saveService.save(form);

        JSONData data = new JSONData();
        data.setStatus(HttpStatus.CREATED);

        return ResponseEntity.status(data.getStatus()).body(data);
    }

    @GetMapping("/view/{seq}")
    public JSONData<TodoData> view(@PathVariable("seq") Long seq) {

        TodoData data = infoService.get(seq);

        return new JSONData<>(data);
    }

    @GetMapping("/delete/{seq}")
    public ResponseEntity delete(@PathVariable("seq") Long seq, @RequestBody @Valid TodoForm form, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage).collect(Collectors.joining(","));
            throw new BadRequestException(Utils.getMessage("Not_Blank_delete", "validation"));
        }
            deleteService.delete(seq);

            return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @GetMapping("/list")
    public JSONData<List<TodoData>> list(TodoDataSearch search) {
        ListData<TodoData> todoList = infoService.getList(search);
        List<TodoData> data = todoList.getContent();

        JSONData<List<TodoData>> jsonData = new JSONData<>();
        jsonData.setData(data);

        return jsonData;
    }

}