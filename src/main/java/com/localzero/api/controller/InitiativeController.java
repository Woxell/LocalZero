package com.localzero.api.controller;

import com.localzero.api.entity.Community;
import com.localzero.api.entity.Initiative;
import com.localzero.api.entity.Person;
import com.localzero.api.enumeration.InitiativeCategory;
import com.localzero.api.repository.CommunityRepository;
import com.localzero.api.repository.InitiativeRepository;
import com.localzero.api.repository.PersonRepository;
import com.localzero.api.service.InitiativeService;
import com.localzero.api.template.InitiativeCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/initiatives")
@RequiredArgsConstructor
public class InitiativeController {

    private final InitiativeCreator ic;
    private final InitiativeRepository initiativeRepository;
    private final PersonRepository personRepository;
    private final CommunityRepository communityRepository;
    private final InitiativeService initiativeService;

    @PostMapping("/create")
    public String createInitiative(@RequestParam String title,
                                   @RequestParam String description,
                                   @RequestParam String location,
                                   @RequestParam String startDate,
                                   @RequestParam String endDate,
                                   @RequestParam InitiativeCategory category,
                                   @RequestParam(required = false, defaultValue = "false") boolean isPublic,
                                   @RequestParam List<Long> communityIds,
                                   @AuthenticationPrincipal UserDetails user) {

        Initiative initiative = new Initiative();
        initiative.setTitle(title);
        initiative.setDescription(description);
        initiative.setLocation(location);
        initiative.setStartDate(LocalDateTime.parse(startDate));
        initiative.setEndDate(LocalDateTime.parse(endDate));
        initiative.setCategory(category);
        initiative.setPublic(isPublic);

        Person creator = personRepository.findByEmail(user.getUsername()).orElseThrow();
        initiative.setCreator(creator);
        initiative.setCommunityMember(creator);


        Set<Community> selectedCommunities = new HashSet<>(communityRepository.findAllById(communityIds));
        initiative.setCommunities(selectedCommunities);

        ic.create(user.getUsername(), initiative);

        return "redirect:/feed";
    }
    @GetMapping("/feed")
    public String showPublicInitiatives(Model model) {
        List<Initiative> publicInitiatives = initiativeService.getAll().stream()
                .filter(Initiative::isPublic)
                .toList();
        model.addAttribute("initiatives", publicInitiatives);
        return "initiative-feed";
    }


    @GetMapping("/new")
    public String showCreateInitiativeForm(Model model) {
        model.addAttribute("categories", InitiativeCategory.values());
        model.addAttribute("allCommunities", communityRepository.findAll());
        return "create-initiative";
    }
}
