package com.localzero.api.controller;


import com.localzero.api.service.PostService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.localzero.api.template.PostCreator;

@Controller
public class PostController {
    private final PostService postService;
    private final PostCreator pCreator;

    public PostController(PostService postService, PostCreator pCreator) {
        this.postService = postService;
        this.pCreator = pCreator;

    }

    @GetMapping("/create-post")
    public String ShowCreatedPostFrom() {
        return "create-post";
    }

    @PostMapping("/create-post")
    public String createPost(@RequestParam String content, @AuthenticationPrincipal UserDetails currentUser) {
        if (currentUser == null) {
            return "redirect:/login";
        }
        pCreator.create(currentUser.getUsername(), content);

        return "redirect:/feed";
    }


    @PostMapping("/posts/{id}/like")
    public String likePost(@PathVariable long id,
                           @RequestParam String source,
                           @AuthenticationPrincipal UserDetails currentUser) {
        if (currentUser == null) {
            return "redirect:/login";
        }

        postService.incrementLikes(id);

        if ("feed".equals(source)) {
            return "redirect:/feed";
        } else {
            String email = currentUser.getUsername();
            return "redirect:/profile/" + email;
        }
    }

}
