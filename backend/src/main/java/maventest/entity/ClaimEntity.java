package maventest.entity;

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
public class ClaimEntity {
    private String claimNo;
    private Long memberId;
    private String policyNo;
    private BigDecimal claimAmount;
    private BigDecimal approveAmount;
    private String claimStatus;
    private Long agentId;
    private LocalDateTime applyTime;
    private LocalDateTime expiryTime;
    private LocalDateTime settleTime;
    private String remark;
    private String updateUser;
    
    // 💡 補上最新的 PDF 檔案欄位
    private String file01Name;
    private String file01Path;
    private String file02Name;
    private String file02Path;
}