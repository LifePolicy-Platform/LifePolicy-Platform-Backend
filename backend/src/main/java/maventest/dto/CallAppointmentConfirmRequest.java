package maventest.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CallAppointmentConfirmRequest {

    @NotBlank(message = "policyNo is required")
    private String policyNo;

    @NotNull(message = "recallResult is required")
    @Min(value = 1, message = "recallResult must be 1 or 2")
    @Max(value = 2, message = "recallResult must be 1 or 2")
    private Integer recallResult;
}
