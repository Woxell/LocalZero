package com.localzero.api.controller;

/**
 * @author: André
 */

import com.localzero.api.entity.Person;
import com.localzero.api.repository.PersonRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class RegistrationController {

    @Autowired
    private PersonRepository personRepository;

    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        return "register";
    }

//    @PostMapping( "/register")
//    public Person createPerson(@RequestBody Person person) {
//        // Lösenord ej krypterade, Spring Security behöver därför {noop} prefix
//        person.setPassword("{noop}" + person.getPassword());
//        //Save sparar personen till databasen (blir en insert into person osv automatiskt)
//        return personRepository.save(person);
//    }


    // Denna metod används för att hantera registrering av nya användare
    @SneakyThrows
    @PostMapping("/register")
    public String handleRegister(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("profilePicture") MultipartFile profilePicture
    ) {
        try {
            Person person = new Person();
            person.setName(name);
            person.setEmail(email);
            person.setPassword("{noop}" + password); // Lägger till {noop} prefix för att undvika kryptering
            person.setProfilePic(profilePicture.getBytes()); // Spara bilden som byte-array
            personRepository.save(person);
            return "redirect:/login"; // Omregistrering lyckades, omdirigera till inloggning
        } catch (Exception e) {
            e.printStackTrace();
            return "error"; // Om något går fel, returnera ett felmeddelande
        }
    }


}
