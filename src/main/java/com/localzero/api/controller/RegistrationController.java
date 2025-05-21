package com.localzero.api.controller;

/**
 * @author: André
 */

import com.localzero.api.entity.Person;
import com.localzero.api.entity.UserRoleAssignment;
import com.localzero.api.enumeration.UserRole;
import com.localzero.api.repository.PersonRepository;
import lombok.Data;
import lombok.SneakyThrows;
import org.antlr.v4.runtime.misc.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    public String handleRegister(@RequestBody RegistrationRequest rq) {
        try {
            Person person = new Person();
            person.setName(rq.getName());
            person.setEmail(rq.getEmail());
            person.setPassword("{noop}" + rq.getPassword()); // Lägger till {noop} prefix för att undvika kryptering
            if (person.getProfilePic() != null && person.getProfilePic().length == 0) {
                person.setProfilePic(null);
            }
            personRepository.save(person);
            System.out.println("Person saved!!!!!!!!");
            return "redirect:/login"; // Omregistrering lyckades, omdirigera till inloggning
        } catch (Exception e) {
            System.out.println("Redirect failed!!!!!!!!");
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
