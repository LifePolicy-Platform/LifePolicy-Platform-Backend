package maventest.claim.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClaimAprvLogEntity {
    private Long claimLogNo;
    private String claimNo;
    private String policyNo;
    private String claimStatus;
    private BigDecimal approveAmount;
    private String aprvRemark;
    private LocalDateTime createTime;
    private LocalDateTime aprvTime;
    private String aprvUser;
}