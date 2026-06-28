package maventest.claim.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
    
    // 🌟 這些也屬於聯結查出欄位，但因為不是對應主表實體，安全起見建議統一由 MyBatis 映射
    private String birthday;
    private String gender;
    private String riskLevel;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;

    // 🌟 聯結查詢的非主表欄位，必須 100% 標註 exist = false，防範 MyBatis-Plus 新增修改時崩潰
    @TableField(exist = false)
    private String agentName;    // 經辦人姓名 (對應 DISPLAY_NAME)
    
    @TableField(exist = false)
    private String memberName;   // 客戶姓名 (對應新欄位 NAME)
    
    @TableField(exist = false)
    private String productCode;  // 商品代碼
    
    @TableField(exist = false)
    private String productName;  // 商品名稱
    
    @TableField(exist = false)
    private String productType;  // 來自 tb_product (用於判斷是否為 HEALTH)
    
    @TableField(exist = false)
    private LocalDateTime effectDate; // 🌟 統一改為 LocalDateTime，來自 tb_policy
    
    @TableField(exist = false)
    private LocalDateTime expireDate; // 🌟 統一改為 LocalDateTime，來自 tb_policy
}