package com.localzero.api.entity;

/**
 * @author André
 */

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class Person {

    @Id
    private String email;

    private String name;

    private String password;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "profile_pic")
    private byte[] profilePic;

    @ManyToMany
    private Set<Community> communities = new HashSet<>();
}