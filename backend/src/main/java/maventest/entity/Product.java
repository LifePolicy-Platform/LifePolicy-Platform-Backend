package maventest.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 商品檔（tb_product）
 */
@Data
@TableName("tb_product")
public class Product {

    /** 商品代碼（PK） */
    @TableId(value = "PRODUCT_CODE", type = IdType.INPUT)
    private String productCode;

    /** 商品名稱 */
    private String productName;

    /** 商品類型（LIFE/HEALTH/ACCIDENT） */
    private String productType;

    /** 基本保費 */
    private BigDecimal basePremium;

    /** 最低保額 */
    private BigDecimal minAmount;

    /** 最高保額 */
    private BigDecimal maxAmount;

    /** 投保年齡下限 */
    private Integer minAge;

    /** 投保年齡上限 */
    private Integer maxAge;

    /** 狀態（ACTIVE/INACTIVE） */
    private String status;

    /** 備註 */
    private String remark;

    /** 建立時間 */
    private LocalDateTime createTime;

    /** 更新時間 */
    private LocalDateTime updateTime;

    /** 更新人員 */
    private String updateUser;
}
