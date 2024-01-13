package org.project.api.controllers.admin;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
public class IndexController {
    @GetMapping
    public String index() {
        return "admin/main/index";
    }
}