package com.localzero.api.service;

import com.localzero.api.entity.Post;
import com.localzero.api.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> getPostsByAuthorEmail(String email){
        return postRepository.findByAuthorEmailOrderByCreationDatetimeDesc(email);
    }
    public Post save(Post post){
        return postRepository.save(post);
    }
}
