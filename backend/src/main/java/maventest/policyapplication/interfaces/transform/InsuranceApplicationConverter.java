package maventest.policyapplication.interfaces.transform;

import org.springframework.stereotype.Component;

import maventest.policyapplication.domain.entity.InsuredPersonEntity;
import maventest.policyapplication.domain.entity.PolicyApplicationEntity;
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
            Long memberId,
            String listNo,
            LocalDateTime submissionTime,
            String createdBy,
            Long agentId
    ) {
        return PolicyApplicationEntity.builder()
                .policyNo(applicationId)
                .memberId(memberId)
                .listNo(listNo)
                .productCode(reqDto.getProductCode())
                .policyStatus("SUBMIT")
                .insuredName(reqDto.getInsuredName())
                .insuredIdentityCard(reqDto.getInsuredIdNo())
                .insuredGender(reqDto.getInsuredGender() != null ? reqDto.getInsuredGender().name() : null)
                .insuredBirthday(reqDto.getInsuredBirthdate())
                .insuredAmount(reqDto.getSumInsured())
                .policyMobile(reqDto.getContactPhone())
                .premiumAmount(reqDto.getAnnualPremium())
                .agentId(agentId)
                .riskLevel(reqDto.getRiskLevel())
                .applyTime(submissionTime)
                .updateUser(createdBy)
                .build();
    }

        public PolicyApplicationEntity toPolicyApplicationEntity(
                        InsuranceApplicationCommandReqDto reqDto,
                        String applicationId,
                        Long memberId,
                        String listNo,
                        LocalDateTime submissionTime
        ) {
                return toPolicyApplicationEntity(reqDto, applicationId, memberId, listNo, submissionTime, reqDto.getCreatedBy(), null);
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
                policyApplicationEntity.getPremiumAmount(),
                policyApplicationEntity.getInsuredAmount()
        );
        UnderwritingRiskLevel riskLevel = policyApplicationEntity.getRiskLevel() != null
                ? UnderwritingRiskLevel.valueOf(policyApplicationEntity.getRiskLevel())
                : evaluateRiskLevel(
                        insuredBirthdate,
                        null,
                        policyApplicationEntity.getInsuredAmount(),
                        policyApplicationEntity.getPremiumAmount()
                );

        return InsuranceApplicationCommandRespDto.builder()
                .applicationId(policyApplicationEntity.getPolicyNo())
                .applicationStatus(policyApplicationEntity.getPolicyStatus())
                .submissionTime(policyApplicationEntity.getApplyTime())
                .applicantIdNo(null)
                .insuredIdNo(policyApplicationEntity.getInsuredIdentityCard())
                .productCode(policyApplicationEntity.getProductCode())
                .sumInsured(policyApplicationEntity.getInsuredAmount())
                .annualPremium(policyApplicationEntity.getPremiumAmount())
                .premiumRatio(premiumRatio)
                .riskLevel(riskLevel.name())
                .build();
    }

        public InsuranceApplicationCommandRespDto toCommandRespDto(PolicyApplicationEntity policyApplicationEntity) {
                return toCommandRespDto(policyApplicationEntity, null);
        }

    public PolicyApplicationEntity toUpdatedPolicyApplicationEntity(
            PolicyApplicationEntity existingApplication,
            InsuranceApplicationUpdateReqDto reqDto,
            String updatedBy
    ) {
        String nextStatus = "RETURN".equals(existingApplication.getPolicyStatus())
                ? "SUBMIT"
                : existingApplication.getPolicyStatus();
        return PolicyApplicationEntity.builder()
                .policyNo(existingApplication.getPolicyNo())
                .memberId(existingApplication.getMemberId())
                .listNo(existingApplication.getListNo())
                .productCode(reqDto.getProductCode())
                .policyStatus(nextStatus)
                .insuredName(reqDto.getInsuredName())
                .insuredIdentityCard(reqDto.getInsuredIdNo())
                .insuredGender(reqDto.getInsuredGender() != null ? reqDto.getInsuredGender().name() : null)
                .insuredBirthday(reqDto.getInsuredBirthdate())
                .insuredAmount(reqDto.getSumInsured())
                .policyMobile(reqDto.getContactPhone())
                .premiumAmount(reqDto.getAnnualPremium())
                .agentId(existingApplication.getAgentId())
                .riskLevel(existingApplication.getRiskLevel())
                .effectDate(existingApplication.getEffectDate())
                .expireDate(existingApplication.getExpireDate())
                .applyTime(existingApplication.getApplyTime())
                .createTime(existingApplication.getCreateTime())
                .updateUser(updatedBy)
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
                .applicationId(updatedApplication.getPolicyNo())
                .applicationStatus(updatedApplication.getPolicyStatus())
                .updatedTime(updatedTime)
                .productCode(updatedApplication.getProductCode())
                .sumInsured(updatedApplication.getInsuredAmount())
                .annualPremium(updatedApplication.getPremiumAmount())
                .premiumRatio(calculatePremiumRatio(updatedApplication.getPremiumAmount(), updatedApplication.getInsuredAmount()))
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
                .policyNo(existingApplication.getPolicyNo())
                .memberId(existingApplication.getMemberId())
                .listNo(existingApplication.getListNo())
                .productCode(existingApplication.getProductCode())
                .policyStatus(reqDto.getTargetStatus() != null ? reqDto.getTargetStatus().name() : null)
                .insuredName(existingApplication.getInsuredName())
                .insuredIdentityCard(existingApplication.getInsuredIdentityCard())
                .insuredGender(existingApplication.getInsuredGender())
                .insuredBirthday(existingApplication.getInsuredBirthday())
                .insuredAmount(existingApplication.getInsuredAmount())
                .policyMobile(existingApplication.getPolicyMobile())
                .premiumAmount(existingApplication.getPremiumAmount())
                .agentId(existingApplication.getAgentId())
                .riskLevel(existingApplication.getRiskLevel())
                .effectDate(existingApplication.getEffectDate())
                .expireDate(existingApplication.getExpireDate())
                .applyTime(existingApplication.getApplyTime())
                .createTime(existingApplication.getCreateTime())
                .updateUser(reviewedBy)
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
            InsuranceApplicationReviewCommandReqDto reqDto
    ) {
        return InsuranceApplicationReviewCommandRespDto.builder()
                .applicationId(after.getPolicyNo())
                .previousStatus(before.getPolicyStatus())
                .currentStatus(after.getPolicyStatus())
                .reviewedBy(after.getUpdateUser())
                .rejectionReason(reqDto.getRejectionReason())
                .reviewTime(after.getUpdateTime())
                .documentsConfirmed(reqDto.getDocumentsConfirmed())
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
                                .riskLevel(row.get("riskLevel") != null
                                                ? asString(row.get("riskLevel"))
                                                : evaluateRiskLevel(
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