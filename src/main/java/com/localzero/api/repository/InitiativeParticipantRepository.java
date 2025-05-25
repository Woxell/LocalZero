package com.localzero.api.repository;

import com.localzero.api.entity.InitiativeParticipant;
import com.localzero.api.entity.InitiativeParticipantId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InitiativeParticipantRepository
        extends JpaRepository<InitiativeParticipant, InitiativeParticipantId> {

    List<InitiativeParticipant> findByInitiativeId(Long initiativeId);
    Optional<InitiativeParticipant> findByInitiativeIdAndPersonEmail(Long initiativeId, String email);
}
