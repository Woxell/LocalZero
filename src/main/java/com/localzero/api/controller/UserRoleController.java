package com.localzero.api.controller;


import com.localzero.api.entity.Person;
import com.localzero.api.enumeration.UserRole;
import com.localzero.api.entity.UserRoleAssignment;
import com.localzero.api.repository.PersonRepository;
import com.localzero.api.repository.UserRoleAssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/roles")
public class UserRoleController {

    @Autowired
    private UserRoleAssignmentRepository roleRepo;
    @Autowired
    private PersonRepository personRepo;

    @PostMapping
    public UserRoleAssignment assignRole(@RequestParam String email, @RequestParam UserRole role) {

        Person p = personRepo.findById(email).orElseThrow();

        UserRoleAssignment assignment = new UserRoleAssignment();
        assignment.setPerson(p);
        assignment.setRole(role);
        return roleRepo.save(assignment);
        
    }

    @GetMapping
    public List<UserRoleAssignment> getRoles(@RequestParam String email) {
        return roleRepo.findByPersonEmail(email);
    }

}
