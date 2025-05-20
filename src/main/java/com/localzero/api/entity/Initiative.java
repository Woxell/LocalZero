package com.localzero.api.entity;

/**
 * @author André
 * @author Emil
 */

import com.localzero.api.enumeration.InitiativeCategory;
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

    @Column(nullable = false)
    private String title;

    @Column(nullable = false,columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InitiativeCategory category;

    @Column(name = "start_date",nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date",nullable = false)
    private LocalDateTime endDate;

    @Column(name = "is_public",nullable = false)
    private boolean isPublic;

    @Column(name = "creation_datetime", nullable = false)
    private LocalDateTime creationDatetime;

    @ManyToOne
    @JoinColumn(name = "community_id", referencedColumnName = "id")
    private Community community;

    @ManyToOne
    @JoinColumn(name = "communitymember_email", referencedColumnName = "email", nullable = false)
    private Person communityMember;

}
