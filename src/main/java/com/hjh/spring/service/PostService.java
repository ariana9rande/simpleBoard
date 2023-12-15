package com.hjh.spring.service;

import com.hjh.spring.model.entity.Post;
import com.hjh.spring.repository.PostRepository;
import com.hjh.spring.repository.UserLikeRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService
{
    PostRepository postRepository;
    UserLikeRepository userLikeRepository;

    public PostService(PostRepository postRepository, UserLikeRepository userLikeRepository)
    {
        this.postRepository = postRepository;
        this.userLikeRepository = userLikeRepository;
    }

    public List<Post> getPostList()
    {
        return postRepository.findAll(Sort.by(Sort.Direction.DESC, "postId"));
    }

    public Post getArticleById(Long id)
    {
        return postRepository.findArticleByPostId(id);
    }

    public void addArticle(Post article)
    {
        Post newArticle = new Post();
        newArticle.setPostTitle(article.getPostTitle());
        newArticle.setPostContent(article.getPostContent());
        newArticle.setPostWriter(article.getPostWriter());
        newArticle.setPostWriteDate(article.getPostWriteDate());

        postRepository.save(newArticle);
    }

    public void viewArticle(Long id)
    {
        Post article = postRepository.findArticleByPostId(id);

        article.setViewCount(article.getViewCount() + 1);
        postRepository.save(article);
    }

    public void editArticle(Post article)
    {
        postRepository.save(article);
    }

    @Transactional
    public void removeArticle(Post article)
    {
        userLikeRepository.deleteByPostPostId(article.getPostId());
        postRepository.delete(article);
    }

    public void likeArticle(Long id)
    {
        Post article = postRepository.findArticleByPostId(id);

        article.setLikeCount(article.getLikeCount() + 1);
        postRepository.save(article);
    }
}
