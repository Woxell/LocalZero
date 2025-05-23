package com.localzero.api.template;

import com.localzero.api.entity.EcoAction;
import com.localzero.api.entity.Initiative;
import com.localzero.api.entity.Person;
import com.localzero.api.entity.Post;
import com.localzero.api.repository.InitiativeRepository;
import com.localzero.api.repository.PersonRepository;
import com.localzero.api.repository.PostRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PostCreator {

    private final PostRepository postRepository;
    private final PersonRepository personRepository;
    private final InitiativeRepository initiativeRepository;

    public PostCreator(PostRepository postRepository, PersonRepository personRepository, InitiativeRepository initiativeRepository) {
        this.postRepository = postRepository;
        this.personRepository = personRepository;
        this.initiativeRepository = initiativeRepository;
    }

    public Post create(String email, String content, Long initiativeId, EcoAction ecoAction) {
        Person person = personRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("User not found with email: " + email));

        Post post = new Post();
        post.setAuthor(person);
        post.setContent(content);
        post.setCreationDatetime(LocalDateTime.now());

        if (initiativeId != null) {
            Initiative initiative = initiativeRepository.findById(initiativeId).orElse(null);
            post.setInitiative(initiative);
        }

        if (ecoAction != null) {
            post.setEcoAction(ecoAction);
        }

        return postRepository.save(post);
    }
}
