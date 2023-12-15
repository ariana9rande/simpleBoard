package com.hjh.spring.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Post
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(length = 50, nullable = false)
    private String postTitle;

    @Column(length = 2000, nullable = false)
    private String postContent;

    @Column(length = 20, nullable = false)
    private String postWriter;

    @Column(nullable = false)
    private String postWriteDate;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int viewCount;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int likeCount;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();
}
