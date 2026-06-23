package maventest.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;

import maventest.entity.AppUserEntity;

public interface AppUserMapper {
    boolean existsByUserName(@Param("username") String username);
    Long insert(AppUserEntity user);
    List<AppUserEntity> findAllUser();
    List<AppUserEntity> findByRoleCode(@Param("roleCode") String roleCode);
    AppUserEntity findById(@Param("id") Long id);
    void updateByUserName(AppUserEntity user);
    void deleteUserByUsername(@Param("username") String username);
}