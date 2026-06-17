package maventest.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 系統使用者 — 後台人員（tb_user）
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_user")
public class AppUserEntity {

    /** 後台人員流水號 ID（PK） */
    @TableId(value = "USER_ID", type = IdType.AUTO)
    private Long userId;

    /** 登入帳號 */
    private String username;

    /** BCrypt 加密密碼 */
    private String password;

    /** 顯示姓名 */
    private String displayName;

    /** 角色代碼（ADMIN/SALES/UNDERWRITER 等） */
    private String roleCode;

    /** 帳號狀態（ACTIVE/INACTIVE 等） */
    private String status;

    /** 最後登入時間 */
    private LocalDateTime lastLoginTime;

    /** 建立時間 */
    private LocalDateTime createTime;

    /** 更新時間 */
    private LocalDateTime updateTime;

    public Long getId() {
        return userId;
    }

    public String getPasswordHash() {
        return password;
    }

    public boolean isEnabled() {
        return "ACTIVE".equalsIgnoreCase(status);
    }

    public List<String> getRoles() {
        return roleCode == null ? List.of() : List.of(roleCode);
    }
}
