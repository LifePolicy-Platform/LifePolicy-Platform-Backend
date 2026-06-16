package maventest.policyapplication.application.internal.commandservices;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import maventest.code.ApiCode;
import maventest.common.exception.BusinessRuleException;
import maventest.common.exception.ErrorInputException;
import maventest.policyapplication.application.internal.PolicyApplicationRuleService;
import maventest.policyapplication.domain.entity.InsuredPersonEntity;
import maventest.policyapplication.domain.entity.PolicyApplicationEntity;
import maventest.policyapplication.domain.entity.ProductEntity;
import maventest.policyapplication.infrastructure.repository.InsuranceApplicationRepository;
import maventest.policyapplication.interfaces.dto.InsuranceApplicationCommandReqDto;
import maventest.policyapplication.interfaces.dto.InsuranceApplicationCommandRespDto;
import maventest.policyapplication.interfaces.transform.InsuranceApplicationConverter;

@Service
@RequiredArgsConstructor
public class POL_APP_CMDCommandService {

    private final InsuranceApplicationRepository insuranceApplicationRepository;
    private final InsuranceApplicationConverter insuranceApplicationConverter;
    private final PolicyApplicationRuleService policyApplicationRuleService;

    @Transactional
    public InsuranceApplicationCommandRespDto createApplication(InsuranceApplicationCommandReqDto reqDto, String createdBy) {
        ProductEntity productEntity = insuranceApplicationRepository.findProductByCode(reqDto.getProductCode())
                .orElseThrow(() -> new ErrorInputException(ApiCode.PRODUCT_NOT_FOUND.getCode(), ApiCode.PRODUCT_NOT_FOUND.getMessage()));

        policyApplicationRuleService.validateApplicantAge(reqDto.getApplicantBirthdate());
        policyApplicationRuleService.validateInsuredAge(reqDto.getInsuredBirthdate(), productEntity);
        policyApplicationRuleService.validateRelationship(reqDto.getApplicantIdNo(), reqDto.getInsuredIdNo(), reqDto.getRelationshipToInsured());
        policyApplicationRuleService.validateSumInsured(reqDto.getSumInsured(), productEntity);
        policyApplicationRuleService.validateAnnualPremium(reqDto.getAnnualPremium(), reqDto.getSumInsured());
        validateDuplicate(reqDto.getApplicantIdNo(), reqDto.getInsuredIdNo(), reqDto.getProductCode());

        String applicationId = generateIdentifier("APP");
        String insuredId = generateIdentifier("INS");
        LocalDateTime submissionTime = LocalDateTime.now();

        PolicyApplicationEntity policyApplicationEntity = insuranceApplicationConverter
            .toPolicyApplicationEntity(reqDto, applicationId, submissionTime, createdBy);
        InsuredPersonEntity insuredPersonEntity = insuranceApplicationConverter
                .toInsuredPersonEntity(reqDto, applicationId, insuredId);

        insuranceApplicationRepository.save(policyApplicationEntity, insuredPersonEntity);

        return insuranceApplicationConverter.toCommandRespDto(policyApplicationEntity, reqDto.getInsuredBirthdate());
    }

    public InsuranceApplicationCommandRespDto createApplication(InsuranceApplicationCommandReqDto reqDto) {
        return createApplication(reqDto, reqDto.getCreatedBy());
    }

    private void validateDuplicate(String applicantIdNo, String insuredIdNo, String productCode) {
        if (insuranceApplicationRepository.existsPendingApplication(applicantIdNo, insuredIdNo, productCode)) {
            throw new BusinessRuleException(ApiCode.DUPLICATE_PENDING_APPLICATION.getCode(), ApiCode.DUPLICATE_PENDING_APPLICATION.getMessage());
        }
    }

    private String generateIdentifier(String prefix) {
        return prefix + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
    }
}