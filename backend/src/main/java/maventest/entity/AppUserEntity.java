package maventest.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    @TableField("USERNAME")
    private String username;

    /** BCrypt 加密密碼 */
    @TableField("PASSWORD")
    private String password;

    /** 顯示姓名 */
    @TableField("DISPLAY_NAME")
    private String displayName;

    /** 角色代碼（ADMIN/SALES/UNDERWRITER 等） */
    @TableField("ROLE_CODE")
    private String roleCode;

    /** 帳號狀態（ACTIVE/INACTIVE 或 1/0） */
    @TableField("STATUS")
    private String status;

    /** 最後登入時間 */
    @TableField("LAST_LOGIN_TIME")
    private LocalDateTime lastLoginTime;

    /** 建立時間 */
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    /** 更新時間 */
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    public Long getId() {
        return userId;
    }

    public String getPasswordHash() {
        return password;
    }

    public boolean isEnabled() {
        if (status == null || status.isBlank()) {
            // 換 DB 後常見 STATUS 未回填，避免全部被判定為停用
            return true;
        }
        String normalized = status.trim();
        if ("INACTIVE".equalsIgnoreCase(normalized)
                || "DISABLED".equalsIgnoreCase(normalized)
                || "0".equals(normalized)
                || "N".equalsIgnoreCase(normalized)
                || "FALSE".equalsIgnoreCase(normalized)) {
            return false;
        }
        return "ACTIVE".equalsIgnoreCase(normalized)
                || "1".equals(normalized)
                || "Y".equalsIgnoreCase(normalized)
                || "TRUE".equalsIgnoreCase(normalized)
                || "ENABLED".equalsIgnoreCase(normalized);
    }

    public List<String> getRoles() {
        return roleCode == null ? List.of() : List.of(roleCode);
    }
}
