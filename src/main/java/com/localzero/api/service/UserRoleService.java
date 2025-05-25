package com.localzero.api.service;

import com.localzero.api.entity.UserRoleAssignment;
import com.localzero.api.repository.UserRoleAssignmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserRoleService {

    private UserRoleAssignmentRepository userRoleAssignmentRepository;

    public UserRoleAssignment save(UserRoleAssignment roleAssignment){
        return userRoleAssignmentRepository.save(roleAssignment);
    }

    public List<UserRoleAssignment> findByEmail(String email){
        return userRoleAssignmentRepository.findByPersonEmail(email);
    }
}
