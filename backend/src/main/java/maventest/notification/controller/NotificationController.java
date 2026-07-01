package maventest.notification.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import maventest.common.ReturnMsg;
import maventest.auth.entity.AppUserPrincipal;
<<<<<<< HEAD:backend/src/main/java/maventest/policyapplication/interfaces/rest/NotificationController.java
import maventest.policyapplication.application.internal.commandservices.NotificationService;
import maventest.policyapplication.domain.entity.NotificationEntity;
=======
import maventest.notification.service.NotificationService;
import maventest.notification.entity.NotificationEntity;
>>>>>>> develop:backend/src/main/java/maventest/notification/controller/NotificationController.java
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "通知管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/unread-count")
    @Operation(summary = "取得未讀通知數量", security = {@SecurityRequirement(name = "bearerAuth")})
    public ResponseEntity<ReturnMsg<Map<String, Integer>>> getUnreadCount(
            @AuthenticationPrincipal AppUserPrincipal principal) {
        int count = notificationService.countUnread(principal.getUsername());
        return ResponseEntity.ok(ReturnMsg.success(Map.of("count", count)));
    }

    @GetMapping
    @Operation(summary = "取得通知列表", security = {@SecurityRequirement(name = "bearerAuth")})
    public ResponseEntity<ReturnMsg<List<NotificationEntity>>> getNotifications(
            @AuthenticationPrincipal AppUserPrincipal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<NotificationEntity> list = notificationService.getNotifications(
                principal.getUsername(), page, size);
        return ResponseEntity.ok(ReturnMsg.success(list));
    }

    @PutMapping("/{notifNo}/read")
    @Operation(summary = "標記單筆已讀", security = {@SecurityRequirement(name = "bearerAuth")})
    public ResponseEntity<ReturnMsg<Void>> markAsRead(
            @PathVariable Long notifNo,
            @AuthenticationPrincipal AppUserPrincipal principal) {
        notificationService.markAsRead(notifNo, principal.getUsername());
        return ResponseEntity.ok(ReturnMsg.success(null));
    }

    @PutMapping("/read-all")
    @Operation(summary = "全部標記已讀", security = {@SecurityRequirement(name = "bearerAuth")})
    public ResponseEntity<ReturnMsg<Void>> markAllAsRead(
            @AuthenticationPrincipal AppUserPrincipal principal) {
        notificationService.markAllAsRead(principal.getUsername());
        return ResponseEntity.ok(ReturnMsg.success(null));
    }
}