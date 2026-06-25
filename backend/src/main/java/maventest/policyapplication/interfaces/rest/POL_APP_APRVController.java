package maventest.policyapplication.interfaces.rest;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import maventest.common.ApiCode;
import maventest.common.EntityUtil;
import maventest.common.ReturnMsg;
import maventest.common.exception.ApiException;
import maventest.common.security.SecurityContextRoleResolver;
import maventest.policyapplication.application.internal.commandservices.POL_APP_APRVCommandService;
import maventest.policyapplication.interfaces.dto.InsuranceApplicationReviewCommandReqDto;
import maventest.policyapplication.interfaces.dto.InsuranceApplicationReviewCommandRespDto;

import org.springframework.http.HttpStatus;
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
@Tag(
        name = "Insurance Policy Application Review",
        description = "APPLICANT reviews SUBMIT stage; REVIEWER reviews PENDING stage"
)
public class POL_APP_APRVController {

    private final POL_APP_APRVCommandService polAppAprvCommandService;

    @PostMapping("/review")
        @Operation(
            summary = "Review life insurance policy application",
            description = "APPLICANT may review SUBMIT applications. REVIEWER may review PENDING applications.",
            security = {@SecurityRequirement(name = "bearerAuth")}
        )
    public ResponseEntity<ReturnMsg<InsuranceApplicationReviewCommandRespDto>> reviewApplication(
            @Valid @RequestBody InsuranceApplicationReviewCommandReqDto reqDto,
            BindingResult bindingResult
    ) {
        try {
            EntityUtil.validDto(bindingResult);
            String actor = resolveActor(reqDto.getReviewedBy());
            String roleCode = SecurityContextRoleResolver.resolveRoleCode();
            InsuranceApplicationReviewCommandRespDto responseDto =
                    polAppAprvCommandService.reviewApplication(reqDto, actor, roleCode);
            return ResponseEntity.ok(ReturnMsg.success(responseDto));
        } catch (ApiException apiException) {
            throw apiException;
        } catch (Exception exception) {
            throw new ApiException(ApiCode.SYSTEM_ERROR.getCode(), exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String resolveActor(String fallbackActor) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return fallbackActor;
        }
        String name = authentication.getName();
        if (name == null || name.isBlank() || "anonymousUser".equals(name)) {
            return fallbackActor;
        }
        return name;
    }
}
