package maventest.product.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("tb_product")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {

    @TableId(value = "PRODUCT_CODE", type = IdType.INPUT)
    private String productCode;

    @TableField("PRODUCT_NAME")
    private String productName;

    @TableField("PRODUCT_TYPE")
    private String productType;

    @TableField("BASE_PREMIUM")
    private BigDecimal basePremium;

    @TableField("MIN_AMOUNT")
    private BigDecimal minAmount;

    @TableField("MAX_AMOUNT")
    private BigDecimal maxAmount;

    @TableField("MIN_AGE")
    private Integer minAge;

    @TableField("MAX_AGE")
    private Integer maxAge;

    @TableField("STATUS")
    private String status;

    @TableField("REMARK")
    private String remark;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    @TableField("UPDATE_USER")
    private String updateUser;

    @TableField("PRODUCT_TERM")
    private Integer productTerm;
}