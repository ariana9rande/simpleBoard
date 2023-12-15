package com.hjh.spring.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Comment
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length=2000, nullable = false)
    private String content;

    private String writer;

    private String writeDate;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int likeCount;

    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;
}
