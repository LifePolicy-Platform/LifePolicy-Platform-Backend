package maventest.policyapplication.infrastructure.repository.mapper;

import maventest.policyapplication.domain.entity.CallListEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CallListMapper {

    int insertCallList(CallListEntity callListEntity);

    String findMaxListNoByPrefix(String prefix);
}
