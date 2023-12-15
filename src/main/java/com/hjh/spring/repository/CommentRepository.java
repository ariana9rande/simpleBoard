package com.hjh.spring.repository;

import com.hjh.spring.model.entity.Comment;
import com.hjh.spring.model.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>
{
    Comment findCommentById(Long id);
    List<Comment> findAllByPost(Post post);
}
