package com.localzero.api.repository;

import com.localzero.api.entity.Person;
import com.localzero.api.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByAuthorEmailOrderByCreationDatetimeDesc(String authorEmail);

    List<Post> findByInitiativeIsNullOrderByCreationDatetimeDesc();

    List<Post> findByInitiativeIdOrderByCreationDatetimeDesc(Long initiativeId);


    Optional<Post> findByContentAndAuthor(String s, Person p1);
}
