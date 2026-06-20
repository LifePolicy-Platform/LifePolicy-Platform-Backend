package maventest.policyapplication.infrastructure.repository.mapper;

import java.util.List;
import java.util.Map;

import maventest.policyapplication.domain.entity.PolicyApplicationEntity;

public interface PolicyApplicationMapper {

    int countPendingApplication(Map<String, Object> paramMap);

    int insertPolicyApplication(PolicyApplicationEntity policyApplicationEntity);

    PolicyApplicationEntity selectByApplicationId(String applicationId);

    int updatePendingApplication(PolicyApplicationEntity policyApplicationEntity);

    int updateApplicationReview(PolicyApplicationEntity policyApplicationEntity);

    long countApplications(Map<String, Object> paramMap);

    List<Map<String, Object>> selectApplications(Map<String, Object> paramMap);

    List<Map<String, Object>> selectIncompleteApplications(Map<String, Object> paramMap);
}