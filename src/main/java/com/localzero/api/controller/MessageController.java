package com.localzero.api.controller;


import com.localzero.api.entity.DirectMessage;
import com.localzero.api.entity.Notification;
import com.localzero.api.repository.DirectMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.localzero.api.repository.NotificationsRepository;
import com.localzero.api.repository.PersonRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping ("/messages")
public class MessageController {

    @Autowired
    private DirectMessageRepository directMessageRepository;  //repository oobject som Spriing boot skapar varje gång vi kallar directMessageRepository

    @Autowired
    private NotificationsRepository notificationRepo;
    @Autowired
    private PersonRepository personRepo;

    @PostMapping
    public DirectMessage sendMessage(@RequestBody DirectMessage message) { //Request, ggör JASON till ett obeject som kan sparas

        message.setCreationDatetime(LocalDateTime.now());
        DirectMessage saved = directMessageRepository.save(message);

        Notification n = new Notification();
        n.setPerson(personRepo.findById(message.getReceiverEmail()).orElseThrow());
        n.setDescription("New Message from " + message.getSenderEmail());
        n.setRead(false);
        n.setCreationDatetime(LocalDateTime.now()); //Vet inte om det behövs riktigt
        notificationRepo.save(n);
        return saved;
    }

    @GetMapping
    public List<DirectMessage> getMessages(@RequestParam String user1, @RequestParam String user2) { //@Requestparam ffår då users mmaill,  FFrontend vitigt att bestämma users mail på rätt sätt! så att denna metod kan hitta rätt

       return directMessageRepository.findConversationBetween(user1, user2);


    }

    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public DirectMessage sendImageMessage(@RequestParam ("senderEmail") String senderEmail,
                                          @RequestParam ("receiverEmail") String receiverEmail,
                                          @RequestParam ("file") MultipartFile file) throws IOException {
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

    @GetMapping ("/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable long id) {  //Skickar tillbaka bildens innehåll som byte
        DirectMessage message = directMessageRepository.findById(id).orElseThrow();
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(message.getImageData());

    }

}
