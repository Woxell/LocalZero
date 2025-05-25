package com.localzero.api.template;

import com.localzero.api.entity.Initiative;
import com.localzero.api.entity.Person;
import com.localzero.api.service.InitiativeService;
import com.localzero.api.service.PersonService;
import org.springframework.stereotype.Service;

@Service
public class InitiativeCreator extends AbstractInitiativeCreator {

    private final InitiativeService initiativeService;
    private final PersonService personService;

    public InitiativeCreator(InitiativeService initiativeService, PersonService personService){
        this.initiativeService = initiativeService;
        this.personService = personService;
    }

    @Override
    protected Person loadUser(String email) {
        return personService.findByEmail(email);
    }

    @Override
    protected void initiativedetailer(Person user, Initiative initiative) {
        initiative.setCreator(user);
        initiative.setCommunityMember(user);
        initiative.getParticipants().add(user);
    }

    @Override
    protected Initiative save(Initiative initiative) {
        Initiative saved = initiativeService.save(initiative);
        System.out.println(" SPARAT: ID=" + saved.getId() + " - " + saved.getTitle());
        return saved;
    }
}

