package maventest.policyapplication.application.internal.queryservices;


import lombok.RequiredArgsConstructor;
import maventest.code.ApiCode;
import maventest.common.PageResult;
import maventest.common.exception.ApiException;
import maventest.common.exception.ErrorInputException;
import maventest.policyapplication.infrastructure.repository.InsuranceApplicationRepository;
import maventest.policyapplication.interfaces.dto.InsuranceApplicationQueryReqDto;
import maventest.policyapplication.interfaces.dto.InsuranceApplicationQueryRespDto;
import maventest.policyapplication.interfaces.transform.InsuranceApplicationConverter;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class POL_APP_QRYQueryService {

    private final InsuranceApplicationRepository insuranceApplicationRepository;
    private final InsuranceApplicationConverter insuranceApplicationConverter;

        public PageResult<InsuranceApplicationQueryRespDto> queryApplications(InsuranceApplicationQueryReqDto reqDto) {
        validateQueryCondition(reqDto);
        String sortDirection = normalizeSortDirection(reqDto.getSortDirection());

        long totalCount = insuranceApplicationRepository.countApplications(
            reqDto.getApplicationId(),
            reqDto.getApplicantIdNo(),
            reqDto.getInsuredIdNo(),
            reqDto.getApplicationStatus(),
            reqDto.getProductCode(),
            reqDto.getSubmissionStartTime(),
            reqDto.getSubmissionEndTime()
        );

        List<Map<String, Object>> result = insuranceApplicationRepository.findApplications(
                reqDto.getApplicationId(),
                reqDto.getApplicantIdNo(),
                reqDto.getInsuredIdNo(),
                reqDto.getApplicationStatus(),
            reqDto.getProductCode(),
            reqDto.getSubmissionStartTime(),
            reqDto.getSubmissionEndTime(),
            (reqDto.getPageNo() - 1) * reqDto.getPageSize(),
            reqDto.getPageSize(),
            sortDirection
        );

        return PageResult.<InsuranceApplicationQueryRespDto>builder()
            .pageNo(reqDto.getPageNo())
            .pageSize(reqDto.getPageSize())
            .totalCount(totalCount)
            .totalPages((int) Math.ceil(totalCount / (double) reqDto.getPageSize()))
            .records(insuranceApplicationConverter.toQueryRespDtos(result))
            .build();
    }

    private void validateQueryCondition(InsuranceApplicationQueryReqDto reqDto) {
        if (isBlank(reqDto.getApplicationId())
                && isBlank(reqDto.getApplicantIdNo())
                && isBlank(reqDto.getInsuredIdNo())
                && isBlank(reqDto.getApplicationStatus())
                && isBlank(reqDto.getProductCode())
                && reqDto.getSubmissionStartTime() == null
                && reqDto.getSubmissionEndTime() == null) {
            throw new ErrorInputException(ApiCode.QUERY_CONDITION_REQUIRED.getCode(), ApiCode.QUERY_CONDITION_REQUIRED.getMessage());
        }
        if (reqDto.getSubmissionStartTime() != null
                && reqDto.getSubmissionEndTime() != null
                && reqDto.getSubmissionStartTime().isAfter(reqDto.getSubmissionEndTime())) {
            throw new ErrorInputException(ApiCode.INPUT_INVALID.getCode(), "submissionStartTime must be earlier than or equal to submissionEndTime");
        }
    }

    private String normalizeSortDirection(String sortDirection) {
        String normalized = sortDirection == null ? "DESC" : sortDirection.trim().toUpperCase(Locale.ROOT);
        if (!"ASC".equals(normalized) && !"DESC".equals(normalized)) {
            throw new ErrorInputException(ApiCode.SORT_DIRECTION_INVALID.getCode(), ApiCode.SORT_DIRECTION_INVALID.getMessage());
        }
        return normalized;
    }

    public List<InsuranceApplicationQueryRespDto> findIncompleteApplications() {
        List<String> statusList = resolveIncompleteStatusList();
        List<Map<String, Object>> rows = insuranceApplicationRepository.findIncompleteApplications(statusList);
        return insuranceApplicationConverter.toQueryRespDtos(rows);
    }

    private List<String> resolveIncompleteStatusList() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new ApiException(ApiCode.AUTHENTICATION_REQUIRED.getCode(),
                    ApiCode.AUTHENTICATION_REQUIRED.getMessage(), HttpStatus.UNAUTHORIZED);
        }
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        if (hasRole(authorities, "ROLE_ADMIN")) {
            return List.of("APPLIED", "RETURNED", "PENDING");
        }
        if (hasRole(authorities, "ROLE_MANAGER")) {
            return List.of("PENDING");
        }
        if (hasRole(authorities, "ROLE_AGENT")) {
            return List.of("APPLIED", "RETURNED");
        }
        throw new ApiException(ApiCode.ACCESS_DENIED.getCode(),
                ApiCode.ACCESS_DENIED.getMessage(), HttpStatus.FORBIDDEN);
    }

    private boolean hasRole(Collection<? extends GrantedAuthority> authorities, String role) {
        return authorities.stream().anyMatch(a -> role.equals(a.getAuthority()));
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}