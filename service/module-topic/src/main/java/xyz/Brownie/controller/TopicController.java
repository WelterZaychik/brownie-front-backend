package xyz.Brownie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.Brownie.commonutils.Result;
import xyz.Brownie.bean.dto.TopicDto;
import xyz.Brownie.bean.entity.Topic;
import xyz.Brownie.service.TopicService;

import java.util.List;


@RestController
@RequestMapping("/topic")
@CrossOrigin(origins = "*")
public class TopicController {
    @Autowired
    private TopicService topicService;

    //创建帖子
    @PostMapping
    public Result publish(@RequestBody Topic topic){return topicService.publish(topic);
    }
    //修改帖子
    @PutMapping
    public Result revise(@RequestBody Topic topic){
        return topicService.revise(topic);
    }
    //删除帖子
    @DeleteMapping("/{ids}")
    public Result delete(@PathVariable("ids") Long ids){
       return topicService.delete(ids);
    }
    //查询帖子
    @PostMapping("/selectsome")
    public Result SelectSome(@RequestBody TopicDto topicDto){
        return topicService.SelectSome(topicDto);
    }

    //帖子的点赞
    @GetMapping("/addlikes/{id}")
    public Result addlikes(@PathVariable("id") Long id){
        return topicService.addlikes(id);
    }
    //帖子详情
    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable("id") Long id){
        return topicService.detail(id);
    }
    //官方发文的帖子
    @GetMapping("/hostoftopic/{curPage}/{pageSize}")
    public Result hostoftopic(@PathVariable("curPage") int curPage ,
                              @PathVariable("pageSize") int pageSize){
        return topicService.HostOfTopic(curPage,pageSize);
    }
    //双重排序
    @GetMapping("/SelectChoose/{current}")
    public Result SelectChoose(@PathVariable("current") Integer current){
        return topicService.SelectChoose(current);
    }
    //热点资讯
    @GetMapping("/focus")
    private Result Focus(){
        return topicService.Focus();
    }

    //为用户模块提供服务，通过用户id查询帖子
    @GetMapping("/getUserTopic/{userId}")
    private List<Topic> getUserTopicList(@PathVariable("userId") Long id){
        LambdaQueryWrapper<Topic> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Topic::getCreateUserId,id);
        List<Topic> list = topicService.list(queryWrapper);
        return list;
    }
    @GetMapping("/getTopImg")
    private List<Topic> getTopImgList(){
        LambdaQueryWrapper<Topic> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Topic::getIsTop)
                .orderByDesc(Topic::getNumberOfLikes)
                .last("limit 0,5")
                .select(Topic::getCoverImage,Topic::getId);;
        List<Topic> list = topicService.list(wrapper);
        return list;
    }

}
