package com.localzero.api.entity;

/**
 * @author André
 * @author Emil
 */

import com.localzero.api.enumeration.InitiativeCategory;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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

    @ManyToMany
    @JoinTable(
            name = "initiative_community",
            joinColumns = @JoinColumn(name = "initiative_id"),
            inverseJoinColumns = @JoinColumn(name = "community_id")
    )
    private Set<Community> communities = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "communitymember_email", referencedColumnName = "email")
    private Person communityMember;

    @ManyToMany
    @JoinTable(
            name = "initiative_participant",
            joinColumns = @JoinColumn(name = "initiative_id"),
            inverseJoinColumns = @JoinColumn(name = "participant_email")
    )
    private Set<Person> participants = new HashSet<>();

}
