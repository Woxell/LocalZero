package com.localzero.api.config;

import com.localzero.api.entity.*;
import com.localzero.api.enumeration.InitiativeCategory;
import com.localzero.api.repository.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final PersonRepository personRepository;
    private final InitiativeRepository initiativeRepository;
    private final PostRepository postRepository;
    private final CommunityRepository communityRepository;

    @PostConstruct
    public void initData() {
        // Skapa och spara community först
        Community community = new Community();
        community.setMemberEmail("test@example.com");
        community = communityRepository.save(community);

        // Skapa och spara person
        Person person = new Person();
        person.setEmail("test@example.com");
        person.setName("Test Användare");
        person.setPassword("{noop}password");
        person.setCommunity(community);
        person = personRepository.save(person);

        // Skapa och spara initiative
        Initiative initiative = new Initiative();
        initiative.setTitle("Miljöinitiativ");
        initiative.setDescription("Planterar träd i Malmö");
        initiative.setCategory(InitiativeCategory.ENVIRONMENT);
        initiative.setLocation("Malmö");
        initiative.setStartDate(LocalDateTime.now().plusDays(1));
        initiative.setEndDate(LocalDateTime.now().plusDays(10));
        initiative.setPublic(true);
        initiative.setCreator(person);
        initiative.setCommunity(community);
        initiative.setCommunityMember(person);
        initiative.setCreationDatetime(LocalDateTime.now());

        // ⚠️ Viktigt: spara först, sedan lägg till deltagare
        initiative = initiativeRepository.save(initiative);
        initiative.getParticipants().add(person);
        initiative = initiativeRepository.save(initiative); // spara igen med deltagare

        // Skapa och spara post
        Post post = new Post();
        post.setContent("Första inlägget i initiativet!");
        post.setAuthor(person);
        post.setInitiative(initiative);
        post.setCreationDatetime(LocalDateTime.now());
        post.setLikesCount(0);
        postRepository.save(post);
    }
}
