package xyz.Brownie.bean.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName resource
 */
@TableName(value ="resource")
@Data
public class Resource implements Serializable {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String title;

    private String url;

    private String logo;

    private Integer tagflag;
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer isDelete;

    private static final long serialVersionUID = 1L;
}