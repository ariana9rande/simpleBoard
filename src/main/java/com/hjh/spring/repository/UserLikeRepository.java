package com.hjh.spring.repository;

import com.hjh.spring.model.entity.Comment;
import com.hjh.spring.model.entity.Post;
import com.hjh.spring.model.entity.User;
import com.hjh.spring.model.entity.UserLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLikeRepository extends JpaRepository<UserLike, Long>
{
    UserLike findUserLikeByUserAndPost(User user, Post post);
    UserLike findUserLikeByUserAndComment(User user, Comment comment);
    void deleteByCommentId(Long id);
    void deleteByPostPostId(Long id);
}
