/*package com.localzero.api.controller;

import com.localzero.api.entity.Person;
import com.localzero.api.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @Autowired
    private PersonService ps;

    @GetMapping("/profile")
    public String showProfile(Model model, @AuthenticationPrincipal UserDetails currentUser){
        if(currentUser == null){
            return "redirect:/login";
        }

        Person person = ps.findByEmail(currentUser.getUsername());
        if (person == null){
            return "redirect:/login";
        }

        model.addAttribute("user",person);
        return "profile";
    }


}*/
