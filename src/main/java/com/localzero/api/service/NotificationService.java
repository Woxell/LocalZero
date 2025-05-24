package com.localzero.api.service;

import com.localzero.api.entity.Notification;
import com.localzero.api.entity.Person;
import com.localzero.api.repository.NotificationsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationService {

    private final NotificationsRepository notificationsRepository;

    public NotificationService(NotificationsRepository repo) {
        this.notificationsRepository = repo;

    }

    public void notify(Person recipient, String description) {
        Notification n = new Notification();
        n.setPerson(recipient);
        n.setDescription(description);
        n.setRead(false);
        n.setCreationDatetime(LocalDateTime.now());
        notificationsRepository.save(n);
    }
}

