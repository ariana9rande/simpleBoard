package com.hjh.spring.service;

import com.hjh.spring.model.entity.Comment;
import com.hjh.spring.model.entity.Post;
import com.hjh.spring.repository.CommentRepository;
import com.hjh.spring.repository.UserLikeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService
{
    CommentRepository commentRepository;
    UserLikeRepository userLikeRepository;

    public CommentService(CommentRepository commentRepository, UserLikeRepository userLikeRepository)
    {
        this.commentRepository = commentRepository;
        this.userLikeRepository = userLikeRepository;
    }

    public Comment getCommentById(Long id)
    {
        return commentRepository.findCommentById(id);
    }

    public void addComment(Comment comment)
    {
        Comment newComment = new Comment();
        newComment.setWriter(comment.getWriter());
        newComment.setContent(comment.getContent());
        newComment.setWriteDate(comment.getWriteDate());
        newComment.setPost(comment.getPost());

        commentRepository.save(newComment);
    }

    public List<Comment> getCommentList()
    {
        return commentRepository.findAll();
    }

    public List<Comment> getCommentListByPost(Post post)
    {
        return commentRepository.findAllByPost(post);
    }

    public void editComment(Comment comment)
    {
        commentRepository.save(comment);
    }

    @Transactional
    public void removeComment(Comment comment)
    {
        userLikeRepository.deleteByCommentId(comment.getId());
        commentRepository.delete(comment);
    }

    public void likeComment(Long id)
    {
        Comment comment = commentRepository.findCommentById(id);

        comment.setLikeCount(comment.getLikeCount() + 1);
        commentRepository.save(comment);
    }
}
