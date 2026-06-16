package maventest.policyapplication.infrastructure.repository.mapper;

import maventest.policyapplication.domain.entity.InsuredPersonEntity;

public interface InsuredPersonMapper {

    int insertInsuredPerson(InsuredPersonEntity insuredPersonEntity);

    InsuredPersonEntity selectByApplicationId(String applicationId);

    int updateByApplicationId(InsuredPersonEntity insuredPersonEntity);
}