package com.akashsyncr.Demo.project.reposistry;

import com.akashsyncr.Demo.project.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface  ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findByUserId(Long userId);
}
