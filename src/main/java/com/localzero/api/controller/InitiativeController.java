package com.localzero.api.controller;

/**
 * @author: Emil
 */

import com.localzero.api.entity.Initiative;
import com.localzero.api.entity.Person;
import com.localzero.api.repository.InitiativeRepository;
import com.localzero.api.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping("/initiatives")
public class InitiativeController {
@Autowired
    private InitiativeRepository initiativeRepository;
@Autowired
    private PersonRepository personRepository;
@PostMapping("/create")
    public String createInitiative(@RequestParam String title, @RequestParam String description, Principal principal) {
    Person person = personRepository.findByEmail(principal.getName()).orElseThrow();
    Initiative initiative = new Initiative();
    initiative.setTitle(title);
    initiative.setDescription(description);
    initiative.setCreator(person);

    initiativeRepository.save(initiative);

    System.out.println("Title:" + title);
    System.out.println("Desc." + description);
    return "redirect:/feed";
}

}
