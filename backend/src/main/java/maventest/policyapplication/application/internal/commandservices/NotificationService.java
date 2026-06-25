package maventest.policyapplication.application.internal.commandservices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maventest.auth.entity.AppUserEntity;
import maventest.policyapplication.domain.entity.NotificationEntity;
import maventest.policyapplication.infrastructure.repository.mapper.CustUserMapper;
import maventest.policyapplication.infrastructure.repository.mapper.NotificationMapper;
import maventest.auth.repository.AppUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationMapper notificationMapper;
    private final AppUserRepository appUserRepository;
    private final CustUserMapper custUserMapper;

    /** 通知所有指定角色的有效使用者（整批 all-or-nothing，與呼叫端交易隔離） */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void pushToRole(String roleCode, String notifType, String title,
                           String content, String refNo, String createUser) {
        try {
            List<AppUserEntity> users = appUserRepository.findByRoleCode(roleCode);
            for (AppUserEntity user : users) {
                if (!"ACTIVE".equals(user.getStatus())) continue;
                String username = user.getUsername();
                if (username == null || username.isBlank()) continue;
                NotificationEntity entity = NotificationEntity.builder()
                        .notifType(notifType)
                        .title(title)
                        .content(content)
                        .targetType("ROLE")
                        .memberId(null)
                        .refNo(refNo)
                        .recipientUsername(username)
                        .createUser(createUser != null ? createUser : "SYSTEM")
                        .build();
                notificationMapper.insert(entity);
            }
        } catch (Exception e) {
            log.error("角色推播整批失敗 roleCode={} title={}", roleCode, title, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }

    /** 通知特定使用者（by username，業務/主管） */
    public void pushToUsername(String username, String notifType, String title,
                               String content, String refNo, String createUser) {
        if (username == null || username.isBlank()) return;
        push(username, null, notifType, title, content, refNo, createUser);
    }

    /** 通知保戶（by memberId，同時記 MEMBER_ID 和 RECIPIENT_USERNAME） */
    public void pushToMember(Long memberId, String notifType, String title,
                             String content, String refNo, String createUser) {
        if (memberId == null) return;
        String username = custUserMapper.findUsernameByMemberId(memberId);
        if (username == null) {
            log.warn("找不到 memberId={} 的 username，通知略過", memberId);
            return;
        }
        push(username, memberId, notifType, title, content, refNo, createUser);
    }

    /** 查詢未讀數量 */
    public int countUnread(String username) {
        return notificationMapper.countUnreadByUsername(username);
    }

    /** 查詢通知列表（含已讀） */
    public List<NotificationEntity> getNotifications(String username, int page, int size) {
        int offset = page * size;
        return notificationMapper.findUnreadByUsername(username, offset, size);
    }

    /** 標記單筆已讀 */
    public void markAsRead(Long notifNo, String username) {
        notificationMapper.markAsRead(notifNo, username);
    }

    /** 全部標記已讀 */
    public void markAllAsRead(String username) {
        notificationMapper.markAllAsReadByUsername(username);
    }

    private void push(String recipientUsername, Long memberId,
                      String notifType, String title, String content,
                      String refNo, String createUser) {
        try {
            NotificationEntity entity = NotificationEntity.builder()
                    .notifType(notifType)
                    .title(title)
                    .content(content)
                    .targetType("SPECIFIC")
                    .memberId(memberId)
                    .refNo(refNo)
                    .recipientUsername(recipientUsername)
                    .createUser(createUser != null ? createUser : "SYSTEM")
                    .build();
            notificationMapper.insert(entity);
        } catch (Exception e) {
            log.error("推播通知失敗 recipient={} title={}", recipientUsername, title, e);
        }
    }
}