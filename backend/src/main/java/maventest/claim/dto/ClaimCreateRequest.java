package maventest.claim.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ClaimCreateRequest {
    private Long memberId;
    private String policyNo;
    private BigDecimal claimAmount;
    private Long agentId;
    private String remark;
    private String file01Name;
    private String file01Path;
    private String file02Name;
    private String file02Path;
}
