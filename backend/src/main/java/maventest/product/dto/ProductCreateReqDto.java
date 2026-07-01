package maventest.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class ProductCreateReqDto {

    @JsonProperty("PRODUCT_CODE")
    @NotBlank(message = "productCode is required")
    private String productCode;

    @JsonProperty("PRODUCT_NAME")
    @NotBlank(message = "productName is required")
    private String productName;

    @JsonProperty("PRODUCT_TYPE")
    @NotBlank(message = "productType is required")
    private String productType;

    @JsonProperty("BASE_PREMIUM")
    @NotNull(message = "basePremium is required")
    @DecimalMin(value = "0.01", message = "basePremium must be greater than zero")
    private BigDecimal basePremium;

    @JsonProperty("MIN_AMOUNT")
    @NotNull(message = "最低保額不得為空")
    @DecimalMin(value = "1", message = "minAmount must be greater than zero")
    private BigDecimal minAmount;

    @JsonProperty("MAX_AMOUNT")
    @NotNull(message = "最高保額不得為空")
    @DecimalMin(value = "1", message = "maxAmount must be greater than zero")
    private BigDecimal maxAmount;

    @JsonProperty("MIN_AGE")
    @NotNull(message = "投保最低年齡不得為空")
    @Min(value = 1, message = "投保年齡最低為 1 歲")
    @Max(value = 99, message = "投保年齡最高為 99 歲")
    private Integer minAge;

    @JsonProperty("MAX_AGE")
    @NotNull(message = "投保最高年齡不得為空")
    @Min(value = 1, message = "投保年齡最低為 1 歲")
    @Max(value = 99, message = "投保年齡最高為 99 歲")
    private Integer maxAge;

    @JsonProperty("REMARK")
    private String remark;
}