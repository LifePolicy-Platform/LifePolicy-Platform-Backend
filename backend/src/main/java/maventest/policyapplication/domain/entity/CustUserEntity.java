package maventest.policyapplication.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@TableName("tb_cust_user")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustUserEntity {

    @TableId(value = "MEMBER_ID", type = IdType.AUTO)
    private Long memberId;

    @TableField("USERNAME")
    private String username;

    @TableField("NAME")
    private String name;

    @TableField("IDENTITY_CARD")
    private String identityCard;

    @TableField("GENDER")
    private String gender;

    @TableField("BIRTHDAY")
    private LocalDate birthday;
}
