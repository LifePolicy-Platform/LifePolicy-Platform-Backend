package maventest.policyapplication.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceApplicationQueryReqDto {

    @JsonProperty("APPLICATION_ID")
    private String applicationId;

    @JsonProperty("APPLICANT_ID_NO")
    private String applicantIdNo;

    @JsonProperty("INSURED_ID_NO")
    private String insuredIdNo;

    @JsonProperty("APPLICATION_STATUS")
    private String applicationStatus;

    @JsonProperty("PRODUCT_CODE")
    private String productCode;

    @JsonProperty("SUBMISSION_START_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime submissionStartTime;

    @JsonProperty("SUBMISSION_END_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime submissionEndTime;

    @JsonProperty("PAGE_NO")
    @Builder.Default
    @Min(value = 1, message = "pageNo must be greater than or equal to 1")
    private Integer pageNo = 1;

    @JsonProperty("PAGE_SIZE")
    @Builder.Default
    @Min(value = 1, message = "pageSize must be greater than or equal to 1")
    @Max(value = 100, message = "pageSize must be less than or equal to 100")
    private Integer pageSize = 10;

    @JsonProperty("SORT_DIRECTION")
    @Builder.Default
    private String sortDirection = "DESC";
}