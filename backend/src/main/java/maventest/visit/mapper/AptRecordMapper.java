<<<<<<<< HEAD:backend/src/main/java/maventest/appointment/mapper/AptRecordMapper.java
package maventest.appointment.mapper;
========
package maventest.visit.mapper;
>>>>>>>> develop:backend/src/main/java/maventest/visit/mapper/AptRecordMapper.java

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

<<<<<<<< HEAD:backend/src/main/java/maventest/appointment/mapper/AptRecordMapper.java
import maventest.appointment.dto.AptRecordListRequest;
import maventest.appointment.dto.AptRecordListResponse;
import maventest.appointment.entity.AptRecord;
========
import maventest.visit.dto.AptRecordListRequest;
import maventest.visit.dto.AptRecordListResponse;
import maventest.visit.entity.AptRecord;
>>>>>>>> develop:backend/src/main/java/maventest/visit/mapper/AptRecordMapper.java

@Mapper
public interface AptRecordMapper extends BaseMapper<AptRecord> {

    List<AptRecordListResponse> selectAptRecordList(@Param("request") AptRecordListRequest request);

    List<AptRecordListResponse> selectAptRecordHistoryByIdentityCard(@Param("identityCard") String identityCard);
}
