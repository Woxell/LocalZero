package com.localzero.api.entity;

/**
 * @author: André , Emil
 */

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Community {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_email", nullable = false)
    private String memberEmail;

}