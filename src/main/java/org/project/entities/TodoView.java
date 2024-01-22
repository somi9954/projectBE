package org.project.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.Data;

@Data
@Entity
@IdClass(TodoViewId.class)
public class TodoView {
    @Id
    private Long seq; // 게시글 번호

    @Id
    private Integer tid; // viewTid
}
