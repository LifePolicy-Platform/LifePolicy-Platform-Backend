package maventest.policyapplication.interfaces.transform;

import org.springframework.stereotype.Component;

import maventest.policyapplication.domain.entity.InsuredPersonEntity;
import maventest.policyapplication.domain.entity.PolicyApplicationEntity;
import maventest.policyapplication.domain.enums.ApplicationStatus;
import maventest.policyapplication.domain.enums.RelationshipToInsured;
import maventest.policyapplication.domain.enums.UnderwritingRiskLevel;
import maventest.policyapplication.interfaces.dto.InsuranceApplicationCommandReqDto;
import maventest.policyapplication.interfaces.dto.InsuranceApplicationCommandRespDto;
import maventest.policyapplication.interfaces.dto.InsuranceApplicationQueryRespDto;
import maventest.policyapplication.interfaces.dto.InsuranceApplicationReviewCommandReqDto;
import maventest.policyapplication.interfaces.dto.InsuranceApplicationReviewCommandRespDto;
import maventest.policyapplication.interfaces.dto.InsuranceApplicationUpdateReqDto;
import maventest.policyapplication.interfaces.dto.InsuranceApplicationUpdateRespDto;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
public class InsuranceApplicationConverter {

    public PolicyApplicationEntity toPolicyApplicationEntity(
            InsuranceApplicationCommandReqDto reqDto,
            String applicationId,
            LocalDateTime submissionTime,
            String createdBy
    ) {
        return PolicyApplicationEntity.builder()
                .applicationId(applicationId)
                .applicantIdNo(reqDto.getApplicantIdNo())
                .applicantName(reqDto.getApplicantName())
                .applicantGender(reqDto.getApplicantGender())
                .applicantBirthdate(reqDto.getApplicantBirthdate())
                .relationshipToInsured(reqDto.getRelationshipToInsured())
                .insuredIdNo(reqDto.getInsuredIdNo())
                .productCode(reqDto.getProductCode())
                .sumInsured(reqDto.getSumInsured())
                .annualPremium(reqDto.getAnnualPremium())
                .applicationStatus(ApplicationStatus.PENDING)
                .submissionTime(submissionTime)
                .createdBy(createdBy)
                .build();
    }

        public PolicyApplicationEntity toPolicyApplicationEntity(
                        InsuranceApplicationCommandReqDto reqDto,
                        String applicationId,
                        LocalDateTime submissionTime
        ) {
                return toPolicyApplicationEntity(reqDto, applicationId, submissionTime, reqDto.getCreatedBy());
        }

    public InsuredPersonEntity toInsuredPersonEntity(
            InsuranceApplicationCommandReqDto reqDto,
            String applicationId,
            String insuredId
    ) {
        return InsuredPersonEntity.builder()
                .insuredId(insuredId)
                .applicationId(applicationId)
                .insuredIdNo(reqDto.getInsuredIdNo())
                .insuredName(reqDto.getInsuredName())
                .insuredGender(reqDto.getInsuredGender())
                .insuredBirthdate(reqDto.getInsuredBirthdate())
                .contactPhone(reqDto.getContactPhone())
                .build();
    }

    public InsuranceApplicationCommandRespDto toCommandRespDto(
            PolicyApplicationEntity policyApplicationEntity,
            LocalDate insuredBirthdate
    ) {
        BigDecimal premiumRatio = calculatePremiumRatio(
                policyApplicationEntity.getAnnualPremium(),
                policyApplicationEntity.getSumInsured()
        );
        UnderwritingRiskLevel riskLevel = evaluateRiskLevel(
                insuredBirthdate,
                policyApplicationEntity.getRelationshipToInsured(),
                policyApplicationEntity.getSumInsured(),
                policyApplicationEntity.getAnnualPremium()
        );

        return InsuranceApplicationCommandRespDto.builder()
                .applicationId(policyApplicationEntity.getApplicationId())
                .applicationStatus(policyApplicationEntity.getApplicationStatus().name())
                .submissionTime(policyApplicationEntity.getSubmissionTime())
                .applicantIdNo(policyApplicationEntity.getApplicantIdNo())
                .insuredIdNo(policyApplicationEntity.getInsuredIdNo())
                .productCode(policyApplicationEntity.getProductCode())
                .sumInsured(policyApplicationEntity.getSumInsured())
                .annualPremium(policyApplicationEntity.getAnnualPremium())
                .premiumRatio(premiumRatio)
                .riskLevel(riskLevel.name())
                .build();
    }

        public InsuranceApplicationCommandRespDto toCommandRespDto(PolicyApplicationEntity policyApplicationEntity) {
                return toCommandRespDto(policyApplicationEntity, null);
        }

