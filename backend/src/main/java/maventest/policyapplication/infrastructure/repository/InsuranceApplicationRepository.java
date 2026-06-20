package maventest.policyapplication.infrastructure.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import maventest.policyapplication.domain.entity.InsuredPersonEntity;
import maventest.policyapplication.domain.entity.PolicyApplicationEntity;
import maventest.policyapplication.domain.entity.ProductEntity;

public interface InsuranceApplicationRepository {

    Optional<ProductEntity> findProductByCode(String productCode);

    default List<ProductEntity> findAllProducts() {
        return List.of();
    }

    boolean existsPendingApplication(String applicantIdNo, String insuredIdNo, String productCode);

    default boolean existsPendingApplicationExcluding(String applicationId, String applicantIdNo, String insuredIdNo, String productCode) {
        return existsPendingApplication(applicantIdNo, insuredIdNo, productCode);
    }

    void save(PolicyApplicationEntity policyApplicationEntity, InsuredPersonEntity insuredPersonEntity);

    default Optional<InsuredPersonEntity> findInsuredPersonByApplicationId(String applicationId) {
        return Optional.empty();
    }

    default int updateApplication(PolicyApplicationEntity policyApplicationEntity) {
        return 0;
    }

    default int updateInsuredPerson(InsuredPersonEntity insuredPersonEntity) {
        return 0;
    }

    Optional<PolicyApplicationEntity> findApplicationById(String applicationId);

    int updateApplicationReview(PolicyApplicationEntity policyApplicationEntity);

        long countApplications(
            String applicationId,
            String applicantIdNo,
            String insuredIdNo,
            String applicationStatus,
            String productCode,
            LocalDateTime submissionStartTime,
            LocalDateTime submissionEndTime
        );

    List<Map<String, Object>> findApplications(
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
    );

    List<Map<String, Object>> findIncompleteApplications(List<String> statusList);
}