package maventest.claim.service.impl;

import maventest.auth.entity.AppUserEntity;
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

        // 解析推播所需人員，在主交易內查好再交給獨立交易的推播方法
        String agentUsername = appUserRepository.findById(claim.getAgentId())
                .map(AppUserEntity::getUsername)
                .orElse(null);
        String pendingUser = ("RETURN".equals(action) || "REJECTED".equals(action))
                ? claimMapper.findPendingAprvUser(claimNo)
                : null;

        notificationService.pushClaimNotification(claimNo, action, agentUsername, pendingUser, user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClaimAprvLogEntity> getClaimLogs(String claimNo) {
        return claimMapper.selectAprvLogHistory(claimNo);
    }
}