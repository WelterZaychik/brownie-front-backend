package xyz.Brownie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.Brownie.commonutils.Result;
import xyz.Brownie.bean.dto.TopicDto;
import xyz.Brownie.bean.entity.Topic;


/**
* @author 76650
* @description 针对表【topic】的数据库操作Service
* @createDate 2023-04-25 15:50:42
*/
public interface TopicService extends IService<Topic> {
      //查询帖子
    Result SelectSome(TopicDto topicDto);
    //删除帖子
    Result delete(Long ids);
    //修改帖子
    Result revise(Topic topic);
    //新建帖子
    Result publish(Topic topic);
    //帖子的点赞
    Result addlikes(Long id);
    //帖子详情
    Result detail(Long id);
    //查询官方发的帖子
    Result HostOfTopic(int curPage,int pageSize);
    //双重排序
    Result SelectChoose(Integer current);
    //热点资讯
    Result Focus();
}
