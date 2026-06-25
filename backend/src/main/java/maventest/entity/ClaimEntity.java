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
    
    
    private String file01Name;
    private String file01Path;
    private String file02Name;
    private String file02Path;
    
    private String birthday;
    private String gender;
    private String riskLevel;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;

    // 請確保 ClaimEntity 內有這四個欄位
    @com.baomidou.mybatisplus.annotation.TableField(exist = false)
    private String agentName;    // 經辦人姓名 (對應 DISPLAY_NAME)
    @com.baomidou.mybatisplus.annotation.TableField(exist = false)
    private String memberName;   // 客戶姓名 (對應新欄位 NAME)
    @com.baomidou.mybatisplus.annotation.TableField(exist = false)
    private String productCode;  // 商品代碼
    @com.baomidou.mybatisplus.annotation.TableField(exist = false)
    private String productName;  // 商品名稱

    
    private String productType;   // 來自 tb_product (用於判斷是否為 HEALTH)
    private java.util.Date effectDate;   // 來自 tb_policy
    private java.util.Date expireDate;   // 來自 tb_policy
}