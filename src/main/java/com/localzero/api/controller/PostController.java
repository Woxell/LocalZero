package com.localzero.api.controller;


import com.localzero.api.entity.Person;
import com.localzero.api.entity.Post;
import com.localzero.api.service.PersonService;
import com.localzero.api.service.PostService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
public class PostController {
    private final PostService postService;
    private final PersonService personService;

    public PostController(PostService postService, PersonService personService) {
        this.postService = postService;
        this.personService = personService;
    }

    @GetMapping("/create-post")
    public String ShowCreatedPostFrom() {
        return "create-post";
    }

    @PostMapping("/create-post")
    public  String createPost(@RequestParam String content, @AuthenticationPrincipal UserDetails currentUser) {
        if (currentUser == null) {
            return "redirect:/login";
        }
        Person author = personService.findByEmail(currentUser.getUsername());
        Post post = new Post();
        post.setContent(content);
        post.setAuthor(author);
        post.setCreationDatetime(LocalDateTime.now());

        postService.save(post);
        return "redirect:/feed";
    }
}
