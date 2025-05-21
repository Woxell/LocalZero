package com.localzero.api.template;

import com.localzero.api.entity.Person;
import com.localzero.api.entity.Post;
import com.localzero.api.repository.PostRepository;
import com.localzero.api.service.PersonService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PostCreator extends template.AbstractCreator<Post> {

    private final PostRepository postRepo;
    private final PersonService personService;

    public PostCreator(PostRepository postRepo, PersonService personService){
        this.postRepo = postRepo;
        this.personService = personService;
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
        return post;
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
