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
public class ProductUpdateReqDto {

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
    @NotNull(message = "minAmount is required")
    @DecimalMin(value = "1", message = "minAmount must be greater than zero")
    private BigDecimal minAmount;

    @JsonProperty("MAX_AMOUNT")
    @NotNull(message = "maxAmount is required")
    @DecimalMin(value = "1", message = "maxAmount must be greater than zero")
    private BigDecimal maxAmount;

    @JsonProperty("MIN_AGE")
    @NotNull(message = "minAge is required")
    @Min(value = 0, message = "minAge must be at least 0")
    @Max(value = 99, message = "minAge must be at most 99")
    private Integer minAge;

    @JsonProperty("MAX_AGE")
    @NotNull(message = "maxAge is required")
    @Min(value = 0, message = "maxAge must be at least 0")
    @Max(value = 99, message = "maxAge must be at most 99")
    private Integer maxAge;

    @JsonProperty("STATUS")
    @NotBlank(message = "status is required")
    private String status;

    @JsonProperty("REMARK")
    private String remark;
}