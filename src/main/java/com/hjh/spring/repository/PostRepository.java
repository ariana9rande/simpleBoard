package com.hjh.spring.repository;

import com.hjh.spring.model.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>
{
    Post findArticleByPostId(Long id);
}
