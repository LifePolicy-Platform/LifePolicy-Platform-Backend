package maventest.policyapplication.application.internal;

import maventest.policyapplication.domain.enums.ApplicationStatus;

public final class PolicyApplicationStatusTransition {

    private PolicyApplicationStatusTransition() {
    }

    public static boolean isReviewable(String currentStatus) {
        return ApplicationStatus.SUBMIT.name().equals(currentStatus)
                || ApplicationStatus.PENDING.name().equals(currentStatus);
    }

    public static boolean isAllowedReviewTransition(String currentStatus, ApplicationStatus targetStatus) {
        if (currentStatus == null || targetStatus == null) {
            return false;
        }
        return switch (currentStatus) {
            case "SUBMIT" -> targetStatus == ApplicationStatus.PENDING
                    || targetStatus == ApplicationStatus.RETURN;
            case "PENDING" -> targetStatus == ApplicationStatus.APPROVED
                    || targetStatus == ApplicationStatus.REJECTED;
            default -> false;
        };
    }

    public static boolean requiresRejectionReason(ApplicationStatus targetStatus) {
        return targetStatus == ApplicationStatus.RETURN
                || targetStatus == ApplicationStatus.REJECTED;
    }

    public static boolean requiresDocumentConfirmation(String currentStatus) {
        return ApplicationStatus.PENDING.name().equals(currentStatus);
    }

    public static boolean isBusinessReviewStage(String currentStatus) {
        return ApplicationStatus.SUBMIT.name().equals(currentStatus);
    }

    public static boolean isSupervisorReviewStage(String currentStatus) {
        return ApplicationStatus.PENDING.name().equals(currentStatus);
    }

    public static boolean isRoleAllowedForReviewStage(String currentStatus, String roleCode) {
        if (roleCode == null || roleCode.isBlank()) {
            return false;
        }
        if (isBusinessReviewStage(currentStatus)) {
            return "APPLICANT".equals(roleCode);
        }
        if (isSupervisorReviewStage(currentStatus)) {
            return "REVIEWER".equals(roleCode);
        }
        return false;
    }
}
