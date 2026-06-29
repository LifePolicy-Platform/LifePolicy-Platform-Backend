package maventest.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maventest.auth.entity.AppUserEntity;
import maventest.notification.entity.NotificationEntity;
import maventest.policyapplication.infrastructure.repository.mapper.CustUserMapper;
import maventest.notification.mapper.NotificationMapper;
import maventest.auth.repository.AppUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationMapper notificationMapper;
    private final AppUserRepository appUserRepository;
    private final CustUserMapper custUserMapper;

    /** 通知所有指定角色的有效使用者（同日同標題同案件不重複發送） */
    public void pushToRoleIfNotSentToday(String roleCode, String notifType, String title,
                                         String content, String refNo, String createUser) {
        List<AppUserEntity> users = appUserRepository.findByRoleCode(roleCode);
        for (AppUserEntity user : users) {
            if (!"ACTIVE".equals(user.getStatus())) {
                continue;
            }
            if (notificationMapper.existsSentToday(user.getUsername(), refNo, title)) {
                continue;
            }
            push(user.getUsername(), null, notifType, title, content, refNo, createUser);
        }
    }

    /** 通知所有指定角色的有效使用者 */
    public void pushToRole(String roleCode, String notifType, String title,
                           String content, String refNo, String createUser) {
        List<AppUserEntity> users = appUserRepository.findByRoleCode(roleCode);
        for (AppUserEntity user : users) {
            if ("ACTIVE".equals(user.getStatus())) {
                push(user.getUsername(), null, notifType, title, content, refNo, createUser);
            }
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

    /**
     * 理賠審核結果推播，獨立交易確保推播失敗不影響主審核交易。
     * pendingUser 由呼叫端預先查好（RETURN/REJECTED 才有值，其餘傳 null）。
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void pushClaimNotification(String claimNo, String action,
                                      String agentUsername, String pendingUser, String reviewer) {
        switch (action) {
            case "PENDING" -> {
                markReadByRefNoAndTitle(claimNo, "理賠新件待審");
                if (agentUsername != null) {
                    pushToUsername(agentUsername, "CLAIM",
                            "理賠案件審核中", "理賠案件 " + claimNo + " 已進入審核程序，請耐心等候。", claimNo, reviewer);
                }
                pushToRole("REVIEWER", "CLAIM",
                        "理賠案件待複審", "業務 " + reviewer + " 已完成初審，理賠案件 " + claimNo + " 待主管複審。", claimNo, reviewer);
            }
            case "RETURN" -> {
                markReadByRefNoAndTitle(claimNo, "理賠案件待複審");
                if (pendingUser != null) {
                    pushToUsername(pendingUser, "CLAIM",
                            "理賠案件退件", "理賠案件 " + claimNo + " 已退件，請修正後重新送件。", claimNo, reviewer);
                }
            }
            case "APPROVED" -> {
                markReadByRefNoAndTitle(claimNo, "理賠案件待複審");
                if (agentUsername != null) {
                    pushToUsername(agentUsername, "CLAIM",
                            "理賠案件核准", "理賠案件 " + claimNo + " 已核准。", claimNo, reviewer);
                }
            }
            case "REJECTED" -> {
                markReadByRefNoAndTitle(claimNo, "理賠案件待複審");
                if (pendingUser != null) {
                    pushToUsername(pendingUser, "CLAIM",
                            "理賠案件駁回", "理賠案件 " + claimNo + " 已駁回，如有疑問請洽主管。", claimNo, reviewer);
                }
            }
            default -> { }
        }
    }

    /** 將指定案件號碼與標題的通知全部標記已讀（審核完成後自動清掉待處理通知） */
    public void markReadByRefNoAndTitle(String refNo, String title) {
        try {
            notificationMapper.markReadByRefNoAndTitle(refNo, title);
        } catch (Exception e) {
            log.error("自動清通知失敗 refNo={} title={}", refNo, title, e);
        }
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