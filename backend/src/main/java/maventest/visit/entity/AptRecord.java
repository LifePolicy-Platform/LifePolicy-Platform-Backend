<<<<<<<< HEAD:backend/src/main/java/maventest/appointment/entity/AptRecord.java
package maventest.appointment.entity;
========
package maventest.visit.entity;
>>>>>>>> develop:backend/src/main/java/maventest/visit/entity/AptRecord.java

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;


@Data
@TableName("tb_call_appointment")
public class AptRecord {

    @TableId(value = "SNO", type = IdType.AUTO)
    private Long sno;

    private String recNo;

    private String listNo;

    private String listCampcode;

    private LocalDateTime recallTime;

    private LocalDateTime recTime;

    private LocalDateTime updateTime;

    private String updateUser;
}
