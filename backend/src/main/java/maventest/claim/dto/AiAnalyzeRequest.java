package maventest.claim.dto;

import lombok.Data;

@Data
public class AiAnalyzeRequest {
    private String claimNo;
    private String auditRuleMsg;
}