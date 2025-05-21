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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class FeedController {

    private final PersonService personService;
    private final PostService postService;
    private final InitiativeService initiativeService;

    @GetMapping("/feed")
    public String showFeed(Authentication authentication, Model model) {
        String email = authentication.getName();
        Person person = personService.findByEmail(email);

        // 1. Hämta initiativ användaren deltar i
        List<Initiative> initiatives = initiativeService.getByParticipant(email);

        // 2. Hämta poster från initiativen
        List<Post> postsFromInitiatives = new ArrayList<>();
        for (Initiative initiative : initiatives) {
            postsFromInitiatives.addAll(postService.getPostsByInitiativeId(initiative.getId()));
        }

        // 3. Hämta alla enskilda poster (utan initiativ)
        List<Post> allStandalonePosts = postService.getAllStandalonePosts();

        // 4. Kombinera och sortera
        List<Post> combinedPosts = new ArrayList<>();
        combinedPosts.addAll(postsFromInitiatives);
        combinedPosts.addAll(allStandalonePosts);
        combinedPosts.sort((a, b) -> b.getCreationDatetime().compareTo(a.getCreationDatetime()));

        // 🧪 Debug
        System.out.println("==== FEED DEBUG ====");
        System.out.println("Användare: " + email);
        System.out.println("Deltar i initiativ: " + initiatives.size());
        for (Initiative i : initiatives) {
            System.out.println(" → " + i.getTitle());
        }
        System.out.println("Totalt antal inlägg i feed: " + combinedPosts.size());
        for (Post p : combinedPosts) {
            System.out.println(" - " + p.getContent() + " | av: " + p.getAuthor().getEmail() +
                    " | initiativ: " + (p.getInitiative() != null ? p.getInitiative().getTitle() : "Enskild"));
        }

        // 5. Skicka till vy
        model.addAttribute("name", person.getName());
        model.addAttribute("posts", combinedPosts);
        model.addAttribute("source", "feed");

        return "feed";
    }
}
