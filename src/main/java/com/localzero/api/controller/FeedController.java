package com.localzero.api.controller;

import com.localzero.api.entity.Initiative;
import com.localzero.api.entity.Person;
import com.localzero.api.entity.Post;
import com.localzero.api.service.InitiativeService;
import com.localzero.api.service.PersonService;
import com.localzero.api.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@AllArgsConstructor
public class FeedController {

    private final PersonService personService;
    private final PostService postService;
    private final InitiativeService initiativeService;

    @GetMapping("/feed")
    @Transactional(readOnly = true)
    public String showFeed(Authentication authentication, Model model) {
        String email = authentication.getName();
        Person person = personService.findByEmail(email);

        List<Initiative> myInitiatives = initiativeService.getByParticipant(email);

        List<Post> postsFromMyInitiatives = new ArrayList<>();
        for (Initiative initiative : myInitiatives) {
            postsFromMyInitiatives.addAll(postService.getPostsByInitiativeId(initiative.getId()));
        }

        List<Post> allStandalonePosts = postService.getAllStandalonePosts();

        Set<Long> myInitiativeIds = new HashSet<>();
        for (Initiative i : myInitiatives) {
            myInitiativeIds.add(i.getId());
        }

        List<Initiative> visibleInitiatives = initiativeService.getPublicOrByCommunities(person.getCommunities());

        List<Post> postsFromVisibleInitiatives = new ArrayList<>();
        for (Initiative initiative : visibleInitiatives) {
            if (!myInitiativeIds.contains(initiative.getId())) {
                postsFromVisibleInitiatives.addAll(postService.getPostsByInitiativeId(initiative.getId()));
            }
        }

        List<Post> combinedPosts = new ArrayList<>();
        combinedPosts.addAll(postsFromMyInitiatives);
        combinedPosts.addAll(postsFromVisibleInitiatives);
        combinedPosts.addAll(allStandalonePosts);
        combinedPosts.sort((a, b) -> b.getCreationDatetime().compareTo(a.getCreationDatetime()));

        System.out.println("==== FEED DEBUG ====");
        System.out.println("Användare: " + email);
        System.out.println("Deltar i initiativ: " + myInitiatives.size());
        for (Initiative i : myInitiatives) {
            System.out.println(" → " + i.getTitle());
        }
        System.out.println("Totalt antal inlägg i feed: " + combinedPosts.size());
        for (Post p : combinedPosts) {
            System.out.println(" - " + p.getContent() + " | av: " + p.getAuthor().getEmail() +
                    " | initiativ: " + (p.getInitiative() != null ? p.getInitiative().getTitle() : "Enskild"));
        }

        model.addAttribute("name", person.getName());
        model.addAttribute("posts", combinedPosts);
        model.addAttribute("source", "feed");

        return "feed";
    }
}