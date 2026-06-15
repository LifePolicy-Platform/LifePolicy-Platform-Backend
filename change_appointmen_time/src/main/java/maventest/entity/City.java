package maventest.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("city")
public class City {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    @TableField("country_code")
    private String countryCode;

    private String district;

    private Integer population;
}
