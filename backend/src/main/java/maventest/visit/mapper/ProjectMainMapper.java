<<<<<<<< HEAD:backend/src/main/java/maventest/customer/mapper/ProjectMainMapper.java
package maventest.customer.mapper;
========
package maventest.visit.mapper;
>>>>>>>> develop:backend/src/main/java/maventest/visit/mapper/ProjectMainMapper.java

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import maventest.customer.dto.ActiveProjectOptionDto;
<<<<<<<< HEAD:backend/src/main/java/maventest/customer/mapper/ProjectMainMapper.java
import maventest.customer.entity.ProjectMain;
========
import maventest.visit.entity.ProjectMain;
>>>>>>>> develop:backend/src/main/java/maventest/visit/mapper/ProjectMainMapper.java

import java.util.List;

@Mapper
public interface ProjectMainMapper extends BaseMapper<ProjectMain> {

    List<ActiveProjectOptionDto> selectActiveProjects();
}
