package xyz.Brownie.bean.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wulinxiong
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentsVo {

    // 评论id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    // 帖子id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long topicId;

    // 根评论id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long rootId;

    // 创建评论人id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long createById;

    // 创建评论人头像
    private String createUserAvatar;

    //所回复的目标评论的用户id
    private Long toCommentUserId;

    //回复的对象名称
    private String toCommentUserName;

    // 回复的目标评论的id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long toCommentId;

    // 评论内容
    private String content;

    // 创建时间
    private LocalDateTime createTime;

    // 评论人的用户名
    private String userName;

    // 评论人头像
    private String userAvatar;

    // 子评论List集合
    private List<CommentsVo> children;

}
