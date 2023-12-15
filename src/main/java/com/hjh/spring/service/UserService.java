package com.hjh.spring.service;

import com.hjh.spring.model.entity.User;
import com.hjh.spring.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService
{
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    public List<User> findAll()
    {
        return userRepository.findAll();
    }

    public void register(User user)
    {
        User newUser = new User();
        newUser.setName(user.getName());
        newUser.setPassword(user.getPassword());
        newUser.setEmail(user.getEmail());
        newUser.setRole(user.getRole());

        userRepository.save(newUser);
    }

    public User findUserByName(String name)
    {
        return userRepository.findUserByName(name);
    }

    public void update(User user)
    {
        userRepository.save(user);
    }
}
