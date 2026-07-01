package maventest.claim.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ClaimUpdateRequest {
    private BigDecimal claimAmount;
    private String claimStatus; // 僅允許 'SUBMIT'（用於退回後補件重新送審）
    private String remark;
    private String file01Name;
    private String file01Path;
    private String file02Name;
    private String file02Path;
}
