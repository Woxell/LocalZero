package com.localzero.api.entity;

/**
 * @author André
 */

import jakarta.persistence.*;
import lombok.Data;

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

    @ManyToOne
    @JoinColumn(name = "community_id", referencedColumnName = "id")
    private Community community;
}