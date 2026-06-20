package maventest.policyapplication.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import maventest.policyapplication.domain.enums.Gender;
import maventest.policyapplication.domain.enums.RelationshipToInsured;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceApplicationCommandReqDto {

    @JsonProperty("APPLICANT_ID_NO")
    @NotBlank(message = "applicantIdNo is required")
    private String applicantIdNo;

    @JsonProperty("APPLICANT_NAME")
    @NotBlank(message = "applicantName is required")
    private String applicantName;

    @JsonProperty("APPLICANT_GENDER")
    @NotNull(message = "applicantGender is required")
    private Gender applicantGender;

    @JsonProperty("APPLICANT_BIRTHDATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "applicantBirthdate is required")
    private LocalDate applicantBirthdate;

    @JsonProperty("RELATIONSHIP_TO_INSURED")
    @NotNull(message = "relationshipToInsured is required")
    private RelationshipToInsured relationshipToInsured;

    @JsonProperty("INSURED_ID_NO")
    @NotBlank(message = "insuredIdNo is required")
    private String insuredIdNo;

    @JsonProperty("INSURED_NAME")
    @NotBlank(message = "insuredName is required")
    private String insuredName;

    @JsonProperty("INSURED_GENDER")
    @NotNull(message = "insuredGender is required")
    private Gender insuredGender;

    @JsonProperty("INSURED_BIRTHDATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "insuredBirthdate is required")
    private LocalDate insuredBirthdate;

    @JsonProperty("PRODUCT_CODE")
    @NotBlank(message = "productCode is required")
    private String productCode;

    @JsonProperty("SUM_INSURED")
    @NotNull(message = "sumInsured is required")
    @DecimalMin(value = "1.00", message = "sumInsured must be greater than zero")
    private BigDecimal sumInsured;

    @JsonProperty("ANNUAL_PREMIUM")
    @NotNull(message = "annualPremium is required")
    @DecimalMin(value = "1.00", message = "annualPremium must be greater than zero")
    private BigDecimal annualPremium;

    @JsonProperty("CONTACT_PHONE")
    @NotBlank(message = "contactPhone is required")
    private String contactPhone;

    @JsonProperty("RISK_LEVEL")
    @NotBlank(message = "riskLevel is required")
    private String riskLevel;

    @JsonProperty("CREATED_BY")
    private String createdBy;
}