package com.localzero.api.controller;

import com.localzero.api.entity.Notification;
import com.localzero.api.repository.NotificationsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationsRepository notificationsRepository;

    @GetMapping
    public List<Notification> getUnread(@RequestParam String email) {
        return notificationsRepository.findByPersonEmailAndIsReadFalse(email);
    }

    @PutMapping("/{id}/read")
    public void markAsRead(@PathVariable long id) {
        Notification n = notificationsRepository.findById(id).orElseThrow();
        n.setRead(true);
        notificationsRepository.save(n);
    }
}

@Controller
@RequestMapping("/notifications-ui")
@RequiredArgsConstructor
class NotificationPageController {

    private final NotificationsRepository notificationsRepository;

    @GetMapping
    public String showNotifications(Authentication authentication, Model model) {
        String email = authentication.getName();
        List<Notification> notifications = notificationsRepository.findByPersonEmailAndIsReadFalse(email);
        model.addAttribute("notifications", notifications);
        return "notifications";
    }

    @PostMapping("/{id}/read-ui")
    public String markAsReadAndReturn(@PathVariable long id) {
        Notification n = notificationsRepository.findById(id).orElseThrow();
        n.setRead(true);
        notificationsRepository.save(n);
        return "redirect:/notifications-ui";
    }
}
