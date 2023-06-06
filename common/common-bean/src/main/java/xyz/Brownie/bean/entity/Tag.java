package xyz.Brownie.bean.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * 
 * @TableName tag
 */
@TableName(value ="tag")
@Data
public class Tag implements Serializable {
    /**
     * 标签id
     */
    @TableId
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     *  标签内容
     */
    private String content;

    /**
     * 是否删除(1已删除,0未删除)
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}