package maventest.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 電話聯絡名單檔（tb_call_list）
 */
@Data
@TableName("tb_call_list")
public class CustomerList {

    /** 名單序號（PK） */
    @TableId(value = "LIST_NO", type = IdType.INPUT)
    private String listNo;

    /** 客戶編號（FK: tb_cust_user.MEMBER_ID） */
    private Long memberId;

    /** 撥出電話 */
    private String listLastphone;

    /**
     * 名單狀態
     * 0: 未處理、1: 已約訪、2: 已結案
     */
    private Integer listStatus;

    /** 建立時間 */
    private LocalDateTime createTime;

    /** 更新時間 */
    private LocalDateTime updateTime;
}
