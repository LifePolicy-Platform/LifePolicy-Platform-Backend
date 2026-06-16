package maventest.policyapplication.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class InsuranceApplicationCommandRespDto {

    @JsonProperty("APPLICATION_ID")
    private String applicationId;

    @JsonProperty("APPLICATION_STATUS")
    private String applicationStatus;

    @JsonProperty("SUBMISSION_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime submissionTime;

    @JsonProperty("APPLICANT_ID_NO")
    private String applicantIdNo;

    @JsonProperty("INSURED_ID_NO")
    private String insuredIdNo;

    @JsonProperty("PRODUCT_CODE")
    private String productCode;

    @JsonProperty("SUM_INSURED")
    private BigDecimal sumInsured;

    @JsonProperty("ANNUAL_PREMIUM")
    private BigDecimal annualPremium;

    @JsonProperty("PREMIUM_RATIO")
    private BigDecimal premiumRatio;

    @JsonProperty("RISK_LEVEL")
    private String riskLevel;
}