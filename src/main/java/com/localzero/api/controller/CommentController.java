package com.localzero.api.controller;

import com.localzero.api.entity.Person;
import com.localzero.api.entity.Post;
import com.localzero.api.entity.PostComment;
import com.localzero.api.repository.PostCommentRepository;
import com.localzero.api.repository.PostRepository;
import com.localzero.api.service.PersonService;
import com.localzero.api.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
@AllArgsConstructor
public class CommentController {

    private final PostRepository postRepo;
    private final PersonService personService;
    private final PostCommentRepository commentRepo;
    private final NotificationService notificationService;


    @PostMapping("/comments")
    public String addComment(@RequestParam Long postId,
                             @RequestParam String content,
                             Authentication auth) {
        String email = auth.getName();
        Person author = personService.findByEmail(email);
        Post post = postRepo.findById(postId).orElseThrow();

        PostComment comment = new PostComment();
        comment.setPost(post);
        comment.setAuthor(author);
        comment.setContent(content);
        comment.setCreationDatetime(LocalDateTime.now());
        commentRepo.save(comment);

        Person person = post.getAuthor();
        if (!author.getEmail().equals(person.getEmail())) {
            notificationService.notify(person,author.getName() + " Commented on your post");
        }

        return "redirect:/feed";
    }
}
