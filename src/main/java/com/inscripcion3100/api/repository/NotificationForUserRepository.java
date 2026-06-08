package com.inscripcion3100.api.repository;

import com.inscripcion3100.api.entity.NotificationForUser;
import com.inscripcion3100.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationForUserRepository extends JpaRepository<NotificationForUser, Long> {
    Long countByReceiverAndIsReadFalse(User receiver);
    List<NotificationForUser> findByReceiverAndIsReadFalse(User receiver);
    List<NotificationForUser> findByReceiver (User user);
    
}
