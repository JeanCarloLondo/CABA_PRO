package com.cabapro.development.repository;

import com.cabapro.development.model.Notification;
import com.cabapro.development.model.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByReferee_IdOrderByCreatedAtDesc(Long refereeId);

    List<Notification> findByReferee_IdAndIsReadFalseOrderByCreatedAtDesc(Long refereeId);

    long countByReferee_IdAndIsReadFalse(Long refereeId);

    List<Notification> findByReferee_IdAndTypeOrderByCreatedAtDesc(Long refereeId, NotificationType type);

    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.referee.id = :refereeId AND n.isRead = false")
    void markAllAsReadByRefereeId(@Param("refereeId") Long refereeId);

    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.id = :notificationId")
    void markAsRead(@Param("notificationId") Long notificationId);


    @Modifying
    @Query("DELETE FROM Notification n WHERE n.createdAt < :cutoffDate")
    void deleteOldNotifications(@Param("cutoffDate") LocalDateTime cutoffDate);

    List<Notification> findByReferee_IdAndCreatedAtAfterOrderByCreatedAtDesc(
            Long refereeId, LocalDateTime since);
}