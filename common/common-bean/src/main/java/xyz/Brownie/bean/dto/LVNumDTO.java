package xyz.Brownie.bean.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@Data
public class LVNumDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String synopsis;
    private Long views;
    private Long likes;
    @TableField(exist = false)
    private Integer total;
}
