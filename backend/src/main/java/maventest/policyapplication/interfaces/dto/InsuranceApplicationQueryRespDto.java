package maventest.policyapplication.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceApplicationQueryRespDto {

    @JsonProperty("APPLICATION_ID")
    private String applicationId;

    @JsonProperty("APPLICANT_ID_NO")
    private String applicantIdNo;

    @JsonProperty("APPLICANT_NAME")
    private String applicantName;

    @JsonProperty("APPLICANT_GENDER")
    private String applicantGender;

    @JsonProperty("APPLICANT_BIRTHDATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate applicantBirthdate;

    @JsonProperty("INSURED_ID_NO")
    private String insuredIdNo;

    @JsonProperty("INSURED_NAME")
    private String insuredName;

    @JsonProperty("INSURED_GENDER")
    private String insuredGender;

    @JsonProperty("INSURED_BIRTHDATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate insuredBirthdate;

    @JsonProperty("CONTACT_PHONE")
    private String contactPhone;

    @JsonProperty("PRODUCT_CODE")
    private String productCode;

    @JsonProperty("PRODUCT_NAME")
    private String productName;

    @JsonProperty("APPLICATION_STATUS")
    private String applicationStatus;

    @JsonProperty("RELATIONSHIP_TO_INSURED")
    private String relationshipToInsured;

    @JsonProperty("SUM_INSURED")
    private BigDecimal sumInsured;

    @JsonProperty("ANNUAL_PREMIUM")
    private BigDecimal annualPremium;

    @JsonProperty("SUBMISSION_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime submissionTime;

    @JsonProperty("REVIEW_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime reviewTime;

    @JsonProperty("REVIEWED_BY")
    private String reviewedBy;

    @JsonProperty("REJECTION_REASON")
    private String rejectionReason;

    @JsonProperty("CREATED_BY")
    private String createdBy;

    @JsonProperty("PREMIUM_RATIO")
    private BigDecimal premiumRatio;

    @JsonProperty("RISK_LEVEL")
    private String riskLevel;

    @JsonProperty("PFILE_01_NAME")
    private String pfile01Name;

    @JsonProperty("PFILE_01_PATH")
    private String pfile01Path;

    @JsonProperty("PFILE_02_NAME")
    private String pfile02Name;

    @JsonProperty("PFILE_02_PATH")
    private String pfile02Path;
}