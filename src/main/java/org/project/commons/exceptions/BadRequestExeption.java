package org.project.commons.exceptions;

import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

public class BadRequestExeption extends CommonException{

    public BadRequestExeption(Map<String, List<String>> messages) {
        super(messages, HttpStatus.BAD_REQUEST);
    }

    public BadRequestExeption(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
