package maventest.policyapplication.interfaces.rest;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import maventest.common.EntityUtil;
import maventest.common.PageResult;
import maventest.common.ReturnMsg;
import maventest.common.exception.ApiException;
import maventest.policyapplication.application.internal.queryservices.POL_APP_QRYQueryService;
import maventest.policyapplication.interfaces.dto.InsuranceApplicationQueryReqDto;
import maventest.policyapplication.interfaces.dto.InsuranceApplicationQueryRespDto;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/insurance/policy-applications")
@Tag(name = "Insurance Policy Application Query", description = "Shared query operations for APPLICANT and REVIEWER")
public class POL_APP_QRYController {

    private final POL_APP_QRYQueryService polAppQryQueryService;

    @PostMapping("/query")
        @Operation(
            summary = "Query life insurance policy applications",
            description = "Requires APPLICANT or REVIEWER role. Query results are used by the workbench to unlock edit or review flows.",
            security = {@SecurityRequirement(name = "bearerAuth")}
        )
    public ResponseEntity<ReturnMsg<PageResult<InsuranceApplicationQueryRespDto>>> queryApplications(
            @Valid @RequestBody InsuranceApplicationQueryReqDto reqDto,
            BindingResult bindingResult
    ) {
        try {
            EntityUtil.validDto(bindingResult);
            PageResult<InsuranceApplicationQueryRespDto> response = polAppQryQueryService.queryApplications(reqDto);
            return ResponseEntity.ok(ReturnMsg.success(response));
        } catch (ApiException apiException) {
            throw apiException;
        } catch (Exception exception) {
            throw new ApiException("SYS_500", exception.getMessage(), org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}