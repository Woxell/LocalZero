package com.localzero.api.controller;

/**
 * @author: André
 */

import com.localzero.api.entity.Person;
import com.localzero.api.service.PersonService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class FeedController {

    private PersonService personService;

    @GetMapping("/feed")
    public String feed(Authentication authentication, Model model) {
        String email = authentication.getName(); // Hämta inloggad e-post
        Person person = personService.findByEmail(email); // Hämta användaren via e-post
        model.addAttribute("name", person.getName()); // Lägg till det riktiga namnet i modellen
        return "feed"; // feed.html kommer att använda detta
    }
}