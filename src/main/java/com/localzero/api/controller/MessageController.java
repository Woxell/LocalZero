package com.localzero.api.controller;

import com.localzero.api.entity.DirectMessage;
import com.localzero.api.entity.Notification;
import com.localzero.api.entity.Person;
import com.localzero.api.repository.DirectMessageRepository;
import com.localzero.api.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.localzero.api.repository.NotificationsRepository;
import com.localzero.api.repository.PersonRepository;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private DirectMessageRepository directMessageRepository;  //repository object som Spring Boot skapar varje gång vi kallar directMessageRepository
    @Autowired
    private NotificationsRepository notificationRepo;
    @Autowired
    private PersonRepository personRepo;

    @Autowired
    private SSEController sseController;

    private PersonService personService;

    public MessageController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public String renderChatPage(Authentication authentication, Model model) {
        String loggedInUserEmail = authentication.getName(); // Get the logged-in user's email

        List<DirectMessage> messages = new ArrayList<>();
        messages.addAll(directMessageRepository.findByReceiverEmail(loggedInUserEmail));
        messages.addAll(directMessageRepository.findBySenderEmail(loggedInUserEmail));

        List<Person> persons = personService.findAll();
        for (Person p : persons)
            System.out.println("Person: " + p);

        model.addAttribute("messages", messages); // Add messages to the model
        model.addAttribute("persons", persons);

        return "messages"; // Resolves messages.html
    }

    @PostMapping
    @ResponseBody
    public DirectMessage sendMessage(@RequestBody DirectMessage message, Authentication authentication) {
        System.out.println(message.getContent());
        System.out.println(message.getReceiverEmail());
        message.setCreationDatetime(LocalDateTime.now());
        message.setSenderEmail(authentication.getName());
        DirectMessage saved = directMessageRepository.save(message);

        // Notify receiver via SSE
        sseController.sendMessageToReceiver(message.getReceiverEmail(), saved);

        Notification n = new Notification();
        n.setPerson(personRepo.findById(message.getReceiverEmail()).orElseThrow());
        n.setDescription("New Message from " + message.getSenderEmail());
        n.setRead(false);
        n.setCreationDatetime(LocalDateTime.now()); //Vet inte om det behövs riktigt
        notificationRepo.save(n);

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

        DirectMessage saved = directMessageRepository.save(message);

        Notification n = new Notification();
        n.setPerson(personRepo.findById(message.getReceiverEmail()).orElseThrow());
        n.setDescription("New Message from " + message.getSenderEmail());
        n.setRead(false);
        n.setCreationDatetime(LocalDateTime.now()); //Vet inte om det behövs riktigt, men kanske för att sortera notifications!!
        notificationRepo.save(n);
        return saved;
    }

    @GetMapping("/image/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getImage(@PathVariable long id) {  //Skickar tillbaka bildens innehåll som bytes
        DirectMessage message = directMessageRepository.findById(id).orElseThrow();
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(message.getImageData());
    }

    @GetMapping(value = "/{email}")
    @ResponseBody
    public List<DirectMessage> getMessages(@PathVariable String email, Authentication authentication) { //@RequestParam får då users mail. Frontend viktigt att bestämma users mail på rätt sätt! Så att denna metod kan hitta rätt
        String loggedInUserEmail = authentication.getName();

        List<DirectMessage> conversation = directMessageRepository.findConversationBetween(loggedInUserEmail, email);

        if (conversation.isEmpty()) {
            DirectMessage initialMessage = new DirectMessage();
            initialMessage.setSenderEmail(loggedInUserEmail);
            initialMessage.setReceiverEmail(email);
            initialMessage.setContent("Conversation started");
            initialMessage.setCreationDatetime(LocalDateTime.now());
            directMessageRepository.save(initialMessage);
            conversation.add(initialMessage);
        }

        return conversation;
    }
}