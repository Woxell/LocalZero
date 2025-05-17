package com.localzero.api.controller;


import com.localzero.api.entity.DirectMessage;
import com.localzero.api.repository.DirectMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping ("/messages")
public class MessageController {

    @Autowired
    private DirectMessageRepository directMessageRepository;  //repository oobject som Spriing boot skapar varje gång vi kallar directMessageRepository


    @PostMapping
    public DirectMessage sendMessage(@RequestBody DirectMessage message) { //Request, ggör JASON till ett obeject som kan sparas

        message.setCreationDatetime(LocalDateTime.now());
        return directMessageRepository.save(message);
    }

    @GetMapping
    public List<DirectMessage> getMessages(@RequestParam String user1, @RequestParam String user2) { //@Requestparam ffår då users mmaill,  FFrontend vitigt att bestämma users mail på rätt sätt! så att denna metod kan hitta rätt

       return directMessageRepository.findConversationBetween(user1, user2);


    }
}