    public PolicyApplicationEntity toUpdatedPolicyApplicationEntity(
            PolicyApplicationEntity existingApplication,
            InsuranceApplicationUpdateReqDto reqDto
    ) {
        return PolicyApplicationEntity.builder()
                .applicationId(existingApplication.getApplicationId())
                .applicantIdNo(reqDto.getApplicantIdNo())
                .applicantName(reqDto.getApplicantName())
                .applicantGender(reqDto.getApplicantGender())
                .applicantBirthdate(reqDto.getApplicantBirthdate())
                .relationshipToInsured(reqDto.getRelationshipToInsured())
                .insuredIdNo(reqDto.getInsuredIdNo())
                .productCode(reqDto.getProductCode())
                .sumInsured(reqDto.getSumInsured())
                .annualPremium(reqDto.getAnnualPremium())
                .applicationStatus(existingApplication.getApplicationStatus())
                .submissionTime(existingApplication.getSubmissionTime())
                .reviewTime(existingApplication.getReviewTime())
                .reviewedBy(existingApplication.getReviewedBy())
                .rejectionReason(existingApplication.getRejectionReason())
                .createdBy(existingApplication.getCreatedBy())
                .build();
    }

    public InsuredPersonEntity toUpdatedInsuredPersonEntity(
            InsuredPersonEntity existingInsuredPerson,
            InsuranceApplicationUpdateReqDto reqDto
    ) {
        return InsuredPersonEntity.builder()
                .insuredId(existingInsuredPerson.getInsuredId())
                .applicationId(existingInsuredPerson.getApplicationId())
                .insuredIdNo(reqDto.getInsuredIdNo())
                .insuredName(reqDto.getInsuredName())
                .insuredGender(reqDto.getInsuredGender())
                .insuredBirthdate(reqDto.getInsuredBirthdate())
                .contactPhone(reqDto.getContactPhone())
                .build();
    }

    public InsuranceApplicationUpdateRespDto toUpdateRespDto(
            PolicyApplicationEntity updatedApplication,
            InsuranceApplicationUpdateReqDto reqDto,
            LocalDateTime updatedTime
    ) {
        return InsuranceApplicationUpdateRespDto.builder()
                .applicationId(updatedApplication.getApplicationId())
                .applicationStatus(updatedApplication.getApplicationStatus().name())
                .updatedTime(updatedTime)
                .productCode(updatedApplication.getProductCode())
                .sumInsured(updatedApplication.getSumInsured())
                .annualPremium(updatedApplication.getAnnualPremium())
                .premiumRatio(calculatePremiumRatio(updatedApplication.getAnnualPremium(), updatedApplication.getSumInsured()))
                .riskLevel(evaluateRiskLevel(
                        reqDto.getInsuredBirthdate(),
                        reqDto.getRelationshipToInsured(),
                        reqDto.getSumInsured(),
                        reqDto.getAnnualPremium()
                ).name())
                .build();
    }

    public PolicyApplicationEntity toReviewPolicyApplicationEntity(
            PolicyApplicationEntity existingApplication,
            InsuranceApplicationReviewCommandReqDto reqDto,
            LocalDateTime reviewTime,
            String reviewedBy
    ) {
        return PolicyApplicationEntity.builder()
                .applicationId(existingApplication.getApplicationId())
                .applicationStatus(reqDto.getTargetStatus())
                .reviewTime(reviewTime)
                .reviewedBy(reviewedBy)
                .rejectionReason(reqDto.getTargetStatus() == ApplicationStatus.REJECTED ? reqDto.getRejectionReason() : null)
                .build();
    }

        public PolicyApplicationEntity toReviewPolicyApplicationEntity(
                        PolicyApplicationEntity existingApplication,
                        InsuranceApplicationReviewCommandReqDto reqDto,
                        LocalDateTime reviewTime
        ) {
                return toReviewPolicyApplicationEntity(existingApplication, reqDto, reviewTime, reqDto.getReviewedBy());
        }

    public InsuranceApplicationReviewCommandRespDto toReviewCommandRespDto(
            PolicyApplicationEntity before,
            PolicyApplicationEntity after,
            Boolean documentsConfirmed
    ) {
        return InsuranceApplicationReviewCommandRespDto.builder()
                .applicationId(after.getApplicationId())
                .previousStatus(before.getApplicationStatus().name())
                .currentStatus(after.getApplicationStatus().name())
                .reviewedBy(after.getReviewedBy())
                .rejectionReason(after.getRejectionReason())
                .reviewTime(after.getReviewTime())
                .documentsConfirmed(documentsConfirmed)
                .build();
    }

        public List<InsuranceApplicationQueryRespDto> toQueryRespDtos(List<Map<String, Object>> result) {
                return result.stream()
                                .map(this::toQueryRespDto)
                                .toList();
        }

