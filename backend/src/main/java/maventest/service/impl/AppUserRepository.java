package maventest.service.impl;

import maventest.code.ApiCode;
import maventest.common.exception.ApiException;
import maventest.entity.AppUserEntity;
import maventest.mapper.AppUserMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AppUserRepository {

    private final AppUserMapper appUserMapper;

    public Optional<AppUserEntity> findByUsername(String username) {
        AppUserEntity user = appUserMapper.findByUsername(username);
        return Optional.ofNullable(user);
    }

    public Long Save(AppUserEntity user) {
        appUserMapper.insert(user);
        return user.getUserId();
    }

    public boolean existsByUserName(String username) {
        return appUserMapper.existsByUserName(username);
    }

    public List<AppUserEntity> findAll() {
        return appUserMapper.findAllUser();
    }

    public void updateByUserName(AppUserEntity user) {
        appUserMapper.updateByUserName(user);
    }

    public void saveRoleByUserName(String username, String roleName) {
        AppUserEntity user = findByUsername(username)
            .orElseThrow(() -> new ApiException(ApiCode.APPLICATION_NOT_FOUND.getCode(),
                                                 ApiCode.APPLICATION_NOT_FOUND.getMessage(),
                                                 HttpStatus.NOT_FOUND));
        AppUserEntity updateUser = AppUserEntity.builder()
                .username(username)
                .password(user.getPassword())
                .displayName(user.getDisplayName())
                .status(user.getStatus())
                .roleCode(roleName)
                .build();
        appUserMapper.updateByUserName(updateUser);
    }

    public void deleteUser(String username) {
        appUserMapper.deleteUserByUsername(username);
    }
}
