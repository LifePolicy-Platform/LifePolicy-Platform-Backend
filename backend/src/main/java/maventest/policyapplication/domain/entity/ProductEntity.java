package maventest.policyapplication.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {

    private String code;
    private String name;
    private String productType;
    private BigDecimal basePremium;
    private BigDecimal minSumInsured;
    private BigDecimal maxSumInsured;
    private Integer minInsuredAge;
    private Integer maxInsuredAge;
    private String status;
    private String remark;
    private LocalDateTime createTime;
}