package maventest.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 個人待辦案件檔（tb_task）
 */
@Data
@TableName("tb_task")
public class Task {

    /** 待辦編號（PK） */
    @TableId(value = "TASK_NO", type = IdType.INPUT)
    private String taskNo;

    /** 待辦類型（POLICY_REVIEW/CLAIM_REVIEW/APPOINTMENT） */
    private String taskType;

    /** 關聯業務單號（POLICY_NO 或 CLAIM_NO 等） */
    private String refNo;

    /** 待辦標題 */
    private String taskTitle;

    /** 待辦狀態（PENDING/PROCESSING/DONE/CANCELLED） */
    private String taskStatus;

    /** 指派處理人（FK: tb_user.USER_ID） */
    private Long assignUserId;

    /** 建立人 */
    private String createUser;

    /** 應完成時間 / SLA 到期時間 */
    private LocalDateTime dueTime;

    /** 完成時間 */
    private LocalDateTime finishTime;

    /** 備註 */
    private String remark;

    /** 建立時間 */
    private LocalDateTime createTime;

    /** 更新時間 */
    private LocalDateTime updateTime;

    /** 更新使用者員編 */
    private String updateUser;
}
