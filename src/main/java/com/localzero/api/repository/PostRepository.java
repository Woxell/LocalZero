package com.localzero.api.repository;

import com.localzero.api.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByAuthorEmailOrderByCreationDatetimeDesc(String authorEmail);
}
