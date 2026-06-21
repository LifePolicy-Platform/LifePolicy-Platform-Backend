package maventest.policyapplication.infrastructure.repository.mapper;

import maventest.policyapplication.domain.entity.CallListEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CallListMapper {

    int insertCallList(CallListEntity callListEntity);

    String findMaxListNoByPrefix(String prefix);

    int updateListStatus(@Param("listNo") String listNo, @Param("listStatus") int listStatus);
}
