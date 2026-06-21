package maventest.policyapplication.application.internal.commandservices;


import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import maventest.code.ApiCode;
import maventest.common.exception.BusinessRuleException;
import maventest.common.exception.ErrorInputException;
import maventest.policyapplication.application.internal.PolicyApplicationRuleService;
import maventest.policyapplication.domain.entity.PolicyApplicationEntity;
import maventest.policyapplication.domain.entity.ProductEntity;
import maventest.policyapplication.infrastructure.repository.InsuranceApplicationRepository;
import maventest.policyapplication.interfaces.dto.InsuranceApplicationUpdateReqDto;
import maventest.policyapplication.interfaces.dto.InsuranceApplicationUpdateRespDto;
import maventest.policyapplication.interfaces.transform.InsuranceApplicationConverter;
import maventest.service.PolicyAprvLogAppender;

@Service
@RequiredArgsConstructor
public class POL_APP_UPDCommandService {

    private final InsuranceApplicationRepository insuranceApplicationRepository;
    private final InsuranceApplicationConverter insuranceApplicationConverter;
    private final PolicyApplicationRuleService policyApplicationRuleService;
    private final PolicyAprvLogAppender policyAprvLogAppender;

    @Transactional
    public InsuranceApplicationUpdateRespDto updateApplication(
            String applicationId,
            InsuranceApplicationUpdateReqDto reqDto,
            String updatedBy
    ) {
        PolicyApplicationEntity existingApplication = insuranceApplicationRepository.findApplicationById(applicationId)
                .orElseThrow(() -> new ErrorInputException(ApiCode.APPLICATION_NOT_FOUND.getCode(), ApiCode.APPLICATION_NOT_FOUND.getMessage()));

        validateUpdatable(existingApplication);

        ProductEntity productEntity = insuranceApplicationRepository.findProductByCode(reqDto.getProductCode())
                .orElseThrow(() -> new ErrorInputException(ApiCode.PRODUCT_NOT_FOUND.getCode(), ApiCode.PRODUCT_NOT_FOUND.getMessage()));

        policyApplicationRuleService.validateApplicantAge(reqDto.getApplicantBirthdate());
        policyApplicationRuleService.validateInsuredAge(reqDto.getInsuredBirthdate(), productEntity);
        policyApplicationRuleService.validateRelationship(reqDto.getApplicantIdNo(), reqDto.getInsuredIdNo(), reqDto.getRelationshipToInsured());
        policyApplicationRuleService.validateSumInsured(reqDto.getSumInsured(), productEntity);
        policyApplicationRuleService.validateAnnualPremium(reqDto.getAnnualPremium(), reqDto.getSumInsured());

        if (insuranceApplicationRepository.existsPendingApplicationExcluding(
                applicationId,
                reqDto.getApplicantIdNo(),
                reqDto.getInsuredIdNo(),
                reqDto.getProductCode()
        )) {
            throw new BusinessRuleException(ApiCode.DUPLICATE_PENDING_APPLICATION.getCode(), ApiCode.DUPLICATE_PENDING_APPLICATION.getMessage());
        }

        LocalDateTime updatedTime = LocalDateTime.now();
        PolicyApplicationEntity updatedApplication = insuranceApplicationConverter.toUpdatedPolicyApplicationEntity(
                existingApplication,
                reqDto,
                updatedBy
        );

        int applicationRows = insuranceApplicationRepository.updateApplication(updatedApplication);
        if (applicationRows == 0) {
            throw new BusinessRuleException(ApiCode.APPLICATION_UPDATE_NOT_ALLOWED.getCode(), ApiCode.APPLICATION_UPDATE_NOT_ALLOWED.getMessage());
        }

        policyAprvLogAppender.append(
                updatedApplication.getPolicyNo(),
                "SUBMIT",
                updatedBy,
                "補件後重新送審"
        );

        return insuranceApplicationConverter.toUpdateRespDto(updatedApplication, reqDto, updatedTime);
    }

    public InsuranceApplicationUpdateRespDto updateApplication(String applicationId, InsuranceApplicationUpdateReqDto reqDto) {
        return updateApplication(applicationId, reqDto, null);
    }

    private void validateUpdatable(PolicyApplicationEntity existingApplication) {
        if (!"RETURN".equals(existingApplication.getPolicyStatus())) {
            throw new BusinessRuleException(ApiCode.APPLICATION_UPDATE_NOT_ALLOWED.getCode(), ApiCode.APPLICATION_UPDATE_NOT_ALLOWED.getMessage());
        }
    }
}
