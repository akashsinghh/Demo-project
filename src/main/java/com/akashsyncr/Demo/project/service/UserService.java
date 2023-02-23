package com.akashsyncr.Demo.project.service;

import com.akashsyncr.Demo.project.model.User;
import com.akashsyncr.Demo.project.reposistry.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(User user) {
        // perform any necessary validation on the user object
        return userRepository.save(user);
    }
}

