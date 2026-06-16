package maventest.policyapplication.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import maventest.common.EntityUtil;
import maventest.common.ReturnMsg;
import maventest.common.exception.ApiException;
import maventest.policyapplication.application.internal.commandservices.POL_APP_CMDCommandService;
import maventest.policyapplication.interfaces.dto.InsuranceApplicationCommandReqDto;
import maventest.policyapplication.interfaces.dto.InsuranceApplicationCommandRespDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/insurance/policy-applications")
@Tag(name = "Insurance Policy Application", description = "APPLICANT operations for creating policy applications")
public class POL_APP_CMDController {

    private final POL_APP_CMDCommandService polAppCmdCommandService;

    @PostMapping
        @Operation(
            summary = "Create life insurance policy application",
            description = "Requires APPLICANT role. createdBy is derived from the current Bearer token rather than trusting client input.",
            security = {@SecurityRequirement(name = "bearerAuth")}
        )
    public ResponseEntity<ReturnMsg<InsuranceApplicationCommandRespDto>> createApplication(
            @Valid @RequestBody InsuranceApplicationCommandReqDto reqDto,
            BindingResult bindingResult
    ) {
        try {
            EntityUtil.validDto(bindingResult);
            String actor = resolveActor(reqDto.getCreatedBy());
            InsuranceApplicationCommandRespDto responseDto = polAppCmdCommandService.createApplication(reqDto, actor);
            return ResponseEntity.ok(ReturnMsg.success(responseDto));
        } catch (ApiException apiException) {
            throw apiException;
        } catch (Exception exception) {
            throw new ApiException("SYS_500", exception.getMessage(), org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String resolveActor(String fallbackActor) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication == null ? fallbackActor : authentication.getName();
    }
}