package com.localzero.api.entity;

/**
 * @author André
 * @author Emil
 */

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Initiative {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "creator_email", referencedColumnName = "email", nullable = false)
    private Person creator;

    private String title;

    private String description;

    @Column(name = "creation_datetime", nullable = false)
    private LocalDateTime creationDatetime;

    @ManyToOne
    @JoinColumn(name = "community_id", referencedColumnName = "id")
    private Community community;

    @ManyToOne
    @JoinColumn(name = "communitymember_email", referencedColumnName = "email", nullable = false)
    private Person communityMember;

}
