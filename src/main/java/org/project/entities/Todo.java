package org.project.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.FileInfo;
import org.project.commons.contansts.TodoAuthority;

import java.util.List;

@Entity
@Data @Builder
@NoArgsConstructor @AllArgsConstructor
public class Todo extends Base{
    @Id
    @Column(length = 30)
    private String tId;

    @Column(length = 60, nullable = false)
    private String tname;

    private boolean active;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private TodoAuthority authority = TodoAuthority.MEMBER;

}
