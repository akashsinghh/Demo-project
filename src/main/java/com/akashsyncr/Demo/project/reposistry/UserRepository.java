package com.akashsyncr.Demo.project.reposistry;

import com.akashsyncr.Demo.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
}
