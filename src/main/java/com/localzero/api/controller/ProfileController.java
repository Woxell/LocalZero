package com.localzero.api.controller;

import com.localzero.api.entity.Person;
import com.localzero.api.entity.Post;
import com.localzero.api.service.PersonService;
import com.localzero.api.service.PostService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    private final PersonService personService;
    private final PostService postService;

    public ProfileController(PersonService personService, PostService postService) {
        this.personService = personService;
        this.postService = postService;
    }

    @GetMapping
    public String myProfile(Model model, @AuthenticationPrincipal UserDetails currentUser) {
        if (currentUser == null) {
            return "redirect:/login";
        }

        String email = currentUser.getUsername();
        Person user = personService.findByEmail(email);
        List<Post> posts = postService.getPostsByAuthorEmail(email);

        model.addAttribute("user", user);
        model.addAttribute("posts", posts);
        return "profile";
    }

    @GetMapping("/{email}")
    public String OtherProfile(@PathVariable String email, Model model) {
        Person user = personService.findByEmail(email);
        if (user == null) {
            model.addAttribute("message", "Användaren med e-post '" + email + "' hittades inte.");
            return "user-not-found";
        }
        List<Post> posts = postService.getPostsByAuthorEmail(email);
        model.addAttribute("user", user);
        model.addAttribute("posts", posts);
        return "profiles";
    }

}

