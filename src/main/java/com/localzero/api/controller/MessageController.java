package com.localzero.api.controller;

import com.localzero.api.entity.DirectMessage;
import com.localzero.api.entity.Person;
import com.localzero.api.service.DMService;
import com.localzero.api.service.NotificationService;
import com.localzero.api.service.PersonService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

@Controller
@RequestMapping("/messages")
public class MessageController {

    private DMService dmService;
    private NotificationService notificationService;
    private PersonService personService;
    private SSEProxy sseProxy;
    private boolean isDevelopment;

    public MessageController(
            DMService dmService,
            NotificationService notificationService,
            PersonService personService,
            SSEProxy sseProxy,
            @Value("${is_development}") String isDevelopment
    ) {
        this.dmService = dmService;
        this.notificationService = notificationService;
        this.personService = personService;
        this.sseProxy = sseProxy;
        this.isDevelopment = isDevelopment.equals("true");
    }

    @GetMapping
    public String renderChatPage(Authentication authentication, Model model) {
        String loggedInUserEmail = authentication.getName(); // Get the logged-in user's email

        List<DirectMessage> messages = new ArrayList<>();
        messages.addAll(dmService.findByReceiverEmail(loggedInUserEmail));
        messages.addAll(dmService.findBySenderEmail(loggedInUserEmail));

        List<Person> persons = personService.findAll();
        for (Person p : persons)
            System.out.println("Person: " + p);

        Set<String> chatPartners = new HashSet<>();
        for (DirectMessage m : messages) {
            if (!m.getSenderEmail().equals(loggedInUserEmail)) chatPartners.add(m.getSenderEmail());
            if (!m.getReceiverEmail().equals(loggedInUserEmail)) chatPartners.add(m.getReceiverEmail());
        }
        model.addAttribute("base_url", isDevelopment ? "http://localhost:8080" : "https://localzero.se");
        model.addAttribute("chatPartners", getChatPartners(authentication));
        model.addAttribute("messages", messages); // Add messages to the model
        model.addAttribute("persons", persons);
        model.addAttribute("user", personService.findByEmail(loggedInUserEmail));

        return "messages";
    }

    @GetMapping("/chatpartners")
    @ResponseBody
    public Set<String> getChatPartners(Authentication authentication) {
        String loggedInUserEmail = authentication.getName();
        List<DirectMessage> messages = new ArrayList<>();
        messages.addAll(dmService.findByReceiverEmail(loggedInUserEmail));
        messages.addAll(dmService.findBySenderEmail(loggedInUserEmail));
        Set<String> chatPartners = new HashSet<>();
        for (DirectMessage m : messages) {
            if (!m.getSenderEmail().equals(loggedInUserEmail)) chatPartners.add(m.getSenderEmail());
            if (!m.getReceiverEmail().equals(loggedInUserEmail)) chatPartners.add(m.getReceiverEmail());
        }
        return chatPartners;
    }

    @PostMapping
    @ResponseBody
    public DirectMessage sendMessage(@RequestBody DirectMessage message, Authentication authentication) {
        message.setCreationDatetime(LocalDateTime.now());
        message.setSenderEmail(authentication.getName());
        DirectMessage saved = dmService.save(message);

        // Notify receiver via SSE
        sseProxy.authenticateAndNotifyReceiver(message.getReceiverEmail(), saved);

        notificationService.notify(personService.findByEmail(message.getReceiverEmail()), "New Message from " + message.getSenderEmail());
        return saved;
    }

    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public DirectMessage sendImageMessage(
            @RequestParam("senderEmail") String senderEmail,
            @RequestParam("receiverEmail") String receiverEmail,
            @RequestParam("file") MultipartFile file) throws IOException {

        DirectMessage message = new DirectMessage();
        message.setSenderEmail(senderEmail);
        message.setReceiverEmail(receiverEmail);
        message.setCreationDatetime(LocalDateTime.now());
        message.setContent("image");
        message.setImageData(file.getBytes());

        DirectMessage saved = dmService.save(message);
        notificationService.notify(personService.findByEmail(receiverEmail), "New Image Message from " + senderEmail);
        return saved;
    }

    @GetMapping("/image/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getImage(@PathVariable long id) {  //Skickar tillbaka bildens innehåll som bytes
        DirectMessage message = dmService.findById(id);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(message.getImageData());
    }

    @GetMapping(value = "/{email}")
    @ResponseBody
    public List<DirectMessage> getMessages(@PathVariable String email, Authentication authentication) { //@RequestParam får då users mail. Frontend viktigt att bestämma users mail på rätt sätt! Så att denna metod kan hitta rätt
        String loggedInUserEmail = authentication.getName();

        List<DirectMessage> conversation = dmService.findConversationBetween(loggedInUserEmail, email);

        if (conversation.isEmpty()) {
            DirectMessage initialMessage = new DirectMessage();
            initialMessage.setSenderEmail(loggedInUserEmail);
            initialMessage.setReceiverEmail(email);
            initialMessage.setContent("Conversation started");
            initialMessage.setCreationDatetime(LocalDateTime.now());
            dmService.save(initialMessage);
            conversation.add(initialMessage);
        }

        return conversation;
    }
}