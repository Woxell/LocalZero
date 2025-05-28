package com.localzero.api.controller;

/**
 * @author: André
 */

import com.localzero.api.entity.Person;
import com.localzero.api.enumeration.UserRole;
import com.localzero.api.service.PersonService;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class RegistrationController {

    @Autowired
    private PersonService personService;
    private boolean isDevelopment;

    public RegistrationController(@Value("${is_development}") String isDevelopment) {
        this.isDevelopment = isDevelopment.equals("true");
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        model.addAttribute("base_url", isDevelopment ? "http://localhost:8080" : "https://localzero.se");
        return "register";
    }

    // Denna metod används för att hantera registrering av nya användare
    @SneakyThrows
    @PostMapping("/register")
    public String handleRegister(@RequestBody RegistrationRequest rq) {
        try {
            Person person = new Person();
            person.setName(rq.getName());
            person.setEmail(rq.getEmail());
            person.setPassword("{noop}" + rq.getPassword()); // Lägger till {noop} prefix för att undvika kryptering
            if (person.getProfilePic() != null && person.getProfilePic().length == 0) {
                person.setProfilePic(null);
            }
            personService.save(person);
            return "redirect:/login"; // Omregistrering lyckades, omdirigera till inloggning
        } catch (Exception e) {
            e.printStackTrace();
            return "error"; // Om något går fel, returnera ett felmeddelande
        }
    }
}

@Data
class RegistrationRequest {
    private String name;
    private String email;
    private String password;
    private byte[] profilePicture;
    private List<UserRole> roles;
}
