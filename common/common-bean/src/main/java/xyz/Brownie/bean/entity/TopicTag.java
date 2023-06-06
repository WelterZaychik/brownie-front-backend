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
 * @TableName topic_tag
 */
@TableName(value ="topic_tag")
@Data
public class TopicTag implements Serializable {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private Long topicId;

    private Long tagId;
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer isDelete;

    public TopicTag() {
    }

    public TopicTag(Long topicId, Long tagId,Integer isDelete) {
        this.isDelete = isDelete;
        this.topicId = topicId;
        this.tagId = tagId;
    }

    private static final long serialVersionUID = 1L;
}