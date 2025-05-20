package com.localzero.api.controller;

import com.localzero.api.entity.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.localzero.api.repository.NotificationsRepository;
import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationsRepository notificationsRepository;

    @GetMapping
    public List<Notification> getUnread (@RequestParam String email) {

        return notificationsRepository.findByPersonEmailAndIsReadFalse(email);


    }

    @PutMapping ("/{id}/read")
    public void markAsRead(@PathVariable long id) {
        Notification n = notificationsRepository.findById(id).orElseThrow();
        n.setRead(true);
        notificationsRepository.save(n);
    }

}
