package maventest.service.impl;

import maventest.code.ApiCode;
import maventest.common.exception.ApiException;
import maventest.entity.AppUserEntity;
import maventest.mapper.AppUserMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AppUserRepository {

    private final JdbcTemplate jdbcTemplate;
    private final AppUserMapper appUserMapper;

    public Optional<AppUserEntity> findByUsername(String username) {
        String sql = """
                SELECT u.id,
                       u.username,
                       u.password_hash,
                       u.display_name,
                       u.enabled,
                       ur.role_name
                FROM app_user u
                LEFT JOIN user_role ur ON u.id = ur.user_id
                WHERE u.username = ?
                ORDER BY ur.role_name
                """;

        return jdbcTemplate.query(sql, (ResultSetExtractor<Optional<AppUserEntity>>) this::mapUser, username);
    }

    private Optional<AppUserEntity> mapUser(ResultSet resultSet) throws SQLException {
        AppUserEntity.AppUserEntityBuilder builder = null;
        List<String> roles = new ArrayList<>();

        while (resultSet.next()) {
            if (builder == null) {
                builder = AppUserEntity.builder()
                        .id(resultSet.getLong("id"))
                        .username(resultSet.getString("username"))
                        .passwordHash(resultSet.getString("password_hash"))
                        .displayName(resultSet.getString("display_name"))
                        .enabled(resultSet.getBoolean("enabled"));
            }
            String roleName = resultSet.getString("role_name");
            if (roleName != null) {
                roles.add(roleName);
            }
        }

        if (builder == null) {
            return Optional.empty();
        }

        return Optional.of(builder.roles(List.copyOf(roles)).build());
    }

    public Long Save(AppUserEntity user){
        appUserMapper.insert(user);
        return user.getId();
    }

    public void Saverole(Long userId, String roleName){
        appUserMapper.insertRole(userId, roleName);
    }

     public boolean existsByUserName(String username) {
        return appUserMapper.existsByUserName(username);
    }

    public List<AppUserEntity> findAll(){
        return appUserMapper.findAllUser();
    }

    public void updateByUserName(AppUserEntity user) {
        appUserMapper.updateByUserName(user);
    }

    public void delectUserRole(String username){
        appUserMapper.delectUserRole(username);
    }

    public void saveRoleByUserName(String username, String roleName){
        AppUserEntity user = findByUsername(username)
            .orElseThrow(() -> new ApiException(ApiCode.APPLICATION_NOT_FOUND.getCode(), 
                                                 ApiCode.APPLICATION_NOT_FOUND.getMessage(), 
                                                 HttpStatus.NOT_FOUND));
        appUserMapper.insertRole(user.getId(), roleName);
    }

    public void deleteUser(String username) {
        appUserMapper.deleteUserByUsername(username);
    }
}