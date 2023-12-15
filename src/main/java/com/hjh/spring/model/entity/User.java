package com.hjh.spring.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class User
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable=false)
    private String name;

    @Column(length = 50, nullable=false)
    private String email;

    @Column(length = 20, nullable=false)
    private String password;

    @Column(nullable=false)
    private String role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserLike> userLikes;
}
