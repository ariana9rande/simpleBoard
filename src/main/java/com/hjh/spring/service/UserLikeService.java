package com.hjh.spring.service;

import com.hjh.spring.model.entity.UserLike;
import com.hjh.spring.repository.UserLikeRepository;
import org.springframework.stereotype.Service;

@Service
public class UserLikeService
{
    UserLikeRepository userLikeRepository;

    public UserLikeService(UserLikeRepository userLikeRepository)
    {
        this.userLikeRepository = userLikeRepository;
    }

    public boolean addLike(UserLike like)
    {
        boolean check = false;
        UserLike newLike = new UserLike();

        newLike.setUser(like.getUser());

        if(like.getPost() != null)
        {
            if(userLikeRepository.findUserLikeByUserAndPost(like.getUser(), like.getPost()) == null)
            {
                newLike.setPost(like.getPost());
                userLikeRepository.save(newLike);
                check = true;
            }
        }

        if(like.getComment() != null)
        {
            if(userLikeRepository.findUserLikeByUserAndComment(like.getUser(), like.getComment()) == null)
            {
                newLike.setComment(like.getComment());
                userLikeRepository.save(newLike);
                check = true;
            }
        }

        return check;
    }
}
