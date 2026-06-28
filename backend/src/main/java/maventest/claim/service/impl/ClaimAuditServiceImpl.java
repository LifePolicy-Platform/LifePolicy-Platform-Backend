package maventest.claim.service.impl;

import maventest.claim.entity.ClaimEntity;
import maventest.claim.entity.ClaimAprvLogEntity;
import maventest.claim.mapper.ClaimMapper;
import maventest.claim.service.ClaimAuditService;
import maventest.notification.service.NotificationService;
import maventest.auth.repository.AppUserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ClaimAuditServiceImpl implements ClaimAuditService {

    private final ClaimMapper claimMapper;
    private final NotificationService notificationService;
    private final AppUserRepository appUserRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ClaimEntity> getAuditList(String status, String policyNo) {
        return claimMapper.selectClaimAuditList(status, policyNo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void makeAuditDecision(Map<String, Object> payload) {
        String claimNo = (String) payload.get("claimNo");
        String action = (String) payload.get("action");
        String remark = (String) payload.get("remark");
        String user = payload.get("aprvUser") != null ? (String) payload.get("aprvUser") : "SYSTEM_AUDITOR";

        BigDecimal aprvAmount = null;
        if (payload.get("approveAmount") != null) {
            aprvAmount = new BigDecimal(payload.get("approveAmount").toString());
        }

        ClaimEntity claim = claimMapper.selectByClaimNo(claimNo);
        if (claim == null) {
            throw new IllegalArgumentException("找不到該理賠案件");
        }

        claim.setClaimStatus(action); 
        claim.setApproveAmount(aprvAmount);
        claim.setRemark(remark);
        claim.setUpdateUser(user);
        claimMapper.updateClaim(claim);

        // 同步寫入歷程 Log 表
        ClaimAprvLogEntity log = new ClaimAprvLogEntity();
        log.setClaimNo(claimNo);
        log.setPolicyNo(claim.getPolicyNo());
        log.setClaimStatus(action);
        log.setApproveAmount(aprvAmount);
        log.setAprvRemark(remark);
        log.setAprvUser(user);
        claimMapper.insertAprvLog(log);

        // 異步推播通知
        pushClaimNotification(claim, claimNo, action, user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClaimAprvLogEntity> getClaimLogs(String claimNo) {
        return claimMapper.selectAprvLogHistory(claimNo);
    }

    private void pushClaimNotification(ClaimEntity claim, String claimNo, String action, String reviewer) {
        String agentUsername = appUserRepository.findById(claim.getAgentId())
                .map(u -> u.getUsername())
                .orElse(null);

        if (agentUsername == null) return;

        switch (action) {
            case "PENDING" -> notificationService.pushToUsername(agentUsername, "CLAIM",
                    "理賠案件審核中", "理賠案件 " + claimNo + " 已進入審核程序，請耐心等候。", claimNo, reviewer);
            case "RETURN" -> notificationService.pushToUsername(agentUsername, "CLAIM",
                    "理賠案件退件", "理賠案件 " + claimNo + " 已退件，請修正後重新送件。", claimNo, reviewer);
            case "APPROVED" -> notificationService.pushToUsername(agentUsername, "CLAIM",
                    "理賠案件核准", "理賠案件 " + claimNo + " 已核准。", claimNo, reviewer);
            case "REJECTED" -> notificationService.pushToUsername(agentUsername, "CLAIM",
                    "理賠案件駁回", "理賠案件 " + claimNo + " 已駁回，如有疑問請洽主管。", claimNo, reviewer);
            default -> { /* 其他狀態不推播 */ }
        }
    }
}