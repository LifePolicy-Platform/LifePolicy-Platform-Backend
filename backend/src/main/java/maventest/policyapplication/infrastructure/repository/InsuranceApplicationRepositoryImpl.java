package maventest.policyapplication.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import maventest.policyapplication.domain.entity.InsuredPersonEntity;
import maventest.policyapplication.domain.entity.PolicyApplicationEntity;
import maventest.policyapplication.domain.entity.ProductEntity;
import maventest.policyapplication.infrastructure.repository.mapper.InsuredPersonMapper;
import maventest.policyapplication.infrastructure.repository.mapper.PolicyApplicationMapper;
import maventest.policyapplication.infrastructure.repository.mapper.ProductMapper;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class InsuranceApplicationRepositoryImpl implements InsuranceApplicationRepository {

    private final ProductMapper productMapper;
    private final PolicyApplicationMapper policyApplicationMapper;
    private final InsuredPersonMapper insuredPersonMapper;

    @Override
    public Optional<ProductEntity> findProductByCode(String productCode) {
        return Optional.ofNullable(productMapper.findByCode(productCode));
    }

    @Override
    public List<ProductEntity> findAllProducts() {
        return productMapper.findAll();
    }

    @Override
    public boolean existsPendingApplication(String applicantIdNo, String insuredIdNo, String productCode) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("applicationId", null);
        paramMap.put("applicantIdNo", applicantIdNo);
        paramMap.put("insuredIdNo", insuredIdNo);
        paramMap.put("productCode", productCode);
        return policyApplicationMapper.countPendingApplication(paramMap) > 0;
    }

    @Override
    public boolean existsPendingApplicationExcluding(String applicationId, String applicantIdNo, String insuredIdNo, String productCode) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("applicationId", applicationId);
        paramMap.put("applicantIdNo", applicantIdNo);
        paramMap.put("insuredIdNo", insuredIdNo);
        paramMap.put("productCode", productCode);
        return policyApplicationMapper.countPendingApplication(paramMap) > 0;
    }

    @Override
    public void save(PolicyApplicationEntity policyApplicationEntity, InsuredPersonEntity insuredPersonEntity) {
        policyApplicationMapper.insertPolicyApplication(policyApplicationEntity);
        insuredPersonMapper.insertInsuredPerson(insuredPersonEntity);
    }

    @Override
    public Optional<InsuredPersonEntity> findInsuredPersonByApplicationId(String applicationId) {
        return Optional.ofNullable(insuredPersonMapper.selectByApplicationId(applicationId));
    }

    @Override
    public int updateApplication(PolicyApplicationEntity policyApplicationEntity) {
        return policyApplicationMapper.updatePendingApplication(policyApplicationEntity);
    }

    @Override
    public int updateInsuredPerson(InsuredPersonEntity insuredPersonEntity) {
        return insuredPersonMapper.updateByApplicationId(insuredPersonEntity);
    }

    @Override
    public Optional<PolicyApplicationEntity> findApplicationById(String applicationId) {
        return Optional.ofNullable(policyApplicationMapper.selectByApplicationId(applicationId));
    }

    @Override
    public int updateApplicationReview(PolicyApplicationEntity policyApplicationEntity) {
        return policyApplicationMapper.updateApplicationReview(policyApplicationEntity);
    }

    @Override
    public long countApplications(
            String applicationId,
            String applicantIdNo,
            String insuredIdNo,
            String applicationStatus,
            String productCode,
            LocalDateTime submissionStartTime,
            LocalDateTime submissionEndTime
    ) {
        Map<String, Object> paramMap = buildQueryParamMap(
                applicationId,
                applicantIdNo,
                insuredIdNo,
                applicationStatus,
                productCode,
                submissionStartTime,
                submissionEndTime,
                null,
                null,
                null
        );
        return policyApplicationMapper.countApplications(paramMap);
    }

    @Override
    public List<Map<String, Object>> findApplications(
            String applicationId,
            String applicantIdNo,
            String insuredIdNo,
            String applicationStatus,
            String productCode,
            LocalDateTime submissionStartTime,
            LocalDateTime submissionEndTime,
            Integer offset,
            Integer pageSize,
            String sortDirection
    ) {
        Map<String, Object> paramMap = buildQueryParamMap(
                applicationId,
                applicantIdNo,
                insuredIdNo,
                applicationStatus,
                productCode,
                submissionStartTime,
                submissionEndTime,
                offset,
                pageSize,
                sortDirection
        );
        return policyApplicationMapper.selectApplications(paramMap);
    }

    @Override
    public List<Map<String, Object>> findIncompleteApplications(List<String> statusList) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("statusList", statusList);
        return policyApplicationMapper.selectIncompleteApplications(paramMap);
    }

    private Map<String, Object> buildQueryParamMap(
            String applicationId,
            String applicantIdNo,
            String insuredIdNo,
            String applicationStatus,
            String productCode,
            LocalDateTime submissionStartTime,
            LocalDateTime submissionEndTime,
            Integer offset,
            Integer pageSize,
            String sortDirection
    ) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("applicationId", applicationId);
        paramMap.put("applicantIdNo", applicantIdNo);
        paramMap.put("insuredIdNo", insuredIdNo);
        paramMap.put("applicationStatus", applicationStatus);
        paramMap.put("productCode", productCode);
        paramMap.put("submissionStartTime", submissionStartTime);
        paramMap.put("submissionEndTime", submissionEndTime);
        paramMap.put("offset", offset);
        paramMap.put("pageSize", pageSize);
        paramMap.put("sortDirection", sortDirection);
        return paramMap;
    }
}