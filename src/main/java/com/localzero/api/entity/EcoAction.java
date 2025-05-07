package com.localzero.api.entity;
import jakarta.persistence.*;
import lombok.Data;

/**
 * @author: Mahyar
 */

@Entity
@Data
@Table(name = "eco_action")
public class EcoAction {

    @Id
    @Column(name = "author_email")
    private String authorEmail;

    @Column(name = "community_id")
    private String communityId;

    private String content;

    @Column(name = "carbon_savings")
    private Float carbonSavings;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "author_email", referencedColumnName = "member_email", insertable = false, updatable = false),
            @JoinColumn(name = "community_id", referencedColumnName = "id", insertable = false, updatable = false)
    })
    private Community community;
}
