package maventest.visit.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import maventest.visit.dto.AptRecordListRequest;
import maventest.visit.dto.AptRecordListResponse;
import maventest.visit.entity.AptRecord;

@Mapper
public interface AptRecordMapper extends BaseMapper<AptRecord> {

    
    List<AptRecordListResponse> selectAptRecordList(@Param("request") AptRecordListRequest request);

    List<AptRecordListResponse> selectAptRecordHistoryByCustName(@Param("custName") String custName);
}
