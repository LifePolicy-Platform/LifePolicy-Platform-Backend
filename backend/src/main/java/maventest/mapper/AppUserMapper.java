package maventest.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;

import maventest.entity.AppUserEntity;

public interface AppUserMapper {
    boolean existsByUserName(@Param("username") String username);
    Long insert(AppUserEntity user);
    void insertRole(@Param("userId") Long userId, @Param("roleName") String roleName);
    List<AppUserEntity> findAllUser();
    void updateByUserName(AppUserEntity user);
    void delectUserRole(@Param("username") String username);
    void deleteUserByUsername(@Param("username") String username);
} 