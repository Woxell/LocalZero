package com.localzero.api.controller;

import com.localzero.api.entity.Person;
import com.localzero.api.entity.Post;
import com.localzero.api.entity.PostComment;
import com.localzero.api.service.CommentService;
import com.localzero.api.service.PersonService;
import com.localzero.api.service.NotificationService;
import com.localzero.api.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
@AllArgsConstructor
public class CommentController {

    private CommentService commentService;
    private NotificationService notificationService;
    private PersonService personService;
    private PostService postService;

    @PostMapping("/comments")
    public String addComment(@RequestParam Long postId,
                             @RequestParam String content,
                             Authentication auth) {
        String email = auth.getName();
        Person author = personService.findByEmail(email);
        Post post = postService.getById(postId);

        PostComment comment = new PostComment();
        comment.setPost(post);
        comment.setAuthor(author);
        comment.setContent(content);
        comment.setCreationDatetime(LocalDateTime.now());
        commentService.save(comment);

        Person person = post.getAuthor();
        if (!author.getEmail().equals(person.getEmail())) {
            notificationService.notify(person, author.getName() + " Commented on your post");
        }

        return "redirect:/feed";
    }
}
