package maventest.policyapplication.application.internal.commandservices;



import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import maventest.code.ApiCode;
import maventest.common.exception.BusinessRuleException;
import maventest.common.exception.ErrorInputException;
import maventest.policyapplication.domain.entity.PolicyApplicationEntity;
import maventest.policyapplication.domain.enums.ApplicationStatus;
import maventest.policyapplication.infrastructure.repository.InsuranceApplicationRepository;
import maventest.policyapplication.interfaces.dto.InsuranceApplicationReviewCommandReqDto;
import maventest.policyapplication.interfaces.dto.InsuranceApplicationReviewCommandRespDto;
import maventest.policyapplication.interfaces.transform.InsuranceApplicationConverter;

@Service
@RequiredArgsConstructor
public class POL_APP_APRVCommandService {

    private final InsuranceApplicationRepository insuranceApplicationRepository;
    private final InsuranceApplicationConverter insuranceApplicationConverter;

    @Transactional
    public InsuranceApplicationReviewCommandRespDto reviewApplication(InsuranceApplicationReviewCommandReqDto reqDto, String reviewedBy) {
        PolicyApplicationEntity existingApplication = insuranceApplicationRepository.findApplicationById(reqDto.getApplicationId())
                .orElseThrow(() -> new ErrorInputException(ApiCode.APPLICATION_NOT_FOUND.getCode(), ApiCode.APPLICATION_NOT_FOUND.getMessage()));

        validateReviewStatus(reqDto);
        validateReviewable(existingApplication);
        validateDocuments(reqDto);

        LocalDateTime reviewTime = LocalDateTime.now();

        PolicyApplicationEntity reviewTarget = insuranceApplicationConverter.toReviewPolicyApplicationEntity(
                existingApplication,
                reqDto,
            reviewTime,
            reviewedBy
        );

        int updatedRows = insuranceApplicationRepository.updateApplicationReview(reviewTarget);
        if (updatedRows == 0) {
            throw new BusinessRuleException(ApiCode.APPLICATION_REVIEWED.getCode(), ApiCode.APPLICATION_REVIEWED.getMessage());
        }

        return insuranceApplicationConverter.toReviewCommandRespDto(existingApplication, reviewTarget, reqDto.getDocumentsConfirmed());
    }

    public InsuranceApplicationReviewCommandRespDto reviewApplication(InsuranceApplicationReviewCommandReqDto reqDto) {
        return reviewApplication(reqDto, reqDto.getReviewedBy());
    }

    private void validateReviewStatus(InsuranceApplicationReviewCommandReqDto reqDto) {
        if (reqDto.getTargetStatus() != ApplicationStatus.APPROVED
                && reqDto.getTargetStatus() != ApplicationStatus.REJECTED) {
            throw new BusinessRuleException(ApiCode.REVIEW_STATUS_INVALID.getCode(), ApiCode.REVIEW_STATUS_INVALID.getMessage());
        }
        if (reqDto.getTargetStatus() == ApplicationStatus.REJECTED
                && (reqDto.getRejectionReason() == null || reqDto.getRejectionReason().isBlank())) {
            throw new BusinessRuleException(ApiCode.REVIEW_REASON_REQUIRED.getCode(), ApiCode.REVIEW_REASON_REQUIRED.getMessage());
        }
    }

    private void validateReviewable(PolicyApplicationEntity existingApplication) {
        if (existingApplication.getApplicationStatus() != ApplicationStatus.PENDING) {
            throw new BusinessRuleException(ApiCode.APPLICATION_REVIEWED.getCode(), ApiCode.APPLICATION_REVIEWED.getMessage());
        }
    }

    private void validateDocuments(InsuranceApplicationReviewCommandReqDto reqDto) {
        if (!Boolean.TRUE.equals(reqDto.getDocumentsConfirmed())) {
            throw new BusinessRuleException(ApiCode.DOCUMENTS_INCOMPLETE.getCode(), ApiCode.DOCUMENTS_INCOMPLETE.getMessage());
        }
    }
}