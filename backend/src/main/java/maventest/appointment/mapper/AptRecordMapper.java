package maventest.appointment.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import maventest.appointment.dto.AptRecordListRequest;
import maventest.appointment.dto.AptRecordListResponse;
import maventest.appointment.entity.AptRecord;

@Mapper
public interface AptRecordMapper extends BaseMapper<AptRecord> {

    List<AptRecordListResponse> selectAptRecordList(@Param("request") AptRecordListRequest request);

    List<AptRecordListResponse> selectAptRecordHistoryByCustName(@Param("custName") String custName);
}
