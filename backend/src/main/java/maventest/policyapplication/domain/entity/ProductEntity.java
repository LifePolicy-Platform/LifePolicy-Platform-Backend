package maventest.policyapplication.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {

    private String code;
    private String name;
    private Integer minInsuredAge;
    private Integer maxInsuredAge;
    private BigDecimal minSumInsured;
    private BigDecimal maxSumInsured;
}
