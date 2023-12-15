package com.hjh.spring.controller;

import com.hjh.spring.model.entity.Comment;
import com.hjh.spring.model.entity.Post;
import com.hjh.spring.model.entity.User;
import com.hjh.spring.model.entity.UserLike;
import com.hjh.spring.service.CommentService;
import com.hjh.spring.service.PostService;
import com.hjh.spring.service.UserLikeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/board")
public class PostController
{
    PostService postService;
    CommentService commentService;
    UserLikeService userLikeService;

    public PostController(PostService postService, CommentService commentService, UserLikeService userLikeService)
    {
        this.postService = postService;
        this.commentService = commentService;
        this.userLikeService = userLikeService;
    }

    @GetMapping("/list")
    public String postListPage(Model model)
    {
        List<Post> postList = postService.getPostList();
        model.addAttribute("postList", postList);

        return "board";
    }

    @GetMapping("/add")
    public String writeForm(HttpSession session,
                            RedirectAttributes redirectAttributes)
    {
        User loggedInUser = (User)session.getAttribute("loggedInUser");
        if(loggedInUser != null)
            return "write";
        else
        {
            redirectAttributes.addFlashAttribute("message", "로그인 필요");
            return "redirect:/board/list";
        }
    }

    @PostMapping("/add")
    public String writeArticle(HttpSession session,
                        @RequestParam("title") String title,
                        @RequestParam("content") String content,
                               RedirectAttributes redirectAttributes)
    {
        Post article = new Post();
        article.setPostTitle(title);
        article.setPostContent(content);
        User loggedInUser = (User)session.getAttribute("loggedInUser");
        if(loggedInUser != null)
            article.setPostWriter(loggedInUser.getName());
        else
            article.setPostWriter("anonymous");

        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = dateTime.format(formatter);
        article.setPostWriteDate(formattedDateTime);

        postService.addArticle(article);
        redirectAttributes.addFlashAttribute("message", "게시글 작성 완료");

        return "redirect:/board/list";
    }

    @GetMapping("/view/{id}")
    public String viewArticlePage(@PathVariable Long id, Model model,
                                  @RequestParam("articleNo") String articleNo)
    {
        postService.viewArticle(id);
        model.addAttribute("article", postService.getArticleById(id));
        model.addAttribute("articleNo", articleNo);
        model.addAttribute("commentList", commentService.getCommentListByPost(postService.getArticleById(id)));

        return "view";
    }

    @PostMapping("/edit")
    public String editArticle(@RequestParam("id") Long id,
                              @RequestParam("title") String title,
                              @RequestParam("content") String content,
                              RedirectAttributes redirectAttributes,
                              HttpServletRequest request)
    {
        Post article = postService.getArticleById(id);
        article.setPostTitle(title);
        article.setPostContent(content);

        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = dateTime.format(formatter);
        article.setPostWriteDate(formattedDateTime);

        postService.editArticle(article);
        redirectAttributes.addFlashAttribute("message", "게시글 수정 완료");

        return "redirect:" + request.getHeader("Referer");
    }

    @PostMapping("/remove")
    public String removeArticle(@RequestParam("id") Long id,
                                RedirectAttributes redirectAttributes)
    {
        Post article = postService.getArticleById(id);
        postService.removeArticle(article);
        redirectAttributes.addFlashAttribute("message", "게시글 삭제 완료");

        return "redirect:/board/list";
    }

    @PostMapping("/add-comment")
    public String addComment(@RequestParam("commentContent") String content,
                             HttpSession session, HttpServletRequest request,
                             @RequestParam("id") Long postId,
                             RedirectAttributes redirectAttributes)
    {
        Comment comment = new Comment();
        comment.setContent(content);

        User loggedInUser = (User)session.getAttribute("loggedInUser");
        comment.setWriter(loggedInUser.getName());

        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = dateTime.format(formatter);
        comment.setWriteDate(formattedDateTime);

        comment.setPost(postService.getArticleById(postId));
        redirectAttributes.addFlashAttribute("message", "댓글 작성 완료");

        commentService.addComment(comment);

        return "redirect:" + request.getHeader("Referer");
    }

    @PostMapping("/edit-comment")
    public String editComment(@RequestParam("id") Long commentId,
                              @RequestParam("content") String content,
                              HttpServletRequest request,
                              RedirectAttributes redirectAttributes)
    {
        Comment comment = commentService.getCommentById(commentId);

        comment.setContent(content);

        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = dateTime.format(formatter);
        comment.setWriteDate(formattedDateTime);

        commentService.editComment(comment);
        redirectAttributes.addFlashAttribute("message", "댓글 수정 완료");

        return "redirect:" + request.getHeader("Referer");
    }

    @PostMapping("/remove-comment")
    public String removeComment(@RequestParam("id") Long commentId,
                                HttpServletRequest request,
                                RedirectAttributes redirectAttributes)
    {
        Comment comment = commentService.getCommentById(commentId);

        commentService.removeComment(comment);
        redirectAttributes.addFlashAttribute("message", "댓글 삭제 완료");

        return "redirect:" + request.getHeader("Referer");
    }

    @PostMapping("/like-post")
    public String likePost(@RequestParam("postId") Long postId,
                           HttpServletRequest request,
                           HttpSession session, RedirectAttributes redirectAttributes)
    {
        UserLike like = new UserLike();
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if(loggedInUser.getName().equals(postService.getArticleById(postId).getPostWriter()))
        {
            redirectAttributes.addFlashAttribute("message", "자신의 게시글은 추천할 수 없습니다");
            return "redirect:" + request.getHeader("Referer");
        }
        
        like.setUser(loggedInUser);
        like.setPost(postService.getArticleById(postId));

        if(userLikeService.addLike(like))
        {
            postService.likeArticle(postId);
            redirectAttributes.addFlashAttribute("message", "게시글 추천 완료");
        }
        else
            redirectAttributes.addFlashAttribute("message", "이미 추천한 게시글입니다.");

        return "redirect:" + request.getHeader("Referer");
    }

    @PostMapping("/like-comment")
    public String likeComment(@RequestParam("commentId") Long commentId,
                           HttpServletRequest request,
                              HttpSession session, RedirectAttributes redirectAttributes)
    {
        UserLike like = new UserLike();
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        
        if(loggedInUser.getName().equals(commentService.getCommentById(commentId).getWriter()))
        {
            redirectAttributes.addFlashAttribute("message", "자신의 댓글은 추천할 수 없습니다");
            return "redirect:" + request.getHeader("Referer");
        }
        
        like.setUser(loggedInUser);
        like.setComment(commentService.getCommentById(commentId));

        if(userLikeService.addLike(like))
        {
            commentService.likeComment(commentId);
            redirectAttributes.addFlashAttribute("message", "댓글 추천 완료");
        }
        else
            redirectAttributes.addFlashAttribute("message", "이미 추천한 댓글입니다.");

        return "redirect:" + request.getHeader("Referer");
    }
}
