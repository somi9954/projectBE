package org.project.api.controllers.admin;

import lombok.Data;

import java.util.List;

@Data
public class TodoSearch {
    private int page = 1;
    private int limit = 20;

    private String sopt;
    private String skey;
    private List<Boolean> active;
    private List<String> authority;
}