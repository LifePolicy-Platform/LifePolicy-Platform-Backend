package maventest.policyapplication.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceApplicationReviewCommandRespDto {

    @JsonProperty("APPLICATION_ID")
    private String applicationId;

    @JsonProperty("PREVIOUS_STATUS")
    private String previousStatus;

    @JsonProperty("CURRENT_STATUS")
    private String currentStatus;

    @JsonProperty("REVIEWED_BY")
    private String reviewedBy;

    @JsonProperty("REJECTION_REASON")
    private String rejectionReason;

    @JsonProperty("REVIEW_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime reviewTime;

    @JsonProperty("DOCUMENTS_CONFIRMED")
    private Boolean documentsConfirmed;
}