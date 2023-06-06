package xyz.Brownie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.Brownie.commonutils.Result;
import xyz.Brownie.bean.entity.Comments;
import xyz.Brownie.service.CommentsService;

/**
 * @author wulinxiong
 * @version 1.0
 */
@RestController
@RequestMapping("/comments")
@CrossOrigin
public class CommentsController {

    @Autowired
    private CommentsService commentsService;

    /**
     * 查询评论
     *
     * @param topicId  帖子id
     * @param pageNum  当前第几页
     * @param pageSize 每页条数
     * @return
     */
    @GetMapping("/commentsList/{topicId}/{pageNum}/{pageSize}")
    public Result commentsList(@PathVariable Long topicId, @PathVariable Integer pageNum, @PathVariable Integer pageSize) {
        return commentsService.commentsList(topicId, pageNum, pageSize);
    }

    /**
     * 发表评论
     *
     * @param comments
     * @return
     */
    @PostMapping
    public Result addComments(@RequestBody Comments comments) {
        return commentsService.addComments(comments);
    }

    /**
     * 删除评论 （根据创建评论人id进行删除）
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result delComments(@PathVariable Long id) {
        return commentsService.delComments(id);
    }
    @GetMapping("/getCommentNum/{id}")
    public Long getCommentNum(@PathVariable("id") Long id) {
        LambdaQueryWrapper<Comments> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comments::getTopicId,id);
        return (long) commentsService.count(queryWrapper);
    }
}
