package maventest.policyapplication.application.internal.commandservices;



import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import maventest.code.ApiCode;
import maventest.common.exception.ApiException;
import maventest.common.exception.BusinessRuleException;
import maventest.common.exception.ErrorInputException;
import maventest.policyapplication.application.internal.PolicyApplicationRuleService;
import maventest.policyapplication.application.internal.PolicyApplicationStatusTransition;
import maventest.policyapplication.domain.entity.PolicyApplicationEntity;
import maventest.product.entity.ProductEntity;
import maventest.policyapplication.domain.enums.ApplicationStatus;
import maventest.policyapplication.infrastructure.repository.InsuranceApplicationRepository;
import maventest.policyapplication.interfaces.dto.InsuranceApplicationReviewCommandReqDto;
import maventest.policyapplication.interfaces.dto.InsuranceApplicationReviewCommandRespDto;
import maventest.policyapplication.interfaces.transform.InsuranceApplicationConverter;
import maventest.policy.service.PolicyAprvLogAppender;
import maventest.auth.repository.AppUserRepository;
import maventest.notification.service.NotificationService;

@Service
@RequiredArgsConstructor
public class POL_APP_APRVCommandService {

    private static final String NOTIF_TYPE_POLICY = "POLICY";

    private final InsuranceApplicationRepository insuranceApplicationRepository;
    private final InsuranceApplicationConverter insuranceApplicationConverter;
    private final PolicyApplicationRuleService policyApplicationRuleService;
    private final PolicyAprvLogAppender policyAprvLogAppender;
    private final NotificationService notificationService;
    private final AppUserRepository appUserRepository;

    @Transactional
    public InsuranceApplicationReviewCommandRespDto reviewApplication(
            InsuranceApplicationReviewCommandReqDto reqDto,
            String reviewedBy,
            String roleCode
    ) {
        PolicyApplicationEntity existingApplication = insuranceApplicationRepository.findApplicationById(reqDto.getApplicationId())
                .orElseThrow(() -> new ErrorInputException(ApiCode.APPLICATION_NOT_FOUND.getCode(), ApiCode.APPLICATION_NOT_FOUND.getMessage()));

        String currentStatus = existingApplication.getPolicyStatus();
        validateReviewable(currentStatus);
        validateReviewerRole(currentStatus, roleCode);
        validateReviewStatus(currentStatus, reqDto);
        validateDocuments(currentStatus, reqDto);

        LocalDateTime reviewTime = LocalDateTime.now();

        PolicyApplicationEntity reviewTarget = insuranceApplicationConverter.toReviewPolicyApplicationEntity(
                existingApplication,
                reqDto,
                reviewTime,
                reviewedBy
        );

        if (isSupervisorApproval(currentStatus, reqDto.getTargetStatus())) {
            reviewTarget = applyApprovedPolicyDates(reviewTarget, reviewTime);
        }

        int updatedRows = insuranceApplicationRepository.updateApplicationReview(reviewTarget, currentStatus);
        if (updatedRows == 0) {
            throw new BusinessRuleException(ApiCode.APPLICATION_REVIEWED.getCode(), ApiCode.APPLICATION_REVIEWED.getMessage());
        }

        appendReviewLog(reviewTarget.getPolicyNo(), reqDto, reviewedBy);
        pushReviewNotification(existingApplication, reqDto.getTargetStatus(), reviewedBy);

        return insuranceApplicationConverter.toReviewCommandRespDto(existingApplication, reviewTarget, reqDto);
    }

    public InsuranceApplicationReviewCommandRespDto reviewApplication(InsuranceApplicationReviewCommandReqDto reqDto) {
        return reviewApplication(reqDto, reqDto.getReviewedBy(), null);
    }

