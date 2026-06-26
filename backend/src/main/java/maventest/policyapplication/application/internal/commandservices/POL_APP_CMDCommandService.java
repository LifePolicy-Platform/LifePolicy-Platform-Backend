package maventest.policyapplication.application.internal.commandservices;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import maventest.code.ApiCode;
import maventest.common.exception.BusinessRuleException;
import maventest.common.exception.ErrorInputException;
import maventest.policyapplication.application.internal.PolicyApplicationRuleService;
import maventest.policyapplication.application.internal.PolicyNumberGenerator;
import maventest.policyapplication.domain.entity.CallListEntity;
import maventest.policyapplication.domain.entity.PolicyApplicationEntity;
import maventest.policyapplication.domain.entity.ProductEntity;
import maventest.policyapplication.infrastructure.repository.InsuranceApplicationRepository;
import maventest.policyapplication.interfaces.dto.InsuranceApplicationCommandReqDto;
import maventest.policyapplication.interfaces.dto.InsuranceApplicationCommandRespDto;
import maventest.policyapplication.interfaces.transform.InsuranceApplicationConverter;
import maventest.policy.service.PolicyAprvLogAppender;
import maventest.auth.repository.AppUserRepository;
import maventest.notification.service.NotificationService;

@Service
@RequiredArgsConstructor
public class POL_APP_CMDCommandService {

    private static final String NOTIF_TYPE_POLICY = "POLICY";

    private static final int CALL_LIST_STATUS_PENDING = 0;

    private final InsuranceApplicationRepository insuranceApplicationRepository;
    private final InsuranceApplicationConverter insuranceApplicationConverter;
    private final PolicyApplicationRuleService policyApplicationRuleService;
    private final PolicyNumberGenerator policyNumberGenerator;
    private final AppUserRepository appUserRepository;
    private final PolicyAprvLogAppender policyAprvLogAppender;
    private final NotificationService notificationService;

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
        
        Long memberId = insuranceApplicationRepository.findMemberIdByIdentityCard(reqDto.getApplicantIdNo())
                .orElseThrow(() -> new ErrorInputException(ApiCode.MEMBER_NOT_FOUND.getCode(), ApiCode.MEMBER_NOT_FOUND.getMessage()));

        LocalDateTime submissionTime = LocalDateTime.now();
        String listNo = policyNumberGenerator.generateListNo(submissionTime);
        insuranceApplicationRepository.insertCallList(CallListEntity.builder()
                .listNo(listNo)
                .memberId(memberId)
                .listLastPhone(reqDto.getContactPhone())
                .listStatus(CALL_LIST_STATUS_PENDING)
                .build());

        String applicationId = policyNumberGenerator.generatePolicyNo(submissionTime);

        Long agentId = resolveAgentId(createdBy);

        PolicyApplicationEntity policyApplicationEntity = insuranceApplicationConverter
                .toPolicyApplicationEntity(reqDto, applicationId, memberId, listNo, submissionTime, createdBy, agentId);

        insuranceApplicationRepository.save(policyApplicationEntity);

        policyAprvLogAppender.append(
                applicationId,
                "SUBMIT",
                createdBy,
                "業務送件"
        );

        notificationService.pushToRole("APPLICANT", NOTIF_TYPE_POLICY,
                "新件待審",
                "保單 " + applicationId + " 已送件，請確認處理。",
                applicationId, "SYSTEM");

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

    private Long resolveAgentId(String username) {
        if (username == null || username.isBlank()) {
            return null;
        }
        return appUserRepository.findByUsername(username)
                .map(user -> user.getId())
                .orElse(null);
    }
}
