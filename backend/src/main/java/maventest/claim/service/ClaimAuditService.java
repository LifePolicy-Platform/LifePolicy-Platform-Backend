package maventest.claim.service;
import maventest.claim.dto.AiAnalyzeRequest;
import maventest.claim.entity.ClaimEntity;
import maventest.claim.entity.ClaimAprvLogEntity;
import java.util.List;
import java.util.Map;

public interface ClaimAuditService {
    List<ClaimEntity> getAuditList(String status, String policyNo);
    void makeAuditDecision(Map<String, Object> payload); // 審核直接使用 Map 接收，免除審核 DTO
    List<ClaimAprvLogEntity> getClaimLogs(String claimNo);
    String analyzeClaimWithAi(AiAnalyzeRequest request); // AI 智慧功能
}