package com.localzero.api.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Initiative {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @ManyToOne
    private Person createdBy;
}
