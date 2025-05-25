package com.localzero.api.service;

import com.localzero.api.Logger;
import com.localzero.api.entity.Community;
import com.localzero.api.entity.Initiative;
import com.localzero.api.entity.Person;
import com.localzero.api.repository.InitiativeRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class InitiativeService {

    @Autowired
    private final InitiativeRepository initiativeRepository;
    @Autowired
    private final PersonService personService;
    private final Logger logger = Logger.getInstance();

    public InitiativeService(InitiativeRepository initiativeRepository, PersonService personService) {
        this.initiativeRepository = initiativeRepository;
        this.personService = personService;
    }
    public Initiative findById(Long id) {
        return initiativeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Initiative save(Initiative initiative) {
        if (initiative.getCreator() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, logger.logError("Initiative creator cannot be null."));
        else {
            logger.log(initiative.getCreator().getName() + " created initiative: " + initiative.getTitle());
            return initiativeRepository.save(initiative);
        }
    }

    public List<Initiative> getAll(){
        return initiativeRepository.findAll();
    }

    public List<Initiative> getByParticipant(String email){
        return initiativeRepository.findByParticipantEmail(email);
    }
    public Initiative getById(Long id){
        return initiativeRepository.findById(id).orElseThrow();
    }

    public List<Initiative> getAllPublic() {
        return initiativeRepository.findByIsPublicTrue();
    }

    public List<Initiative> getPublicOrByCommunity(Community community) {
        return initiativeRepository.findByIsPublicTrueOrCommunitiesContaining(community);
    }

    public List<Initiative> getPublicOrByCommunities(Set<Community> communities) {
        return initiativeRepository.findByIsPublicTrueOrCommunitiesIn(communities);
    }

    public List<Initiative> getVisibleForUser(Person person) {
        Set<Community> communities = person.getCommunities();
        String email = person.getEmail();

        Set<Initiative> all = new HashSet<>();
        all.addAll(initiativeRepository.findByIsPublicTrueOrCommunitiesIn(communities));
        all.addAll(initiativeRepository.findByCreatorEmail(email));

        return new ArrayList<>(all);
    }

    public void addParticipant(Long initiativeId, String email) {
        Initiative initiative = initiativeRepository.findById(initiativeId).orElseThrow();
        Person person = personService.findByEmail(email);
        initiative.getParticipants().add(person);
        initiativeRepository.save(initiative);
    }

    public void removeParticipant(Long initiativeId, String userEmail) {
        Initiative initiative = initiativeRepository.findById(initiativeId).orElseThrow();
        Person person = personService.findByEmail(userEmail);

        initiative.getParticipants().remove(person);
        initiativeRepository.save(initiative);
    }

}
