package com.localzero.api.config;

import com.localzero.api.entity.*;
import com.localzero.api.service.*;
import com.localzero.api.enumeration.InitiativeCategory;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final PersonService personService;
    private final InitiativeService initiativeService;
    private final PostService postService;
    private final CommunityService communityService;

    // src/main/java/com/localzero/api/config/DataInitializer.java
    @PostConstruct
    public void initData() {
        // Communities
        Community c1 = communityService.findByMemberEmail("Lund").orElseGet(() -> {
            Community c = new Community();
            c.setMemberEmail("Lund");
            return communityService.save(c);
        });
        Community c2 = communityService.findByMemberEmail("Stockholm").orElseGet(() -> {
            Community c = new Community();
            c.setMemberEmail("Stockholm");
            return communityService.save(c);
        });
        Community c3 = communityService.findByMemberEmail("Göteborg").orElseGet(() -> {
            Community c = new Community();
            c.setMemberEmail("Göteborg");
            return communityService.save(c);
        });

        // Persons
        Person p1 = personService.findOptionalByEmail("alice@example.com").orElseGet(() -> {
            Person p = new Person();
            p.setEmail("alice@example.com");
            p.setName("Alice");
            p.setPassword("{noop}pass");
            p.setCommunities(Set.of(c1));
            return personService.save(p);
        });
        Person p2 = personService.findOptionalByEmail("bob@example.com").orElseGet(() -> {
            Person p = new Person();
            p.setEmail("bob@example.com");
            p.setName("Bob");
            p.setPassword("{noop}pass");
            p.setCommunities(Set.of(c1));
            return personService.save(p);
        });
        Person p3 = personService.findOptionalByEmail("charlie@example.com").orElseGet(() -> {
            Person p = new Person();
            p.setEmail("charlie@example.com");
            p.setName("Charlie");
            p.setPassword("{noop}pass");
            p.setCommunities(Set.of(c2));
            return personService.save(p);
        });

        // Initiatives
        Initiative i1 = initiativeService.optionalFindByTitle("Cykelverkstad").orElseGet(() -> {
            Initiative i = new Initiative();
            i.setTitle("Cykelverkstad");
            i.setDescription("Laga cyklar");
            i.setCategory(InitiativeCategory.TOOL_SHARING);
            i.setLocation("Lund");
            i.setStartDate(LocalDateTime.now().plusDays(1));
            i.setEndDate(LocalDateTime.now().plusDays(3));
            i.setPublic(true);
            i.setCreator(p2);
            i.setCommunityMember(p2);
            i.setCreationDatetime(LocalDateTime.now());
            i.setCommunities(Set.of(c1));
            i.getParticipants().add(p2);
            return initiativeService.save(i);
        });
        Initiative i2 = initiativeService.optionalFindByTitle("Kompostering i kvarteret").orElseGet(() -> {
            Initiative i = new Initiative();
            i.setTitle("Kompostering i kvarteret");
            i.setDescription("Skapa ett kompostsystem i trädgården");
            i.setCategory(InitiativeCategory.RECYCLING);
            i.setLocation("Malmö");
            i.setStartDate(LocalDateTime.now().plusDays(2));
            i.setEndDate(LocalDateTime.now().plusDays(5));
            i.setPublic(true);
            i.setCreator(p1);
            i.setCommunityMember(p1);
            i.setCreationDatetime(LocalDateTime.now());
            i.setCommunities(Set.of(c1));
            i.getParticipants().add(p1);
            return initiativeService.save(i);
        });

        // Posts (use content+author as unique key for demo)
        if (postService.findByContentAndAuthor("Vi har fått hem nya kärl!", p1).isEmpty()) {
            Post post1 = new Post();
            post1.setAuthor(p1);
            post1.setInitiative(i2);
            post1.setContent("Vi har fått hem nya kärl!");
            post1.setCreationDatetime(LocalDateTime.now());
            post1.setLikesCount(0);
            postService.save(post1);
        }
        if (postService.findByContentAndAuthor("Cykelverkstad startar imorgon!", p2).isEmpty()) {
            Post post2 = new Post();
            post2.setAuthor(p2);
            post2.setInitiative(i1);
            post2.setContent("Cykelverkstad startar imorgon!");
            post2.setCreationDatetime(LocalDateTime.now());
            post2.setLikesCount(0);
            postService.save(post2);
        }
        if (postService.findByContentAndAuthor("Någon som vill samåka till eko-marknaden?", p3).isEmpty()) {
            Post standalone = new Post();
            standalone.setAuthor(p3);
            standalone.setContent("Någon som vill samåka till eko-marknaden?");
            standalone.setCreationDatetime(LocalDateTime.now());
            standalone.setLikesCount(0);
            postService.save(standalone);
        }
    }
}