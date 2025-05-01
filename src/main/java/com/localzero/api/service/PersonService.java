package com.localzero.api.service;

import com.localzero.api.entity.Person;
import com.localzero.api.repository.PersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class PersonService implements UserDetailsService {

    @Autowired
    private final PersonRepository personRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Person> person = personRepository.findByEmail(email);
        if (person.isPresent()) {
            var personObj = person.get();
            return User.builder().
                    username(personObj.getEmail()).
                    password(personObj.getPassword()).
                    build();
        } else {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
    }

}
