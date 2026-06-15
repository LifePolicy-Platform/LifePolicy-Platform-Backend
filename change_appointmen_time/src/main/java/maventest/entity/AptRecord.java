package maventest.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;


@Data
@TableName("tbca003")
public class AptRecord {

    /** 流水號（PK，自動遞增） */
    @TableId(value = "SNO", type = IdType.AUTO)
    private Long sno;

    /** 約訪紀錄序號 */
    private String recNo;

    /** 名單序號 */
    private String listNo;

    /** 專案代碼 */
    private String listCampcode;

    /** 預定約訪時間（yyyy-MM-dd HH:mm:ss） */
    private LocalDateTime recallTime;

    /** 實際約訪時間（完成後才寫入，未完成為 null） */
    private LocalDateTime recTime;

    /** 更新時間 */
    private LocalDateTime updateTime;

    /** 更新使用者帳號 */
    private String updateUser;
}
