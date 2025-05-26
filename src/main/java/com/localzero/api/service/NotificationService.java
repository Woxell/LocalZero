package com.localzero.api.service;

import com.localzero.api.Logger;
import com.localzero.api.entity.Notification;
import com.localzero.api.entity.Person;
import com.localzero.api.repository.NotificationsRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationsRepository notificationsRepository;
    private final Logger logger = Logger.getInstance();

    @Autowired
    public NotificationService(NotificationsRepository notificationsRepository) {
        this.notificationsRepository = notificationsRepository;
    }

    public void notify(Person recipient, String description) {
        Notification n = new Notification();
        n.setPerson(recipient);
        n.setDescription(description);
        n.setRead(false);
        n.setCreationDatetime(LocalDateTime.now());
        notificationsRepository.save(n);
    }

    public List<Notification> getUnreadNotificationsBy(String email){
        return notificationsRepository.findByPersonEmailAndIsReadFalse(email);
    }

    public Notification findById(Long id) {
        return notificationsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(logger.logError("Notification not found with id: " + id)));
    }

    public Notification save(Notification notification) {
        logger.log("Notification sent to " + notification.getPerson().getName() + ": " + notification.getDescription());
        return notificationsRepository.save(notification);
    }
}

