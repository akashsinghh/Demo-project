package com.akashsyncr.Demo.project.controllers;

import com.akashsyncr.Demo.project.model.Image;
import com.akashsyncr.Demo.project.model.User;
import com.akashsyncr.Demo.project.reposistry.ImageRepository;
import com.akashsyncr.Demo.project.reposistry.UserRepository;
import com.akashsyncr.Demo.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageRepository imageRepository;

    @GetMapping("/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{username}/images")
    public ResponseEntity<List<Image>> getImagesForUser(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        List<Image> images = imageRepository.findByUserId(user.getId());
        return ResponseEntity.ok(images);
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        User registeredUser = userService.registerUser(user);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }
}