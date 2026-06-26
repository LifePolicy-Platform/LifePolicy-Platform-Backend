package maventest.visit.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import maventest.customer.dto.ActiveProjectOptionDto;
import maventest.visit.entity.ProjectMain;

import java.util.List;

@Mapper
public interface ProjectMainMapper extends BaseMapper<ProjectMain> {

    List<ActiveProjectOptionDto> selectActiveProjects();
}
