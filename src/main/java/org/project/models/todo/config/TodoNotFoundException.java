package org.project.models.todo.config;


import org.project.commons.Utils;
import org.project.commons.exceptions.BadRequestException;


public class TodoNotFoundException extends BadRequestException {
    public TodoNotFoundException() {
        super(Utils.getMessage("NotFound.board", "error"));
    }
}
