package com.localzero.api.entity;
import jakarta.persistence.*;
import lombok.Data;

/**
 * @author: Mahyar, Emil
 */

@Entity
@Data
@Table(name = "eco_action")
public class EcoAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "author_email",nullable = false)
    private String authorEmail;

    @Column(name = "community_id")
    private Long communityId;

    private String content;

    @Column(name = "carbon_savings")
    private Float carbonSavings;

    @ManyToOne
    @JoinColumn(name = "community_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Community community;
}
