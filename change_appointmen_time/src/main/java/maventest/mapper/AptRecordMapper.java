package maventest.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import maventest.dto.AptRecordListRequest;
import maventest.dto.AptRecordListResponse;
import maventest.entity.AptRecord;

@Mapper
public interface AptRecordMapper extends BaseMapper<AptRecord> {

    
    List<AptRecordListResponse> selectAptRecordList(@Param("request") AptRecordListRequest request);
}
