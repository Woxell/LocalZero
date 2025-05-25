package com.localzero.api.template;

import com.localzero.api.entity.EcoAction;
import com.localzero.api.entity.Initiative;
import com.localzero.api.entity.Person;
import com.localzero.api.entity.Post;
import com.localzero.api.service.InitiativeService;
import com.localzero.api.service.PersonService;
import com.localzero.api.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class PostCreator {

    private final PostService postService;
    private final PersonService personService;
    private final InitiativeService initiativeService;

    public Post create(String email, String content, Long initiativeId, EcoAction ecoAction, byte[] imageData) {
        Person person = personService.findByEmail(email);

        Post post = new Post();
        post.setAuthor(person);
        post.setContent(content);
        post.setCreationDatetime(LocalDateTime.now());

        if (initiativeId != null) {
            Initiative initiative = initiativeService.findById(initiativeId);
            post.setInitiative(initiative);
        }

        if (ecoAction != null) {
            post.setEcoAction(ecoAction);
        }
        if (imageData != null && imageData.length > 0) {
            post.setImage(imageData);
        }

        return postService.save(post);
    }
}
