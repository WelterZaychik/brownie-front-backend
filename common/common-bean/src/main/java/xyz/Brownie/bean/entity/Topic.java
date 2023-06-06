package xyz.Brownie.bean.entity;
import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @TableName topic
 */
@TableName(value ="topic")
@Data
public class Topic implements Serializable {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long createUserId;

    private String title;

    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    private Integer plate;

    private Long numberOfLikes;

    private Long numberOfViews;
    private Long comments;
    private Integer isTop;

    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer isDelete;

    private Integer isVideo;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    private String coverImage;

    private String synopsis;

    @TableField(exist = false)
    private Long tagId;

    @TableField(exist = false)
    private String tagName;
    @TableField(exist = false)
    private String nickName;
    @TableField(exist = false)
    private String avatar;
    //文章总数
    @TableField(exist = false)
    private Long total;
    @TableField(exist = false)
    private Integer queryType;
    @TableField(exist = false)
    private Integer queryDay;
    @TableField(exist = false)
    private Date queryStartTime;
    @TableField(exist = false)
    private Date queryEndTime;

    private static final long serialVersionUID = 1L;
}