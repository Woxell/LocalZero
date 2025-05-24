package com.localzero.api.controller;

import com.localzero.api.entity.Community;
import com.localzero.api.entity.EcoAction;
import com.localzero.api.entity.Person;
import com.localzero.api.entity.Post;
import com.localzero.api.repository.CommunityRepository;
import com.localzero.api.repository.PostRepository;
import com.localzero.api.service.EcoActionService;
import com.localzero.api.service.PersonService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    @Transactional(readOnly = true)
    public String getProfile(Model model, @AuthenticationPrincipal UserDetails currentUser) {
        if (currentUser == null) {
            return "redirect:/login";
        }

        String email = currentUser.getUsername();
        Person user = personService.findByEmail(email);

        List<Post> posts = postRepository.findByAuthorEmailOrderByCreationDatetimeDesc(email);

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

    @PostMapping("/profile/communities")
    public String updateCommunities(@RequestParam(required = false) List<Long> communityIds,
                                    Authentication authentication) {
        String email = authentication.getName();
        Person user = personService.findByEmail(email);

        Set<Community> selected = (communityIds != null)
                ? new HashSet<>(communityRepository.findAllById(communityIds))
                : new HashSet<>();
        user.setCommunities(selected);
        personService.save(user);
        return "redirect:/profile";
    }
}
