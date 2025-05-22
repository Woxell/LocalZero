package com.localzero.api.controller;


import com.localzero.api.service.InitiativeService;
import com.localzero.api.service.PostService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.localzero.api.template.PostCreator;

@Controller
public class PostController {
    private final PostService postService;
    private final PostCreator pCreator;
    private final InitiativeService inservice;

    public PostController(PostService postService, PostCreator pCreator, InitiativeService inservice) {
        this.postService = postService;
        this.pCreator = pCreator;
        this.inservice = inservice;

    }

    @GetMapping("/create-post")
    public String ShowCreatedPostFrom(Model model, @AuthenticationPrincipal UserDetails currentUser) {
        if(currentUser != null){
            model.addAttribute("initiatives",inservice.getByParticipant(currentUser.getUsername()));
        }else {
            model.addAttribute("initiatives",inservice.getAll());

        }
        return "create-post";
    }

    @PostMapping("/create-post")
    public String createPost(@RequestParam String content,@RequestParam(required = false) Long initiativeId ,@AuthenticationPrincipal UserDetails currentUser) {
        if (currentUser == null) {
            return "redirect:/login";
        }
        pCreator.create(currentUser.getUsername(), content, initiativeId);

        if(initiativeId != null){
            return "redirect:/initiatives/"+initiativeId;
        }

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
