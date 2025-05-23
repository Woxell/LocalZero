package com.localzero.api.controller;

import com.localzero.api.entity.EcoAction;
import com.localzero.api.entity.Person;
import com.localzero.api.entity.Post;
import com.localzero.api.repository.CommunityRepository;
import com.localzero.api.repository.PostRepository;
import com.localzero.api.service.EcoActionService;
import com.localzero.api.service.PersonService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ProfileController {

    private final PersonService personService;
    private final CommunityRepository communityRepository;
    private final EcoActionService ecoActionService;
    private final PostRepository postRepository;

    public ProfileController(PersonService personService,
                             CommunityRepository communityRepository,
                             EcoActionService ecoActionService,
                             PostRepository postRepository) {
        this.personService = personService;
        this.communityRepository = communityRepository;
        this.ecoActionService = ecoActionService;
        this.postRepository = postRepository;
    }

    @GetMapping("/profile")
    public String getProfile(Model model, @AuthenticationPrincipal UserDetails currentUser) {
        if (currentUser == null) {
            return "redirect:/login";
        }

        String email = currentUser.getUsername();
        Person user = personService.findByEmail(email);

        // Hämta användarens poster via repository
        List<Post> posts = postRepository.findByAuthorEmailOrderByCreationDatetimeDesc(email);

        // Eco actions och total CO₂
        List<EcoAction> actions = ecoActionService.getAllByUser(email);
        float totalCarbon = actions.stream()
                .map(EcoAction::getCarbonSavings)
                .reduce(0f, Float::sum);

        model.addAttribute("user", user);
        model.addAttribute("posts", posts);
        model.addAttribute("allCommunities", communityRepository.findAll());
        model.addAttribute("totalCarbonSavings", totalCarbon);

        return "profile";
    }
}
