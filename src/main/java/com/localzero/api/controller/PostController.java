package com.localzero.api.controller;

import com.localzero.api.entity.EcoAction;
import com.localzero.api.entity.Notification;
import com.localzero.api.entity.Person;
import com.localzero.api.enumeration.EcoActionType;
import com.localzero.api.entity.Post;
import com.localzero.api.repository.NotificationsRepository;
import com.localzero.api.service.*;
import com.localzero.api.template.PostCreator;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class PostController {

    private final PostService postService;
    private final PostCreator pCreator;
    private final InitiativeService inservice;
    private final EcoActionService ecoActionService;
    private final PersonService personService;
    private final NotificationsRepository notificationsRepository;
    private final NotificationService notificationService;

    public PostController(PostService postService, PostCreator pCreator,
                          InitiativeService inservice, EcoActionService ecoActionService,
                          PersonService personService, NotificationsRepository notificationsRepository,
                          NotificationService notificationService) {
        this.postService = postService;
        this.pCreator = pCreator;
        this.inservice = inservice;
        this.ecoActionService = ecoActionService;
        this.personService = personService;
        this.notificationsRepository = notificationsRepository;
        this.notificationService = notificationService;
    }


    @GetMapping("/create-post")
    public String ShowCreatedPostForm(Model model, @AuthenticationPrincipal UserDetails currentUser) {
        if (currentUser != null) {
            model.addAttribute("initiatives", inservice.getByParticipant(currentUser.getUsername()));
        } else {
            model.addAttribute("initiatives", inservice.getAll());
        }
        return "create-post";
    }

    @PostMapping("/create-post")
    public String createPost(@RequestParam String content,
                             @RequestParam(required = false) Long initiativeId,
                             @RequestParam(required = false) String ecoContent,
                             @RequestParam(required = false) MultipartFile image,
                             @AuthenticationPrincipal UserDetails currentUser) throws IOException {
        if (currentUser == null) {
            return "redirect:/login";
        }

        EcoAction ecoAction = null;

        if (ecoContent != null && !ecoContent.isBlank()) {
            EcoActionType type = EcoActionType.fromLabel(ecoContent);
            if (type != null) {
                Person person = personService.findByEmail(currentUser.getUsername());

                ecoAction = new EcoAction();
                ecoAction.setAuthorEmail(person.getEmail());
                ecoAction.setContent(type.getLabel());
                ecoAction.setCarbonSavings(type.getCarbonSavings());

                Long communityId = person.getCommunities().stream()
                        .findFirst()
                        .map(c -> c.getId())
                        .orElse(1L);

                ecoAction.setCommunityId(communityId);
                ecoActionService.save(ecoAction);
            }
        }
        byte[] imageData = null;
        if (image != null && !image.isEmpty()) {
            imageData = image.getBytes();
        }

        Post post = pCreator.create(currentUser.getUsername(), content, initiativeId, ecoAction, imageData);

        if (initiativeId != null) {
            return "redirect:/initiatives/" + initiativeId;
        }

        return "redirect:/feed";
    }

    @GetMapping("/posts/image/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getPostImage(@PathVariable long id) {
        Post post = postService.getById(id);
        if (post.getImage() != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(post.getImage());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/posts/{id}/like")
    public String likePost(@PathVariable long id,
                           @RequestParam String source,
                           @AuthenticationPrincipal UserDetails currentUser) {
        if (currentUser == null) {
            return "redirect:/login";
        }

        Post post = postService.getById(id);
        Person liker = personService.findByEmail(currentUser.getUsername());
        Person postAuthor = post.getAuthor();

        postService.incrementLikes(id);

        if (!liker.getEmail().equals(postAuthor.getEmail())) {
            notificationService.notify(postAuthor, liker.getName() + " liked your post.");
        }

        if ("feed".equals(source)) {
            return "redirect:/feed";
        } else {
            return "redirect:/profile/" + liker.getEmail();
        }
    }

}
