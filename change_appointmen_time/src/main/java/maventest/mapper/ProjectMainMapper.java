package maventest.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import maventest.entity.ProjectMain;

@Mapper
public interface ProjectMainMapper extends BaseMapper<ProjectMain> {
}
