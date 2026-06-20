package maventest.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
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