package maventest.policyapplication.application.internal.queryservices;


import lombok.RequiredArgsConstructor;
import maventest.code.ApiCode;
import maventest.common.PageResult;
import maventest.common.exception.ErrorInputException;
import maventest.policyapplication.infrastructure.repository.InsuranceApplicationRepository;
import maventest.policyapplication.interfaces.dto.InsuranceApplicationQueryReqDto;
import maventest.policyapplication.interfaces.dto.InsuranceApplicationQueryRespDto;
import maventest.policyapplication.interfaces.transform.InsuranceApplicationConverter;

import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.List;
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

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}