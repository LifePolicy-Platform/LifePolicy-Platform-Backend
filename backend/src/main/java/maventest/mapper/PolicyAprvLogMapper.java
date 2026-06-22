package maventest.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import maventest.dto.PolicyAprvLogList;
import maventest.entity.PolicyAprvLogEntity;

@Mapper
public interface PolicyAprvLogMapper extends BaseMapper<PolicyAprvLogEntity> {

    List<PolicyAprvLogList> selectByPolicyNo(@Param("policyNo") String policyNo);
}
