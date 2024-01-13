package org.project.api.controllers.admin;

import lombok.Data;

@Data
public class AdminConfig {
    private String siteTitle = "";
    private String siteDescription = "";
    private int cssJsVersion = 1;
    private String joinTerms = "";
    private String thumbSize = "";
}
