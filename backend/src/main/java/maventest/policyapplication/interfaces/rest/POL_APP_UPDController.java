package maventest.policyapplication.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import maventest.code.ApiCode;
import maventest.common.EntityUtil;
import maventest.common.ReturnMsg;
import maventest.common.exception.ApiException;
import maventest.policyapplication.application.internal.commandservices.POL_APP_UPDCommandService;
import maventest.policyapplication.interfaces.dto.InsuranceApplicationUpdateReqDto;
import maventest.policyapplication.interfaces.dto.InsuranceApplicationUpdateRespDto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/insurance/policy-applications")
@Tag(name = "Insurance Policy Application Update", description = "APPLICANT operations for updating pending applications")
public class POL_APP_UPDController {

    private final POL_APP_UPDCommandService polAppUpdCommandService;

    @PutMapping("/{applicationId}")
        @Operation(
            summary = "Update life insurance policy application",
            description = "Requires APPLICANT role. Only PENDING applications may be updated. In the JSP workbench, users must query the record first and then load it into the edit panel.",
            security = {@SecurityRequirement(name = "bearerAuth")}
        )
    public ResponseEntity<ReturnMsg<InsuranceApplicationUpdateRespDto>> updateApplication(
            @PathVariable String applicationId,
            @Valid @RequestBody InsuranceApplicationUpdateReqDto reqDto,
            BindingResult bindingResult
    ) {
        try {
            EntityUtil.validDto(bindingResult);
            InsuranceApplicationUpdateRespDto responseDto = polAppUpdCommandService.updateApplication(applicationId, reqDto);
            return ResponseEntity.ok(ReturnMsg.success(responseDto));
        } catch (ApiException apiException) {
            throw apiException;
        } catch (Exception exception) {
            throw new ApiException(ApiCode.SYSTEM_ERROR.getCode(), exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}