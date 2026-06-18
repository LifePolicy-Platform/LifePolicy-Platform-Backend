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
    private String username;
    private String password;
    private String displayName;
    private String roleCode;
    private String status;
    private LocalDateTime lastLoginTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}