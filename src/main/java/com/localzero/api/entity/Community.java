package com.localzero.api.entity;

/**
 * @author: André
 */

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Data
@IdClass(CommunityId.class)
public class Community {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Id
    @Column(name = "member_email", insertable = false, updatable = false)
    private String memberEmail;

}
@Data
class CommunityId implements Serializable {
    private Long id;
    private String memberEmail;
}