    private void validateReviewerRole(String currentStatus, String roleCode) {
        if (!PolicyApplicationStatusTransition.isRoleAllowedForReviewStage(currentStatus, roleCode)) {
            throw new ApiException(ApiCode.ACCESS_DENIED.getCode(), ApiCode.ACCESS_DENIED.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    private void validateReviewStatus(String currentStatus, InsuranceApplicationReviewCommandReqDto reqDto) {
        ApplicationStatus targetStatus = reqDto.getTargetStatus();
        if (!PolicyApplicationStatusTransition.isAllowedReviewTransition(currentStatus, targetStatus)) {
            throw new BusinessRuleException(ApiCode.REVIEW_STATUS_INVALID.getCode(), ApiCode.REVIEW_STATUS_INVALID.getMessage());
        }
        if (PolicyApplicationStatusTransition.requiresRejectionReason(targetStatus)
                && (reqDto.getRejectionReason() == null || reqDto.getRejectionReason().isBlank())) {
            throw new BusinessRuleException(ApiCode.REVIEW_REASON_REQUIRED.getCode(), ApiCode.REVIEW_REASON_REQUIRED.getMessage());
        }
    }

    private void validateReviewable(String currentStatus) {
        if (!PolicyApplicationStatusTransition.isReviewable(currentStatus)) {
            throw new BusinessRuleException(ApiCode.APPLICATION_REVIEWED.getCode(), ApiCode.APPLICATION_REVIEWED.getMessage());
        }
    }

    private void validateDocuments(String currentStatus, InsuranceApplicationReviewCommandReqDto reqDto) {
        if (PolicyApplicationStatusTransition.requiresDocumentConfirmation(currentStatus)
                && !Boolean.TRUE.equals(reqDto.getDocumentsConfirmed())) {
            throw new BusinessRuleException(ApiCode.DOCUMENTS_INCOMPLETE.getCode(), ApiCode.DOCUMENTS_INCOMPLETE.getMessage());
        }
    }

    private void appendReviewLog(
            String policyNo,
            InsuranceApplicationReviewCommandReqDto reqDto,
            String reviewedBy
    ) {
        ApplicationStatus targetStatus = reqDto.getTargetStatus();
        String remark = resolveReviewRemark(targetStatus, reqDto.getRejectionReason());
        policyAprvLogAppender.append(policyNo, targetStatus.name(), reviewedBy, remark);
    }

    private String resolveReviewRemark(ApplicationStatus targetStatus, String rejectionReason) {
        return switch (targetStatus) {
            case PENDING -> "業務審核通過";
            case RETURN -> rejectionReason;
            case APPROVED -> "主管審核通過";
            case REJECTED -> rejectionReason;
            default -> null;
        };
    }

    private void pushReviewNotification(PolicyApplicationEntity app,
                                        ApplicationStatus targetStatus,
                                        String reviewedBy) {
        String policyNo = app.getPolicyNo();
        String agentUsername = appUserRepository.findById(app.getAgentId())
                .map(u -> u.getUsername())
                .orElse(null);

        switch (targetStatus) {
            case RETURN -> notificationService.pushToMember(
                    app.getMemberId(), NOTIF_TYPE_POLICY,
                    "保單退回通知",
                    "保單 " + policyNo + " 已被退回，請確認並補正資料。",
                    policyNo, reviewedBy);

            case PENDING -> notificationService.pushToRole(
                    "REVIEWER", NOTIF_TYPE_POLICY,
                    "待審保單通知",
                    "保單 " + policyNo + " 已送主管審核，請確認處理。",
                    policyNo, reviewedBy);

            case APPROVED -> {
                notificationService.pushToUsername(agentUsername, NOTIF_TYPE_POLICY,
                        "保單核准通知",
                        "保單 " + policyNo + " 已核准。",
                        policyNo, reviewedBy);
                notificationService.pushToMember(app.getMemberId(), NOTIF_TYPE_POLICY,
                        "保單核准通知",
                        "您的保單 " + policyNo + " 已核准，感謝投保。",
                        policyNo, reviewedBy);
            }

            case REJECTED -> {
                notificationService.pushToUsername(agentUsername, NOTIF_TYPE_POLICY,
                        "保單拒絕通知",
                        "保單 " + policyNo + " 已被拒絕。",
                        policyNo, reviewedBy);
                notificationService.pushToMember(app.getMemberId(), NOTIF_TYPE_POLICY,
                        "保單拒絕通知",
                        "您的保單 " + policyNo + " 申請未通過，請洽業務人員。",
                        policyNo, reviewedBy);
            }

            default -> { /* 其他狀態不推播 */ }
        }
    }

     private boolean isSupervisorApproval(String currentStatus, ApplicationStatus targetStatus) {
        return ApplicationStatus.PENDING.name().equals(currentStatus)
                && ApplicationStatus.APPROVED.equals(targetStatus);
    }

    private PolicyApplicationEntity applyApprovedPolicyDates(
            PolicyApplicationEntity reviewTarget,
            LocalDateTime reviewTime
    ) {
        ProductEntity product = insuranceApplicationRepository.findProductByCode(reviewTarget.getProductCode())
                .orElseThrow(() -> new ErrorInputException(
                        ApiCode.PRODUCT_NOT_FOUND.getCode(),
                        ApiCode.PRODUCT_NOT_FOUND.getMessage()
                ));

        LocalDate effectDate = policyApplicationRuleService.calculateApprovedEffectDate(reviewTime.toLocalDate());
        LocalDate expireDate = policyApplicationRuleService.calculateApprovedExpireDate(
                effectDate,
                product.getProductTerm()
        );

        return reviewTarget.toBuilder()
                .effectDate(effectDate)
                .expireDate(expireDate)
                .build();
    }
}
