package maventest.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 約訪記錄檔（tb_call_appointment）
 */
@Data
@TableName("tb_call_appointment")
public class AptRecord {

    /** 流水號（PK，自動遞增） */
    @TableId(value = "SNO", type = IdType.AUTO)
    private Integer sno;

    /** 約訪記錄序號 */
    private String recNo;

    /** 名單序號（FK: tb_call_list.LIST_NO） */
    private String listNo;

    /** 專案代碼（FK: tb_camp_mst.CAMP_CODE） */
    private String campCode;

    /** 約訪日期時間 */
    private LocalDateTime recallTime;

    /** 實際約訪時間（未完成為 null） */
    private LocalDateTime recTime;

    /** 建立時間 */
    private LocalDateTime createTime;

    /** 更新時間 */
    private LocalDateTime updateTime;

    /** 更新使用者員編 */
    private String updateUser;
}
