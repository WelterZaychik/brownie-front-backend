package xyz.Brownie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import xyz.Brownie.bean.entity.User;
import xyz.Brownie.client.UserClient;
import xyz.Brownie.commonutils.BeanCopyUtils;
import xyz.Brownie.commonutils.EmptyContentException;
import xyz.Brownie.commonutils.ResponseCode;
import xyz.Brownie.commonutils.Result;
import xyz.Brownie.bean.entity.Comments;
import xyz.Brownie.bean.vo.CommentsVo;
import xyz.Brownie.bean.vo.PageVo;
import xyz.Brownie.mapper.CommentsMapper;
import xyz.Brownie.service.CommentsService;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class CommentsServiceImpl extends ServiceImpl<CommentsMapper, Comments>
        implements CommentsService {

    @Autowired
    private UserClient userClient;

    @Override
    public Result commentsList(Long topicId, Integer pageNum, Integer pageSize) {
        Map commentsListMap = new HashMap<>();
        // 查询对应文章的根评论
        LambdaQueryWrapper<Comments> queryWrapper = new LambdaQueryWrapper<>();
        // 对topicId进行判断
        queryWrapper.eq(Comments::getTopicId, topicId);
        // 根评论 rootId为-1
        queryWrapper.eq(Comments::getRootId, -1);

        // 分页查询
        Page<Comments> page = new Page(pageNum, pageSize);
        page(page, queryWrapper);

        List<CommentsVo> commentVoList = toCommentVoList(page.getRecords());

        // 查询所有根评论对应的子评论集合，并且赋值给对应的属性
        for (CommentsVo commentsVo : commentVoList) {
            // 查询对应的子评论
            List<CommentsVo> children = getChildren(commentsVo.getId());
            // 赋值
            commentsVo.setChildren(children);
        }


        commentsListMap.put("list", new PageVo(commentVoList, page.getTotal()));

        return Result.suc(ResponseCode.Code200, commentsListMap);
    }

    @Override
    public Result addComments(Comments comments) {
        Map addCommentsMap = new HashMap<>();
        String msg = "";
        // 评论内容不能为空
        if (!StringUtils.hasText(comments.getContent())) {
            try {
                msg = "评论内容不能为空";
                addCommentsMap.put("msg", msg);
                throw new EmptyContentException(msg);
            } catch (EmptyContentException e) {
                System.out.println(e.getMessage());
                return Result.fail(ResponseCode.Code402, addCommentsMap);
            }
        }
        save(comments);

        addCommentsMap.put("msg", "操作成功");

        return Result.suc(ResponseCode.Code200, addCommentsMap);
    }

    @Override
    public Result delComments(Long id) {
        Map delCommentsMap = new HashMap<>();
        // 根据评论id删除评论
        removeById(id);
        LambdaQueryWrapper<Comments> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comments::getRootId,id);
        remove(queryWrapper);
        delCommentsMap.put("msg", "操作成功");
        return Result.suc(ResponseCode.Code200, delCommentsMap);
    }


    /**
     * 根据根评论的id查询所对应的子评论的集合
     *
     * @param id 根评论的id
     * @return
     */
    private List<CommentsVo> getChildren(Long id) {

        LambdaQueryWrapper<Comments> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comments::getRootId, id);
        queryWrapper.orderByAsc(Comments::getCreateTime);
        List<Comments> comments = list(queryWrapper);

        List<CommentsVo> commentVos = toCommentVoList(comments);
        return commentVos;

    }

    private List<CommentsVo> toCommentVoList(List<Comments> list) {
        List<CommentsVo> commentVos = BeanCopyUtils.copyBeanList(list, CommentsVo.class);
        //遍历vo集合
        for (CommentsVo commentVo : commentVos) {
            //通过createById查询用户的昵称并赋值
            User user = userClient.getUserById(commentVo.getCreateById());
            commentVo.setUserName(user.getName());
            commentVo.setUserAvatar(user.getAvatar());

            //通过toCommentUserId查询用户的昵称并赋值
            //如果toCommentUserId不为-1才进行查询
            if (commentVo.getToCommentUserId() != -1) {
                String toCommentUserName = userClient.getUserById(commentVo.getToCommentUserId()).getName();
                commentVo.setToCommentUserName(toCommentUserName);
            }
        }
        return commentVos;
    }


}




