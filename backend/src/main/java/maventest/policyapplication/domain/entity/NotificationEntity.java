package maventest.policyapplication.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@TableName("tb_notification")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEntity {

    @TableId(value = "NOTIF_NO", type = IdType.AUTO)
    private Long notifNo;

    @TableField("NOTIF_TYPE")
    private String notifType;

    @TableField("TITLE")
    private String title;

    @TableField("CONTENT")
    private String content;

    @TableField("TARGET_TYPE")
    private String targetType;

    @TableField("MEMBER_ID")
    private Long memberId;

    @TableField("REF_NO")
    private String refNo;

    @TableField("RECIPIENT_USERNAME")
    private String recipientUsername;

    @TableField("STATUS")
    private String status;

    @TableField("IS_READ")
    private Integer isRead;

    @TableField("READ_TIME")
    private LocalDateTime readTime;

    @TableField("SEND_TIME")
    private LocalDateTime sendTime;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("CREATE_USER")
    private String createUser;
}