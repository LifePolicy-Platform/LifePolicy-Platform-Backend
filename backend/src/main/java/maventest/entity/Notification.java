package maventest.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 推播通知檔（tb_notification）
 */
@Data
@TableName("tb_notification")
public class Notification {

    /** 推播通知流水號 ID（PK） */
    @TableId(value = "NOTIF_NO", type = IdType.AUTO)
    private Long notifNo;

    /** 通知類型（POLICY/CLAIM/APPOINTMENT/SYSTEM） */
    private String notifType;

    /** 標題 */
    private String title;

    /** 內文 */
    private String content;

    /** 目標類型（ALL/SPECIFIC） */
    private String targetType;

    /** 指定接收的前台保戶（FK: tb_cust_user.MEMBER_ID） */
    private Long memberId;

    /** 狀態（DRAFT/SENT） */
    private String status;

    /** 是否已讀：0=未讀，1=已讀 */
    private Integer isRead;

    /** 保戶點開已讀的時間 */
    private LocalDateTime readTime;

    /** 實際發送時間 */
    private LocalDateTime sendTime;

    /** 建立時間 */
    private LocalDateTime createTime;

    /** 建立此推播的人員或系統代碼 */
    private String createUser;
}
