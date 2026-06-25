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
    @NotBlank(message = "投保人身份證號為必填")
    private String applicantIdNo;

    @JsonProperty("APPLICANT_NAME")
    @NotBlank(message = "投保人姓名為必填")
    private String applicantName;

    @JsonProperty("APPLICANT_GENDER")
    @NotNull(message = "投保人性別為必填")
    private Gender applicantGender;

    @JsonProperty("APPLICANT_BIRTHDATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "投保人生日為必填")
    private LocalDate applicantBirthdate;

    @JsonProperty("RELATIONSHIP_TO_INSURED")
    @NotNull(message = "relationshipToInsured is required")
    private RelationshipToInsured relationshipToInsured;

    @JsonProperty("INSURED_ID_NO")
    @NotBlank(message = "被保人身份證為必填")
    private String insuredIdNo;

    @JsonProperty("INSURED_NAME")
    @NotBlank(message = "被保人姓名為必填")
    private String insuredName;

    @JsonProperty("INSURED_GENDER")
    @NotNull(message = "被保人性別為必填")
    private Gender insuredGender;

    @JsonProperty("INSURED_BIRTHDATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "被保人生日為必填")
    private LocalDate insuredBirthdate;

    @JsonProperty("PRODUCT_CODE")
    @NotBlank(message = "產品代碼為必填")
    private String productCode;

    @JsonProperty("SUM_INSURED")
    @NotNull(message = "保險金額為必填")
    @DecimalMin(value = "1.00", message = "保險金額必須大於零")
    private BigDecimal sumInsured;

    @JsonProperty("ANNUAL_PREMIUM")
    @NotNull(message = "年繳保費為必填")
    @DecimalMin(value = "1.00", message = "年繳保費必須大於零")
    private BigDecimal annualPremium;

    @JsonProperty("CONTACT_PHONE")
    @NotBlank(message = "聯絡電話為必填")
    private String contactPhone;

    @JsonProperty("RISK_LEVEL")
    @NotBlank
    private String riskLevel;

    @JsonProperty("CREATED_BY")
    private String createdBy;

    @JsonProperty("PFILE_01_NAME")
    private String pfile01Name;

    @JsonProperty("PFILE_01_PATH")
    private String pfile01Path;

    @JsonProperty("PFILE_02_NAME")
    private String pfile02Name;

    @JsonProperty("PFILE_02_PATH")
    private String pfile02Path;
}