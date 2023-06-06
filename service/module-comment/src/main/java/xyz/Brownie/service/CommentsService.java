package xyz.Brownie.service;


import com.baomidou.mybatisplus.extension.service.IService;
import xyz.Brownie.commonutils.Result;
import xyz.Brownie.bean.entity.Comments;

public interface CommentsService extends IService<Comments> {

    Result commentsList(Long topicId, Integer pageNum, Integer pageSize);

    Result addComments(Comments comments);


    Result delComments(Long id);

}
