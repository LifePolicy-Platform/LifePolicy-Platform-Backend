package maventest.policyapplication.application.internal;



import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;

import org.springframework.stereotype.Service;

import maventest.code.ApiCode;
import maventest.common.exception.BusinessRuleException;
import maventest.policyapplication.domain.entity.ProductEntity;
import maventest.policyapplication.domain.enums.RelationshipToInsured;
import maventest.policyapplication.domain.enums.UnderwritingRiskLevel;

@Service
public class PolicyApplicationRuleService {

    public static final BigDecimal PREMIUM_RATIO_LIMIT = new BigDecimal("0.05");
    public static final BigDecimal MAX_ANNUAL_PREMIUM = new BigDecimal("1000000.00");
    private static final BigDecimal MEDIUM_RATIO_THRESHOLD = new BigDecimal("0.04");
    private static final BigDecimal MEDIUM_SUM_INSURED_THRESHOLD = new BigDecimal("1500000.00");
    private static final BigDecimal HIGH_SUM_INSURED_THRESHOLD = new BigDecimal("3000000.00");

    public void validateApplicantAge(LocalDate applicantBirthdate) {
        int applicantAge = Period.between(applicantBirthdate, LocalDate.now()).getYears();
        if (applicantAge < 18) {
            throw new BusinessRuleException(ApiCode.APPLICANT_AGE_INVALID.getCode(), ApiCode.APPLICANT_AGE_INVALID.getMessage());
        }
    }

    public void validateInsuredAge(LocalDate insuredBirthdate, ProductEntity productEntity) {
        int insuredAge = Period.between(insuredBirthdate, LocalDate.now()).getYears();
        if (insuredAge < productEntity.getMinAge() || insuredAge > productEntity.getMaxAge()) {
            throw new BusinessRuleException(ApiCode.INSURED_AGE_INVALID.getCode(), ApiCode.INSURED_AGE_INVALID.getMessage());
        }
    }

    public void validateRelationship(String applicantIdNo, String insuredIdNo, RelationshipToInsured relationshipToInsured) {
        if (applicantIdNo.equals(insuredIdNo) && relationshipToInsured != RelationshipToInsured.SELF) {
            throw new BusinessRuleException(ApiCode.RELATIONSHIP_INVALID.getCode(), ApiCode.RELATIONSHIP_INVALID.getMessage());
        }
    }

    public void validateSumInsured(BigDecimal sumInsured, ProductEntity productEntity) {
        if (sumInsured.compareTo(productEntity.getMinAmount()) < 0
                || sumInsured.compareTo(productEntity.getMaxAmount()) > 0) {
            throw new BusinessRuleException(ApiCode.SUM_INSURED_INVALID.getCode(), ApiCode.SUM_INSURED_INVALID.getMessage());
        }
    }

    public void validateAnnualPremium(BigDecimal annualPremium, BigDecimal sumInsured) {
        BigDecimal premiumLimit = sumInsured.multiply(PREMIUM_RATIO_LIMIT);
        if (annualPremium.compareTo(premiumLimit) > 0) {
            throw new BusinessRuleException(ApiCode.PREMIUM_RATIO_INVALID.getCode(), ApiCode.PREMIUM_RATIO_INVALID.getMessage());
        }
        if (annualPremium.compareTo(MAX_ANNUAL_PREMIUM) > 0) {
            throw new BusinessRuleException(ApiCode.PREMIUM_LIMIT_INVALID.getCode(), ApiCode.PREMIUM_LIMIT_INVALID.getMessage());
        }
    }

    public BigDecimal calculatePremiumRatio(BigDecimal annualPremium, BigDecimal sumInsured) {
        if (sumInsured == null || sumInsured.signum() == 0 || annualPremium == null) {
            return BigDecimal.ZERO;
        }
        return annualPremium.divide(sumInsured, 4, RoundingMode.HALF_UP);
    }

    /** 主管核准後：生效日 = 審核日 D+1 */
    public LocalDate calculateApprovedEffectDate(LocalDate approvalDate) {
        return approvalDate.plusDays(1);
    }

    /** 主管核准後：到期日 = 生效日 + 商品年期（年） */
    public LocalDate calculateApprovedExpireDate(LocalDate effectDate, Integer productTermYears) {
        if (productTermYears == null || productTermYears <= 0) {
            throw new BusinessRuleException(ApiCode.INPUT_INVALID.getCode(), "productTerm must be greater than zero");
        }
        return effectDate.plusYears(productTermYears);
    }

    public UnderwritingRiskLevel evaluateRiskLevel(
            LocalDate insuredBirthdate,
            RelationshipToInsured relationshipToInsured,
            BigDecimal sumInsured,
            BigDecimal annualPremium
    ) {
        int insuredAge = insuredBirthdate == null ? 0 : Period.between(insuredBirthdate, LocalDate.now()).getYears();
        BigDecimal premiumRatio = calculatePremiumRatio(annualPremium, sumInsured);

        if (insuredAge >= 60
                || (sumInsured != null && sumInsured.compareTo(HIGH_SUM_INSURED_THRESHOLD) >= 0)
                || relationshipToInsured == RelationshipToInsured.PARENT
                || relationshipToInsured == RelationshipToInsured.CHILD) {
            return UnderwritingRiskLevel.HIGH;
        }

        if (insuredAge >= 50
                || (sumInsured != null && sumInsured.compareTo(MEDIUM_SUM_INSURED_THRESHOLD) >= 0)
                || premiumRatio.compareTo(MEDIUM_RATIO_THRESHOLD) >= 0
                || relationshipToInsured == RelationshipToInsured.SPOUSE) {
            return UnderwritingRiskLevel.MEDIUM;
        }

        return UnderwritingRiskLevel.LOW;
    }
}