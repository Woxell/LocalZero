package com.localzero.api.service;

import com.localzero.api.Logger;
import com.localzero.api.entity.Person;
import com.localzero.api.entity.Post;
import com.localzero.api.repository.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PostService {

    @Autowired
    private final PostRepository postRepository;
    private final Logger logger = Logger.getInstance();

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Transactional(readOnly = true)
    public List<Post> getPostsByAuthorEmail(String email) {
        return postRepository.findByAuthorEmailOrderByCreationDatetimeDesc(email);
    }

    public Post save(Post post) {
        logger.log(post.getAuthor().getName() + " created a post: " + post.getContent());
        return postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public List<Post> getAllStandalonePosts() {
        return postRepository.findByInitiativeIsNullOrderByCreationDatetimeDesc();
    }

    @Transactional(readOnly = true)
    public List<Post> getPostsByInitiativeId(Long initiativeId) {
        return postRepository.findByInitiativeIdOrderByCreationDatetimeDesc(initiativeId);
    }

    public void incrementLikes(long postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        post.setLikesCount(post.getLikesCount() + 1);
        postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public Post getById(long postId) {
        return postRepository.findById(postId).orElseThrow(() ->
                new RuntimeException(logger.logError("Post not found with id: " + postId)));
    }

    public Optional<Post> findByContentAndAuthor(String content, Person author) {
        return postRepository.findByContentAndAuthor(content, author);

    }
}