        public InsuranceApplicationQueryRespDto toQueryRespDto(Map<String, Object> row) {
                return InsuranceApplicationQueryRespDto.builder()
                                .applicationId(asString(row.get("applicationId")))
                                .applicantIdNo(asString(row.get("applicantIdNo")))
                                .applicantName(asString(row.get("applicantName")))
                                .applicantGender(asString(row.get("applicantGender")))
                                .applicantBirthdate(toLocalDate(row.get("applicantBirthdate")))
                                .insuredIdNo(asString(row.get("insuredIdNo")))
                                .insuredName(asString(row.get("insuredName")))
                                .insuredGender(asString(row.get("insuredGender")))
                                .insuredBirthdate(toLocalDate(row.get("insuredBirthdate")))
                                .contactPhone(asString(row.get("contactPhone")))
                                .productCode(asString(row.get("productCode")))
                                .productName(asString(row.get("productName")))
                                .applicationStatus(asString(row.get("applicationStatus")))
                                .relationshipToInsured(asString(row.get("relationshipToInsured")))
                                .sumInsured((java.math.BigDecimal) row.get("sumInsured"))
                                .annualPremium((java.math.BigDecimal) row.get("annualPremium"))
                                .submissionTime(toLocalDateTime(row.get("submissionTime")))
                                .reviewTime(toLocalDateTime(row.get("reviewTime")))
                                .reviewedBy(asString(row.get("reviewedBy")))
                                .rejectionReason(asString(row.get("rejectionReason")))
                                .createdBy(asString(row.get("createdBy")))
                                .premiumRatio(calculatePremiumRatio(
                                                (BigDecimal) row.get("annualPremium"),
                                                (BigDecimal) row.get("sumInsured")
                                ))
                                .riskLevel(evaluateRiskLevel(
                                                toLocalDate(row.get("insuredBirthdate")),
                                                toRelationshipToInsured(row.get("relationshipToInsured")),
                                                (BigDecimal) row.get("sumInsured"),
                                                (BigDecimal) row.get("annualPremium")
                                ).name())
                                .build();
        }

        private String asString(Object value) {
                return value == null ? null : String.valueOf(value);
        }

        private LocalDateTime toLocalDateTime(Object value) {
                if (value == null) {
                        return null;
                }
                if (value instanceof LocalDateTime localDateTime) {
                        return localDateTime;
                }
                if (value instanceof java.sql.Timestamp timestamp) {
                        return timestamp.toLocalDateTime();
                }
                if (value instanceof java.util.Date date) {
                        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
                }
                return LocalDateTime.parse(String.valueOf(value));
        }

        private LocalDate toLocalDate(Object value) {
                if (value == null) {
                        return null;
                }
                if (value instanceof LocalDate localDate) {
                        return localDate;
                }
                if (value instanceof java.sql.Date sqlDate) {
                        return sqlDate.toLocalDate();
                }
                if (value instanceof java.util.Date date) {
                        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).toLocalDate();
                }
                return LocalDate.parse(String.valueOf(value));
        }

        private BigDecimal calculatePremiumRatio(BigDecimal annualPremium, BigDecimal sumInsured) {
                if (annualPremium == null || sumInsured == null || sumInsured.signum() == 0) {
                        return BigDecimal.ZERO;
                }
                return annualPremium.divide(sumInsured, 4, RoundingMode.HALF_UP);
        }

        private UnderwritingRiskLevel evaluateRiskLevel(
                LocalDate insuredBirthdate,
                RelationshipToInsured relationshipToInsured,
                BigDecimal sumInsured,
                BigDecimal annualPremium
        ) {
                int insuredAge = insuredBirthdate == null ? 0 : java.time.Period.between(insuredBirthdate, LocalDate.now()).getYears();
                BigDecimal premiumRatio = calculatePremiumRatio(annualPremium, sumInsured);

                if (insuredAge >= 60
                                || (sumInsured != null && sumInsured.compareTo(new BigDecimal("3000000.00")) >= 0)
                                || relationshipToInsured == RelationshipToInsured.PARENT
                                || relationshipToInsured == RelationshipToInsured.CHILD) {
                        return UnderwritingRiskLevel.HIGH;
                }
                if (insuredAge >= 50
                                || (sumInsured != null && sumInsured.compareTo(new BigDecimal("1500000.00")) >= 0)
                                || premiumRatio.compareTo(new BigDecimal("0.04")) >= 0
                                || relationshipToInsured == RelationshipToInsured.SPOUSE) {
                        return UnderwritingRiskLevel.MEDIUM;
                }
                return UnderwritingRiskLevel.LOW;
        }

        private RelationshipToInsured toRelationshipToInsured(Object value) {
                if (value == null) {
                        return null;
                }
                return RelationshipToInsured.valueOf(String.valueOf(value));
        }
}