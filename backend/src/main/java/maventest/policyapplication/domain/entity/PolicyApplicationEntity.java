package maventest.policyapplication.domain.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import maventest.policyapplication.domain.enums.ApplicationStatus;
import maventest.policyapplication.domain.enums.Gender;
import maventest.policyapplication.domain.enums.RelationshipToInsured;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PolicyApplicationEntity {

    private String applicationId;
    private String applicantIdNo;
    private String applicantName;
    private Gender applicantGender;
    private LocalDate applicantBirthdate;
    private RelationshipToInsured relationshipToInsured;
    private String insuredIdNo;
    private String productCode;
    private BigDecimal sumInsured;
    private BigDecimal annualPremium;
    private ApplicationStatus applicationStatus;
    private LocalDateTime submissionTime;
    private LocalDateTime reviewTime;
    private String reviewedBy;
    private String rejectionReason;
    private String createdBy;
}