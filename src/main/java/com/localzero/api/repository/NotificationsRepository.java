package com.localzero.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.localzero.api.entity.Notification;
import java.util.List;


public interface NotificationsRepository extends JpaRepository<Notification, Long> {

    List<Notification>findByPersonEmailAndIsReadFalse(String email);
}

