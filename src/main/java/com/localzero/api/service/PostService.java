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

    public List<Post> getPostsByAuthorEmail(String email) {
        return postRepository.findByAuthorEmailOrderByCreationDatetimeDesc(email);
    }

    public Post save(Post post) {
        return postRepository.save(post);
    }

    public List<Post> getAllStandalonePosts() {
        return postRepository.findByInitiativeIsNullOrderByCreationDatetimeDesc();
    }

    public List<Post> getPostsByInitiativeId(Long initiativeId) {
        return postRepository.findByInitiativeIdOrderByCreationDatetimeDesc(initiativeId);
    }

    public void incrementLikes(long postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        post.setLikesCount(post.getLikesCount() + 1);
        postRepository.save(post);
    }
    public Post getById(long postId) {
        return postRepository.findById(postId).orElseThrow();
    }

}
