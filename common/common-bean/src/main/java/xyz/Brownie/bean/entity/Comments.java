package xyz.Brownie.bean.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 *
 * @TableName comments
 */
@TableName(value ="comments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comments implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 帖子id
     */
    @TableField(value = "topic_id")
    private Long topicId;

    /**
     * 根评论id
     */
    @TableField(value = "root_id")
    private Long rootId;

    /**
     * 创建评论人id
     */
    @TableField(value = "create_by_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long createById;

    /**
     * 所回复的目标评论的用户id
     */
    @TableField(value = "to_comment_user_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long toCommentUserId;

    /**
     * 回复的目标评论的id
     */
    @TableField(value = "to_comment_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long toCommentId;

    /**
     * 评论内容
     */
    @TableField(value = "content")
    private String content;


    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 删除标志（0代表未删除，1代表已删除）
     */
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
