package org.project.api.controllers.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.project.commons.contansts.TodoAuthority;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoConfigForm {

    private String mode;

    private String bId;

    private String bName;

    private boolean active;

    private TodoAuthority authority = TodoAuthority.MEMBER;

    private String category;
}
