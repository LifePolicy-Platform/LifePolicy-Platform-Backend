package maventest.visit.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import maventest.visit.dto.CallAppointmentItemDto;
import maventest.visit.dto.PolicyAppointmentContextDto;
import maventest.visit.entity.CallAppointmentEntity;

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
