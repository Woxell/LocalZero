package com.localzero.api.controller;

import com.localzero.api.entity.Notification;
import com.localzero.api.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    @ResponseBody
    public List<Notification> getUnread(@RequestParam String email) {
        return notificationService.getUnreadNotificationsBy(email);
    }

    @PutMapping("/{id}/read")
    public void markAsRead(@PathVariable long id) {
        Notification n = notificationService.findById(id);
        n.setRead(true);
        notificationService.save(n);
    }

    @GetMapping("/ui")
    public String showNotifications(Authentication authentication, Model model) {
        String email = authentication.getName();
        List<Notification> notifications = getUnread(email);
        model.addAttribute("notifications", notifications);
        return "notifications";
    }

    @PostMapping("/{id}/read-ui")
    public String markAsReadAndReturn(@PathVariable long id) {
        Notification n = notificationService.findById(id);
        n.setRead(true);
        notificationService.save(n);
        return "redirect:/notifications/ui";
    }
}

