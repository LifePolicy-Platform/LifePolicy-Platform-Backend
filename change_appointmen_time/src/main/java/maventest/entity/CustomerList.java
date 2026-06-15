package maventest.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 客戶名單主檔（tbca001）
 */
@Data
@TableName("tbca001")
public class CustomerList {

    /** 名單序號（PK） */
    @TableId(value = "LIST_NO", type = IdType.INPUT)
    private String listNo;

    /** 客戶編號（FK: tbca008.CUST_NO） */
    private String custNo;

    /** 名單電話 */
    private Integer listLastphone;

    /**
     * 名單狀態
     * 0: 待處理、1: 已約訪、2: 已結案
     */
    private Integer listStatus;

    /** 建立時間 */
    private LocalDateTime createTime;

    /** 更新時間 */
    private LocalDateTime updateTime;
}
