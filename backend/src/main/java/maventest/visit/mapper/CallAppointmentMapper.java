<<<<<<<< HEAD:backend/src/main/java/maventest/appointment/mapper/CallAppointmentMapper.java
package maventest.appointment.mapper;
========
package maventest.visit.mapper;
>>>>>>>> develop:backend/src/main/java/maventest/visit/mapper/CallAppointmentMapper.java

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

<<<<<<<< HEAD:backend/src/main/java/maventest/appointment/mapper/CallAppointmentMapper.java
import maventest.appointment.dto.CallAppointmentItemDto;
import maventest.appointment.dto.PolicyAppointmentContextDto;
import maventest.appointment.entity.CallAppointmentEntity;
========
import maventest.visit.dto.CallAppointmentItemDto;
import maventest.visit.dto.PolicyAppointmentContextDto;
import maventest.visit.entity.CallAppointmentEntity;
>>>>>>>> develop:backend/src/main/java/maventest/visit/mapper/CallAppointmentMapper.java

@Mapper
public interface CallAppointmentMapper {

    PolicyAppointmentContextDto selectAppointmentContextByPolicyNo(@Param("policyNo") String policyNo);

    List<CallAppointmentItemDto> selectByListNo(@Param("listNo") String listNo);

    String findMaxRecNoByPrefix(@Param("prefix") String prefix);

    CallAppointmentEntity selectPendingByListNo(@Param("listNo") String listNo);

    int insertCallAppointment(CallAppointmentEntity entity);

    int confirmAppointmentResult(
            @Param("sno") Long sno,
            @Param("recallResult") int recallResult,
            @Param("recTime") java.time.LocalDateTime recTime,
            @Param("updateUser") String updateUser);
}
