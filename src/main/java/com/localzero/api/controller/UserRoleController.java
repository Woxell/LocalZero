package com.localzero.api.controller;


import com.localzero.api.repository.PersonRepository;
import com.localzero.api.repository.UserRoleAssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/roles")
public class UserRoleController {

    @Autowired
    private UserRoleAssignmentRepository roleRepo;
    @Autowired
    private PersonRepository personRepo;

    @PostMapping
    public UserRoleAssignmnet assignRole(@RequestParam String email, @Requestparam UserRole role) {

        Person p = personRepo.fiindById(email).orElseThrow();

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
