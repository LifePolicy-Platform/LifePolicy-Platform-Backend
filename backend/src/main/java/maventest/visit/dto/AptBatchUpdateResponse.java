<<<<<<<< HEAD:backend/src/main/java/maventest/appointment/dto/AptBatchUpdateResponse.java
package maventest.appointment.dto;
========
package maventest.visit.dto;
>>>>>>>> develop:backend/src/main/java/maventest/visit/dto/AptBatchUpdateResponse.java

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class AptBatchUpdateResponse {

    /**
     * 流水號（對應前端表格列）
     */
    private Long sno;

    /**
     * success / fail
     */
    private String result;

    /**
     * 失敗原因，成功時為 null
     */
    private String errorMsg;

    /**
     * 更新後的約訪時間，僅成功時有值
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime recallTime;
}
