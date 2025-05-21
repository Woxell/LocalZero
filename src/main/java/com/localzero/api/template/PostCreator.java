package com.localzero.api.template;

import com.localzero.api.entity.Initiative;
import com.localzero.api.entity.Person;
import com.localzero.api.entity.Post;
import com.localzero.api.repository.InitiativeRepository;
import com.localzero.api.repository.PostRepository;
import com.localzero.api.service.PersonService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PostCreator extends template.AbstractCreator<Post> {

    private final PostRepository postRepo;
    private final PersonService personService;
    private final InitiativeRepository initiativeRepository;

    public PostCreator(PostRepository postRepo, PersonService personService, InitiativeRepository initiativeRepository){
        this.postRepo = postRepo;
        this.personService = personService;
        this.initiativeRepository = initiativeRepository;
    }

    @Override
    protected Person loadUser(String email) {
        return personService.findByEmail(email);
    }

    @Override
    protected Post build(Person user,String content) {
        Post post = new Post();
        post.setAuthor(user);
        post.setContent(content);
        post.setLikesCount(0);
        return post;
    }

    public Post create(String email, String content,Long initiativeId){
        Post post = super.create(email,content);
        if (initiativeId != null){
            Initiative initiative = initiativeRepository.findById(initiativeId).orElseThrow(()->new IllegalArgumentException("The initiative does not exist!"));
            post.setInitiative(initiative);
        }
        return postRepo.save(post);
    }

    @Override
    protected void setTimestamps(Post entity) {
        entity.setCreationDatetime(LocalDateTime.now());
    }

    @Override
    protected Post persist(Post entity) {
        return postRepo.save(entity);
    }


}
