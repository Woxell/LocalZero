package com.localzero.api.service;

import com.localzero.api.Logger;
import com.localzero.api.entity.Person;
import com.localzero.api.repository.PersonRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
public class PersonService implements UserDetailsService {

    @Autowired
    private PersonRepository personRepository;
    private Logger logger = Logger.getInstance();

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        logger.log("Loading user by email: " + email);

        Optional<Person> person = findOptionalByEmail(email);
        if (person.isPresent()) {
            var personObj = person.get();
            return User.builder().
                    username(personObj.getEmail()).
                    password(personObj.getPassword()).
                    build();
        } else {
            ;
            throw new UsernameNotFoundException(logger.logError("User not found with email: " + email));
        }
    }

    public Person findByEmail(String email) {
        Person person = null;
        try{
            person = personRepository.findByEmail(email).orElseThrow(() -> new RuntimeException());
        } catch (RuntimeException e) {
            logger.logError("Error finding person by email: " + email + ", " + e);
        }
        return person;
    }

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public Optional<Person> findOptionalByEmail(String email) {
        return personRepository.findById(email);
    }

    public Person save(Person person) {
        return personRepository.save(person);
    }
}
