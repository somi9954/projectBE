package org.project.api.controllers.todo;

import lombok.Data;

@Data
public class TodoDataSearch {
    private String tId;
    private int page = 1;
    private int limit = 20;

    private String sopt;
    private String skey;
}
