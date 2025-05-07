package com.localzero.api.entity;

/**
 * @author André
 */

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Data
@IdClass(InitiativeParticipantId.class)
public class InitiativeParticipant {

    @Id
    @Column(name = "initiative_id", nullable = false)
    private Long initiativeId;

    @Id
    @Column(name = "participant_email", nullable = false)
    private String participantEmail;

    @ManyToOne
    @JoinColumn(name = "initiative_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Initiative initiative;

    @ManyToOne
    @JoinColumn(name = "participant_email", referencedColumnName = "email", insertable = false, updatable = false)
    private Person participant;
}

@Data
class InitiativeParticipantId implements Serializable {
    private Long initiativeId;
    private String participantEmail;
}