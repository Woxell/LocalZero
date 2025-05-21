package com.localzero.api.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Data;
import com.localzero.api.enumeration.UserRole;

@Entity
@Data
public class UserRoleAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "email", referencedColumnName = "email")
    private Person person;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

}
