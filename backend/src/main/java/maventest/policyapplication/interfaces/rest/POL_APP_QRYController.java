package maventest.policyapplication.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import maventest.code.ApiCode;
import maventest.common.EntityUtil;
import maventest.common.PageResult;
import maventest.common.ReturnMsg;
import maventest.common.exception.ApiException;
import maventest.policyapplication.application.internal.queryservices.POL_APP_QRYQueryService;
import maventest.policyapplication.interfaces.dto.InsuranceApplicationQueryReqDto;
import maventest.policyapplication.interfaces.dto.InsuranceApplicationQueryRespDto;
import maventest.policyapplication.interfaces.dto.MemberProfileRespDto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/insurance/policy-applications")
@Tag(name = "Insurance Policy Application Query", description = "Shared query operations for APPLICANT and REVIEWER")
public class POL_APP_QRYController {

    private final POL_APP_QRYQueryService polAppQryQueryService;

    @PostMapping("/query")
    @Operation(
        summary = "Query life insurance policy applications",
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
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(ApiCode.SYSTEM_ERROR.getCode(), e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/members/by-identity-card")
    @Operation(
            summary = "Lookup active member profile by identity card",
            description = "Returns member name, gender, birthday and contact phone for ACTIVE tb_cust_user.",
            security = {@SecurityRequirement(name = "bearerAuth")}
    )
    public ResponseEntity<ReturnMsg<MemberProfileRespDto>> findMemberByIdentityCard(
            @RequestParam("identityCard") String identityCard
    ) {
        try {
            Optional<MemberProfileRespDto> profile = polAppQryQueryService.findActiveMemberByIdentityCard(identityCard);
            return ResponseEntity.ok(ReturnMsg.success(profile.orElse(null)));
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(ApiCode.SYSTEM_ERROR.getCode(), e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/incomplete")
    @Operation(
        summary = "List incomplete policy applications by role",
        description = "AGENT sees APPLIED/RETURNED, MANAGER sees PENDING, ADMIN sees all incomplete.",
        security = {@SecurityRequirement(name = "bearerAuth")}
    )
    public ResponseEntity<ReturnMsg<List<InsuranceApplicationQueryRespDto>>> listIncomplete() {
        try {
            List<InsuranceApplicationQueryRespDto> result = polAppQryQueryService.findIncompleteApplications();
            return ResponseEntity.ok(ReturnMsg.success(result));
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(ApiCode.SYSTEM_ERROR.getCode(), e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}