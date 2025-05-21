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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/initiatives")
public class InitiativeController {
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
                                   Principal principal
) {
    Person person = personRepository.findByEmail(principal.getName()).orElseThrow();
    Initiative initiative = new Initiative();
    initiative.setTitle(title);
    initiative.setDescription(description);
    initiative.setLocation(location);
    initiative.setStartDate(LocalDateTime.parse(startDate));
    initiative.setEndDate(LocalDateTime.parse(endDate));
    initiative.setCategory(category);
    initiative.setPublic(isPublic);
    initiative.setCreator(person);
    initiative.setCommunityMember(person);
    initiative.setCommunity(person.getCommunity());
    initiative.setCreationDatetime(LocalDateTime.now());
    initiativeRepository.save(initiative);

    System.out.println("Title:" + title);
    System.out.println("Desc." + description);
    return "redirect:/feed";
}
    @RequestMapping("/new")
    public String showCreateInitiativeForm(Model model) {
    model.addAttribute("categories", InitiativeCategory.values());
        return "create-initiative";
    }


}
