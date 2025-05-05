package com.localzero.api.controller;

import com.localzero.api.entity.Person;
import com.localzero.api.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

    @Autowired
    private PersonRepository personRepository;

    @PostMapping(value = "/register", consumes = "application/json")
    public Person createPerson(@RequestBody Person person) {
        // Lösenord ej krypterade, Spring Security behöver därför {noop} prefix
        person.setPassword("{noop}" + person.getPassword());
        //Save sparar personen till databasen (blir en insert into person osv automatiskt)
        return personRepository.save(person);
    }

}
