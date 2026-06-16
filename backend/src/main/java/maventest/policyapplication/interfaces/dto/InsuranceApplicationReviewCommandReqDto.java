package maventest.policyapplication.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import maventest.policyapplication.domain.enums.ApplicationStatus;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceApplicationReviewCommandReqDto {

    @JsonProperty("APPLICATION_ID")
    @NotBlank(message = "applicationId is required")
    private String applicationId;

    @JsonProperty("TARGET_STATUS")
    @NotNull(message = "targetStatus is required")
    private ApplicationStatus targetStatus;

    @JsonProperty("REVIEWED_BY")
    private String reviewedBy;

    @JsonProperty("REJECTION_REASON")
    private String rejectionReason;

    @JsonProperty("DOCUMENTS_CONFIRMED")
    @NotNull(message = "documentsConfirmed is required")
    private Boolean documentsConfirmed;
}