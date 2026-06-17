package maventest.entity;

import java.time.LocalDate;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 專案主檔（tb_camp_mst）
 */
@Data
@TableName("tb_camp_mst")
public class ProjectMain {

    /** 活動類型代碼（PK） */
    @TableId(value = "CAMP_CODE", type = IdType.INPUT)
    private String campCode;

    /** 專案名稱 */
    private String campName;

    /** 專案名單最後可處理日 / 服務截止日 */
    private LocalDate campServiceDt;
}
