package com.localzero.api.config;

import com.localzero.api.entity.*;
import com.localzero.api.enumeration.InitiativeCategory;
import com.localzero.api.repository.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final PersonRepository personRepository;
    private final InitiativeRepository initiativeRepository;
    private final PostRepository postRepository;
    private final CommunityRepository communityRepository;

    @PostConstruct
    public void initData() {
        Community c1 = new Community();
        c1.setMemberEmail("Lund");
        communityRepository.save(c1);

        Community c2 = new Community();
        c2.setMemberEmail("Stockholm");
        communityRepository.save(c2);

        Community c3 = new Community();
        c3.setMemberEmail("Göteborg");
        communityRepository.save(c3);

        Person p1 = new Person();
        p1.setEmail("alice@example.com");
        p1.setName("Alice");
        p1.setPassword("{noop}pass");
        p1.setCommunities(Set.of(c1));
        personRepository.save(p1);

        Person p2 = new Person();
        p2.setEmail("bob@example.com");
        p2.setName("Bob");
        p2.setPassword("{noop}pass");
        p1.setCommunities(Set.of(c1));
        personRepository.save(p2);

        Person p3 = new Person();
        p3.setEmail("charlie@example.com");
        p3.setName("Charlie");
        p3.setPassword("{noop}pass");
        p1.setCommunities(Set.of(c2));
        personRepository.save(p3);

        Initiative i1 = new Initiative();
        i1.setTitle("Cykelverkstad");
        i1.setDescription("Laga cyklar");
        i1.setCategory(InitiativeCategory.TOOL_SHARING);
        i1.setLocation("Lund");
        i1.setStartDate(LocalDateTime.now().plusDays(1));
        i1.setEndDate(LocalDateTime.now().plusDays(3));
        i1.setPublic(true);
        i1.setCreator(p2);
        i1.setCommunityMember(p2);
        i1.setCreationDatetime(LocalDateTime.now());
        i1.setCommunities(Set.of(c1));
        i1.getParticipants().add(p2);
        initiativeRepository.save(i1);

        Initiative i2 = new Initiative();
        i2.setTitle("Kompostering i kvarteret");
        i2.setDescription("Skapa ett kompostsystem i trädgården");
        i2.setCategory(InitiativeCategory.RECYCLING);
        i2.setLocation("Malmö");
        i2.setStartDate(LocalDateTime.now().plusDays(2));
        i2.setEndDate(LocalDateTime.now().plusDays(5));
        i2.setPublic(true);
        i2.setCreator(p1);
        i2.setCommunityMember(p1);
        i2.setCreationDatetime(LocalDateTime.now());
        i2.setCommunities(Set.of(c1));
        i2.getParticipants().add(p1);
        initiativeRepository.save(i2);

        Post post1 = new Post();
        post1.setAuthor(p1);
        post1.setInitiative(i2);
        post1.setContent("Vi har fått hem nya kärl!");
        post1.setCreationDatetime(LocalDateTime.now());
        post1.setLikesCount(0);
        postRepository.save(post1);

        Post post2 = new Post();
        post2.setAuthor(p2);
        post2.setInitiative(i1);
        post2.setContent("Cykelverkstad startar imorgon!");
        post2.setCreationDatetime(LocalDateTime.now());
        post2.setLikesCount(0);
        postRepository.save(post2);

        Post standalone = new Post();
        standalone.setAuthor(p3);
        standalone.setContent("Någon som vill samåka till eko-marknaden?");
        standalone.setCreationDatetime(LocalDateTime.now());
        standalone.setLikesCount(0);
        postRepository.save(standalone);
    }
}