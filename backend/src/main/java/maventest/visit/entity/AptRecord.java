package maventest.visit.entity;

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