package maventest.service.impl;

import maventest.entity.AppUserEntity;
import maventest.mapper.AppUserMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AppUserRepository {

    private final AppUserMapper appUserMapper;

    public Optional<AppUserEntity> findByUsername(String username) {
        String sql = """
                SELECT USER_ID, USERNAME, PASSWORD, DISPLAY_NAME, ROLE_CODE, STATUS,
                       LAST_LOGIN_TIME, CREATE_TIME, UPDATE_TIME
                FROM TB_USER
                WHERE USERNAME = ?
                """;

        return jdbcTemplate.query(sql, (ResultSetExtractor<Optional<AppUserEntity>>) this::mapUser, username);
    }

    private Optional<AppUserEntity> mapUser(ResultSet rs) throws SQLException {
        if (!rs.next()) {
            return Optional.empty();
        }
        return Optional.of(AppUserEntity.builder()
                .id(rs.getLong("USER_ID"))
                .username(rs.getString("USERNAME"))
                .password(rs.getString("PASSWORD"))
                .displayName(rs.getString("DISPLAY_NAME"))
                .roleCode(rs.getString("ROLE_CODE"))
                .status(rs.getString("STATUS"))
                .lastLoginTime(rs.getTimestamp("LAST_LOGIN_TIME") != null ? rs.getTimestamp("LAST_LOGIN_TIME").toLocalDateTime() : null)
                .createTime(rs.getTimestamp("CREATE_TIME") != null ? rs.getTimestamp("CREATE_TIME").toLocalDateTime() : null)
                .updateTime(rs.getTimestamp("UPDATE_TIME") != null ? rs.getTimestamp("UPDATE_TIME").toLocalDateTime() : null)
                .build());
    }

    public Long Save(AppUserEntity user) {
    public Long Save(AppUserEntity user) {
        appUserMapper.insert(user);
        return user.getId();
    }

    public boolean existsByUserName(String username) {
    public boolean existsByUserName(String username) {
        return appUserMapper.existsByUserName(username);
    }

    public List<AppUserEntity> findAll() {
    public List<AppUserEntity> findAll() {
        return appUserMapper.findAllUser();
    }

    public void updateByUserName(AppUserEntity user) {
        appUserMapper.updateByUserName(user);
    }

    public void deleteUser(String username) {
        appUserMapper.deleteUserByUsername(username);
    }
}

