package org.project.models.todo;

import org.project.commons.Utils;
import org.project.commons.exceptions.BadRequestException;

public class TodoDataNotFoundException extends BadRequestException {
    public TodoDataNotFoundException() {
        super(Utils.getMessage("NotFound.boardData", "error"));
    }
}
