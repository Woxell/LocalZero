package com.localzero.api.service;

import com.localzero.api.entity.InitiativeParticipant;
import com.localzero.api.repository.InitiativeParticipantRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class InitiativeParticipantService {

    private InitiativeParticipantRepository initiativeParticipantRepository;

    public InitiativeParticipant save(InitiativeParticipant participant){
        return initiativeParticipantRepository.save(participant);
    }

    public InitiativeParticipant findByInitiativeIdAndPersonEmail(Long id, String email){
        return initiativeParticipantRepository.findByInitiativeIdAndPersonEmail(id, email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Deltagaren hittades inte."));
    }

    public Optional<InitiativeParticipant> optionalFindByInitiativeIdAndPersonEmail(Long id, String email){
        return initiativeParticipantRepository.findByInitiativeIdAndPersonEmail(id, email);
    }

    public boolean isParticipantInInitiative(Long id, String email){
        return initiativeParticipantRepository.findByInitiativeIdAndPersonEmail(id, email).isPresent();
    }

    public List<InitiativeParticipant> findByInitiativeId(Long id){
        return initiativeParticipantRepository.findByInitiativeId(id);
    }

    public void delete(InitiativeParticipant participant){
        initiativeParticipantRepository.delete(participant);
    }
}
