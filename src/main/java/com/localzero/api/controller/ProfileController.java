package com.localzero.api.controller;

import com.localzero.api.entity.Community;
import com.localzero.api.entity.EcoAction;
import com.localzero.api.entity.Person;
import com.localzero.api.entity.Post;
import com.localzero.api.service.CommunityService;
import com.localzero.api.service.PostService;
import com.localzero.api.service.EcoActionService;
import com.localzero.api.service.PersonService;
import org.springframework.security.core.Authentication;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@AllArgsConstructor
public class ProfileController {

    private final CommunityService communityService;
    private final EcoActionService ecoActionService;
    private final PersonService personService;
    private final PostService postService;

    @GetMapping("/profile")
    @Transactional(readOnly = true)
    public String getProfile(Model model, @AuthenticationPrincipal UserDetails currentUser) {
        if (currentUser == null) {
            return "redirect:/login";
        }

        String email = currentUser.getUsername();
        Person user = personService.findByEmail(email);


        List<Post> posts = postService.getPostsByAuthorEmail(email);

        List<EcoAction> actions = ecoActionService.getAllEcoactionsBy(email);
        float totalCarbon = actions.stream()
                .map(EcoAction::getCarbonSavings)
                .reduce(0f, Float::sum);

        model.addAttribute("user", user);
        model.addAttribute("posts", posts);
        model.addAttribute("allCommunities", communityService.findAll());
        model.addAttribute("totalCarbonSavings", totalCarbon);

        return "profile";
    }

    @PostMapping("/profile/communities")
    public String updateCommunities(@RequestParam(required = false) List<Long> communityIds,
                                    Authentication authentication) {
        String email = authentication.getName();
        Person user = personService.findByEmail(email);

        Set<Community> selected = (communityIds != null)
                ? new HashSet<>(communityService.findAllById(communityIds))
                : new HashSet<>();
        user.setCommunities(selected);
        personService.save(user);
        return "redirect:/profile";
    }

    @GetMapping("/{email}")
    public String OtherProfile(@PathVariable String email, Model model) {
        Optional<Person> optionalUser = personService.findOptionalByEmail(email);

        if (optionalUser.isEmpty()) {
            model.addAttribute("message", "Användaren med e-post '" + email + "' hittades inte.");
            return "user-not-found";
        }
        Person user = optionalUser.get();
        List<Post> posts = postService.getPostsByAuthorEmail(email);
        model.addAttribute("user", user);
        model.addAttribute("posts", posts);
        model.addAttribute("allCommunities", communityService.findAll());
        model.addAttribute("source", "profile");
        return "profile";
    }
}
