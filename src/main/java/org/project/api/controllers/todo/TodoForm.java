package org.project.api.controllers.todo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


import java.util.UUID;

@Data
public class TodoForm {

    private String mode = "write";

    private Long seq;

    private String gid = UUID.randomUUID().toString();

    @NotBlank
    private String subject;

    @NotBlank
    private String content;
}
