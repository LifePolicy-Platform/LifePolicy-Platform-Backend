package maventest.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import maventest.code.ApiCode;
import maventest.common.ReturnMsg;
import maventest.common.exception.ApiException;
import maventest.common.exception.ErrorInputException;
import maventest.dto.PolicyAprvLogList;
import maventest.service.PolicyAprvLogService;

@Tag(name = "保單歷程查詢", description = "提供保單歷程資料的 GET API")
@RestController
@RequestMapping("/api/policy-log")
@RequiredArgsConstructor
public class QryPolicyLogController {

    private final PolicyAprvLogService policyAprvLogService;

    @GetMapping("/{policyNo}")
    @Operation(
            summary = "依保單號查詢審核歷程",
            security = {@SecurityRequirement(name = "bearerAuth")}
    )
    public ResponseEntity<ReturnMsg<List<PolicyAprvLogList>>> findByPolicyNo(
            @PathVariable String policyNo) {
        try {
            if (policyNo == null || policyNo.isBlank()) {
                throw new ErrorInputException(ApiCode.INPUT_INVALID.getCode(), "policyNo is required");
            }
            List<PolicyAprvLogList> logs = policyAprvLogService.findByPolicyNo(policyNo.trim());
            return ResponseEntity.ok(ReturnMsg.success(logs));
        } catch (ApiException apiException) {
            throw apiException;
        } catch (Exception exception) {
            throw new ApiException(ApiCode.SYSTEM_ERROR.getCode(), exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
