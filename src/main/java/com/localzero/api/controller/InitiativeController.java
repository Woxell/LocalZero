package com.localzero.api.controller;

/**
 * @author: Emil
 */

import com.localzero.api.entity.Initiative;
import com.localzero.api.entity.Person;
import com.localzero.api.enumeration.InitiativeCategory;
import com.localzero.api.repository.InitiativeRepository;
import com.localzero.api.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.localzero.api.template.InitiativeCreator;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/initiatives")
public class InitiativeController {

    private final InitiativeCreator ic;

    public InitiativeController(InitiativeCreator ic){
        this.ic = ic;
    }

@Autowired
    private InitiativeRepository initiativeRepository;
@Autowired
    private PersonRepository personRepository;


    @PostMapping("/create")
    public String createInitiative(@RequestParam String title,
                                   @RequestParam String description,
                                   @RequestParam String location,
                                   @RequestParam String startDate,
                                   @RequestParam String endDate,
                                   @RequestParam InitiativeCategory category,
                                   @RequestParam(required = false, defaultValue = "false") boolean isPublic,
                                   @AuthenticationPrincipal UserDetails user) {

        Initiative initiative = new Initiative();
        initiative.setTitle(title);
        initiative.setDescription(description);
        initiative.setLocation(location);
        initiative.setStartDate(LocalDateTime.parse(startDate));
        initiative.setEndDate(LocalDateTime.parse(endDate));
        initiative.setCategory(category);
        initiative.setPublic(isPublic);

        ic.create(user.getUsername(), initiative);

        return "redirect:/feed";
    }    @RequestMapping("/new")
    public String showCreateInitiativeForm(Model model) {
    model.addAttribute("categories", InitiativeCategory.values());
        return "create-initiative";
    }


}
