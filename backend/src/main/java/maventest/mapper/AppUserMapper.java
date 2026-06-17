package maventest.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import maventest.entity.AppUserEntity;

public interface AppUserMapper {

    Long insert(AppUserEntity user);

    boolean existsByUserName(@Param("username") String username);

    AppUserEntity findByUsername(@Param("username") String username);

    List<AppUserEntity> findAllUser();

    void updateByUserName(AppUserEntity user);

    void deleteUserByUsername(@Param("username") String username);
}
