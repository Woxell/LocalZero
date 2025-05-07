package com.localzero.api.entity;

/**
 * @author Adrian Von krösus Schwenssonmssims 💀
 */

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

    @Entity
    @Data
    public class Notification {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "email", referencedColumnName = "email", nullable = false)
        private Person person;

        @Column(name = "description", nullable = false)
        private String description;

        @Column(name = "creation_datetime", nullable = false)
        private LocalDateTime creationDatetime;

    }

