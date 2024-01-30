package org.project.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data @Builder
@NoArgsConstructor @AllArgsConstructor
@Table (indexes = @Index(name = "idx_bd_list", columnList = "createdAt DESC"))
public class TodoData extends Base{

    @Id
    @GeneratedValue
    private Long seq;

    @Column(length=50, nullable = false)
    private String gid = UUID.randomUUID().toString();

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="tId")
    private Todo todo;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="USER_NO")
    private Member member;

    @Column(nullable = false)
    private String subject;

    @Lob
    @Column(nullable = false)
    private String content;

}
