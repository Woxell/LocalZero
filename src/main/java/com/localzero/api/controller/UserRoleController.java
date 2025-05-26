package com.localzero.api.controller;


import com.localzero.api.entity.Person;
import com.localzero.api.enumeration.UserRole;
import com.localzero.api.entity.UserRoleAssignment;
import com.localzero.api.service.PersonService;
import com.localzero.api.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/roles")
public class UserRoleController {

    private UserRoleService roleService;
    private PersonService personService;

    @PostMapping
    public UserRoleAssignment assignRole(@RequestParam String email, @RequestParam UserRole role) {

        Person p = personService.findByEmail(email);

        UserRoleAssignment assignment = new UserRoleAssignment();
        assignment.setPerson(p);
        assignment.setRole(role);
        return roleService.save(assignment);
    }

    @GetMapping
    public List<UserRoleAssignment> getRoles(@RequestParam String email) {
        return roleService.findByEmail(email);
    }

}
