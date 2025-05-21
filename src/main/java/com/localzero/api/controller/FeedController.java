package com.localzero.api.controller;

/**
 * @author: André
 */

import com.localzero.api.entity.Person;
import com.localzero.api.entity.Post;
import com.localzero.api.service.PersonService;
import com.localzero.api.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class FeedController {

    private PersonService personService;
    private PostService postService;

    @GetMapping("/feed")
    public String showFeed(Authentication authentication, Model model) {
        String email = authentication.getName(); // Hämta inloggad e-post
        Person person = personService.findByEmail(email); // Hämta användaren via e-post

        List<Post> posts = postService.getPostsByAuthorEmail(email);

        model.addAttribute("name", person.getName());
        model.addAttribute("posts",posts);
        model.addAttribute("source", "feed");
        return "feed";
    }

}