package com.localzero.api.entity;

import com.localzero.api.enumeration.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "initiative_membership")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InitiativeParticipant {

    @EmbeddedId
    private InitiativeParticipantId id = new InitiativeParticipantId();

    @ManyToOne @MapsId("initiativeId")
    private Initiative initiative;

    @ManyToOne @MapsId("personEmail")
    private Person person;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private LocalDateTime joinedAt;
}



