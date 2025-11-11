package com.cabapro.development.controller;

import com.cabapro.development.model.Assignment;
import com.cabapro.development.model.AssignmentStatus;
import com.cabapro.development.model.Referee;
import com.cabapro.development.service.AssignmentService;
import com.cabapro.development.service.NotificationService;
import com.cabapro.development.repository.RefereeRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/referees")
@CrossOrigin(origins = "*") // Allow acces from express 
public class RefereeRestController {

    private final RefereeRepository refereeRepo;
    private final AssignmentService assignmentService;
    private final NotificationService notificationService;

    public RefereeRestController(RefereeRepository refereeRepo,
                                 AssignmentService assignmentService,
                                 NotificationService notificationService) {
        this.refereeRepo = refereeRepo;
        this.assignmentService = assignmentService;
        this.notificationService = notificationService;
    }

    // Dashboard data (profile + assignments + notifications)
    @GetMapping("/{id}/dashboard")
    public Map<String, Object> getDashboard(@PathVariable Long id) {
        Referee referee = refereeRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Referee not found: " + id));
        var assignments = assignmentService.findByRefereeId(id);
        var notifications = notificationService.getNotificationsByReferee(id);
        var unread = notificationService.countUnreadNotifications(id);
        return Map.of(
                "referee", referee,
                "assignments", assignments,
                "notifications", notifications,
                "unreadCount", unread
        );
    }

    // View my assignments
    @GetMapping("/{id}/assignments")
    public List<Assignment> getAssignments(@PathVariable Long id) {
        return assignmentService.findByRefereeId(id);
    }

    // Accept assignment
    @PutMapping("/{refId}/assignments/{aId}/accept")
    public Assignment accept(@PathVariable Long refId, @PathVariable Long aId) {
        return assignmentService.updateStatus(aId, AssignmentStatus.ACCEPTED);
    }

    // Reject assignment
    @PutMapping("/{refId}/assignments/{aId}/reject")
    public Assignment reject(@PathVariable Long refId, @PathVariable Long aId) {
        return assignmentService.updateStatus(aId, AssignmentStatus.REJECTED);
    }

    // Update profile (only their own email/phone)
    @PutMapping("/{id}/profile")
    public Referee updateProfile(@PathVariable Long id, @RequestBody Referee data) {
        Referee referee = refereeRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Referee not found: " + id));
        referee.setEmail(data.getEmail());
        referee.setPhoneNumber(data.getPhoneNumber());
        return refereeRepo.save(referee);
    }

    // Notifications
    @GetMapping("/{id}/notifications")
    public Map<String, Object> getNotifications(@PathVariable Long id) {
        var notifications = notificationService.getNotificationsByReferee(id);
        var unreadCount = notificationService.countUnreadNotifications(id);
        return Map.of("notifications", notifications, "unreadCount", unreadCount);
    }

    @PutMapping("/notifications/{notificationId}/read")
    public Map<String, String> markNotificationAsRead(@PathVariable Long notificationId) {
        try {
            notificationService.markAsRead(notificationId);
            return Map.of("status", "success");
        } catch (Exception e) {
            return Map.of("status", "error", "message", e.getMessage());
        }
    }

    @PutMapping("/{id}/notifications/read-all")
    public Map<String, String> markAllAsRead(@PathVariable Long id) {
        try {
            notificationService.markAllAsRead(id);
            return Map.of("status", "success");
        } catch (Exception e) {
            return Map.of("status", "error", "message", e.getMessage());
        }
    }
}