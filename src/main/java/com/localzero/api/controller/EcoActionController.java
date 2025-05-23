package com.localzero.api.controller;

import org.springframework.ui.Model;
import com.localzero.api.entity.Community;
import com.localzero.api.entity.EcoAction;
import com.localzero.api.entity.Person;
import com.localzero.api.service.EcoActionService;
import com.localzero.api.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/eco-actions")
@RequiredArgsConstructor
public class EcoActionController {

    private final EcoActionService ecoActionService;
    private final PersonService personService;

    @GetMapping
    public String showActions(Model model, @AuthenticationPrincipal UserDetails currentUser) {
        String email = currentUser.getUsername();
        List<EcoAction> actions = ecoActionService.getAllByUser(email);
        model.addAttribute("actions", actions);
        return "eco-actions";
    }

    @PostMapping
    public String submitEcoAction(@RequestParam String content,
                                  @RequestParam Float carbonSavings,
                                  @AuthenticationPrincipal UserDetails currentUser) {
        Person user = personService.findByEmail(currentUser.getUsername());

        EcoAction action = new EcoAction();
        action.setAuthorEmail(user.getEmail());
        action.setCommunityId(user.getCommunities().stream().findFirst().map(Community::getId).orElse(1L));
        action.setContent(content);
        action.setCarbonSavings(carbonSavings);

        ecoActionService.save(action);
        return "redirect:/eco-actions";
    }
}

