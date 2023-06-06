package xyz.Brownie.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import xyz.Brownie.bean.entity.User;
import xyz.Brownie.client.CommentClient;
import xyz.Brownie.client.TagClient;
import xyz.Brownie.client.UserClient;
import xyz.Brownie.commonutils.EmptyContentException;
import xyz.Brownie.commonutils.ResponseCode;
import xyz.Brownie.commonutils.Result;
import xyz.Brownie.bean.dto.TopicDto;
import xyz.Brownie.bean.entity.Topic;
import xyz.Brownie.bean.entity.TopicTag;
import xyz.Brownie.mapper.TopicMapper;
import xyz.Brownie.mapper.TopicTagMapper;
import xyz.Brownie.service.TopicService;
import xyz.Brownie.service.TopicTagService;

import java.time.LocalDateTime;
import java.util.*;


/**
* @author 76650
* @description 针对表【topic】的数据库操作Service实现
* @createDate 2023-04-25 15:50:42
*/
@Service
public class TopicServiceImpl extends ServiceImpl<TopicMapper, Topic>
    implements TopicService{
    @Autowired
    private TopicMapper topicMapper;
    @Autowired
    private TopicTagService topicTagService;
    @Autowired
    private UserClient userClient;
    @Autowired
    private TagClient tagClient;
    @Autowired
    private TopicTagMapper topicTagMapper;
    @Autowired
    private CommentClient commentClient;

    private Map resNo;
    private Map resYes;

    public List<Topic> searchTopic(TopicDto topicDto) {
        return topicMapper.searchTopic(topicDto);
    }
    //新增帖子
    @Override
    public Result publish(Topic topic){
        resNo = new HashMap();
        resYes = new HashMap();
        String msg = "";
        try {
            if (!StringUtils.hasText(topic.getTitle())){
                msg = "标题为空";
                resNo.put("msg",msg);
                throw new EmptyContentException(msg);
            }else if(!StringUtils.hasText(topic.getContent())){
                msg = "内容为空";
                resNo.put("msg",msg);
                throw new EmptyContentException(msg);
            }else if(!StringUtils.hasText(topic.getSynopsis())){
                msg = "简介为空";
                resNo.put("msg",msg);
                throw new EmptyContentException(msg);
            } else if(topic.getTagId() == null){
                msg = "未选择标签";
                resNo.put("msg",msg);
                throw new EmptyContentException(msg);
            }else if (save(topic)){
                topicTagService.save(new TopicTag(topic.getId(),topic.getTagId(),topic.getIsDelete()));
                Long createUserId = topic.getCreateUserId();
                User user = userClient.getUserById(createUserId);
                topic.setNickName(user.getName());
                topic.setAvatar(user.getAvatar());
                // resYes.put("topic",topic);
            }
        } catch (EmptyContentException e) {
            System.out.println(e.getMessage());
            return Result.fail(ResponseCode.Code402,resNo);
        }

        // return Result.suc(ResponseCode.Code200,resYes);
        return Result.suc(ResponseCode.Code200);
    }

    //删除帖子
    @Override
    public Result delete(Long ids) {
        resNo = new HashMap();
        resYes = new HashMap();
        try {
            if (removeById(ids)) {
                //通过帖子id删除对应的帖子标签表
                LambdaQueryWrapper<TopicTag> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(TopicTag::getTopicId,ids);
                topicTagService.remove(wrapper);
                resYes.put("msg","删除成功!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resNo.put("msg","已经删除啦,请刷新页面");
            return Result.fail(ResponseCode.Code402,resNo);
        }
        return Result.suc(ResponseCode.Code200,resYes);
    }

    //修改帖子
    @Override
    public Result revise(Topic topic){
        resNo = new HashMap();
        resYes = new HashMap();
        String msg = "";
        try {
            if (!StringUtils.hasText(topic.getTitle())){
                msg = "标题为空";
                resNo.put("msg",msg);
                throw new EmptyContentException(msg);
            }else if(!StringUtils.hasText(topic.getContent())){
                msg = "内容为空";
                resNo.put("msg",msg);
                throw new EmptyContentException(msg);
            }else if(!StringUtils.hasText(topic.getSynopsis())){
                msg = "简介为空";
                resNo.put("msg",msg);
                throw new EmptyContentException(msg);
            } else if(topic.getTagId() == null){
                msg = "未选择标签";
                resNo.put("msg",msg);
                throw new EmptyContentException(msg);
            }else if(updateById(topic)){
                UpdateWrapper<TopicTag> wrapper = new UpdateWrapper<>();
                wrapper.eq("topic_id",topic.getId()).set("tag_id",topic.getTagId());
                topicTagService.update(null,wrapper);
                resYes.put("topic",topic);
            }
        } catch (EmptyContentException e) {
            System.out.println(e.getMessage());
            return Result.fail(ResponseCode.Code402,resNo);

        }
        return Result.suc(ResponseCode.Code200,resYes);
    }

    //模糊查询
    @Override
    public Result SelectSome(TopicDto topicDto) {
        resNo = new HashMap();
        resYes = new HashMap();
        LambdaQueryWrapper<Topic> topicWrapper = new LambdaQueryWrapper<>();
        List<Long> topicIds = new ArrayList<>();
        Page<Topic> page = null;
        String msg = "某一处出现错误请检查!";
        try {
            if (!(topicDto.getTagId() == null)) {
                QueryWrapper<TopicTag> wrapper = new QueryWrapper<>();
                wrapper.eq("tag_id", topicDto.getTagId());
                List<TopicTag> topicTags = topicTagMapper.selectList(wrapper);
                for (int i = 0; i < topicTags.size(); i++) {
                    topicIds.add(topicTags.get(i).getTopicId());
                }
                topicWrapper.in(Topic::getId, topicIds);
            }
            topicWrapper.gt(StringUtils.hasText(topicDto.getStartTime()),Topic::getCreateTime, topicDto.getStartTime())
                    .le(StringUtils.hasText(topicDto.getEndTime()),Topic::getCreateTime,topicDto.getEndTime())
                    .eq(StringUtils.hasText(topicDto.getIsVideo()), Topic::getIsVideo, topicDto.getIsVideo())
                    .orderByDesc(StringUtils.isEmpty(topicDto.getIsTime()) || topicDto.getIsTime().equals("0"), Topic::getNumberOfViews)
                    .orderByDesc(topicDto.getIsTime().equals("1"), Topic::getCreateTime);
            if (StringUtils.hasText(topicDto.getKeyWord())) {
                topicWrapper.and(qw -> qw.like(Topic::getTitle, topicDto.getKeyWord())
                        .or()
                        .like(Topic::getSynopsis, topicDto.getKeyWord()));
            }
            if (StringUtils.hasText(topicDto.getWithTime())){
                String withTime = topicDto.getWithTime();
                switch (withTime){
                    case "0":
                        break;
                    case "1":
                        topicWrapper.apply(true, "DATE_SUB(CURDATE(), INTERVAL 1 DAY) <= date(create_time)");
                        break;
                    case "2":
                        topicWrapper.apply(true, "DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= date(create_time)");
                        break;
                    case "3":
                        topicWrapper.apply(true, "DATE_SUB(CURDATE(), INTERVAL 30 DAY) <= date(create_time)");
                        break;
                    default:
                        break;
                }
            }
            page = new Page<>(topicDto.getCurrent(), 6);

            Page<Topic> topicPage = topicMapper.selectPage(page, topicWrapper);
            List<Topic> topicList = topicPage.getRecords();
            for (int i = 0; i < topicList.size(); i++) {

                User user = userClient.getUserById(topicList.get(i).getCreateUserId());
                topicList.get(i).setNickName(user.getName());
                topicList.get(i).setAvatar(user.getAvatar());

                LambdaQueryWrapper<TopicTag> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(TopicTag::getTopicId,topicList.get(i).getId());
                TopicTag topicTag = topicTagMapper.selectOne(queryWrapper);
                topicList.get(i).setTagName(tagClient.getTagName(topicTag.getTagId()));
                topicList.get(i).setComments(commentClient.getCommentNum(topicList.get(i).getId()));
            }
            resYes.put("fuzzyList",topicList);
            resYes.put("fuzzyTotal",(int)topicPage.getTotal());
        }catch (Exception e) {
            e.printStackTrace();
            resNo.put("msg",msg);
            return Result.fail(ResponseCode.Code402,resNo);
        }
        return Result.suc(ResponseCode.Code200,resYes);
    }

    //帖子的点赞
    @Override
    public Result addlikes(Long id){
        resNo = new HashMap();
        resYes = new HashMap();
        if(topicMapper.updatelikes(id) == 0){
            resNo.put("data","未能点赞");
            Result.fail(ResponseCode.Code402,resNo);
        }
        resYes.put("data","成功");
        return Result.suc(ResponseCode.Code200,resYes);
    }
    //帖子的观看数
    public Result addviews(Long id) {
        resNo = new HashMap();
        resYes = new HashMap();
        if (topicMapper.updateviews(id) != 0) {
            Topic topic = topicMapper.selectById(id);
            Long numberOfViews = null;
            try {
                numberOfViews = topic.getNumberOfViews();
            } catch (Exception e) {
                String msg = "估计是is_delete为0,反正就是出错啦!";
                System.out.println(msg);
                resNo.put("msg",msg);
                return Result.fail(ResponseCode.Code402,resNo);
            }
            resYes.put("data",numberOfViews);
            return Result.suc(ResponseCode.Code200,resYes);
        }
        return Result.fail(ResponseCode.Code402,resNo);
    }
    //帖子详情
    @Override
    public Result detail(Long id) {
        Topic topic = null;
        try {
            resNo = new HashMap();
            resYes = new HashMap();
            topic = topicMapper.selectById(id);
            Long createUserId = topic.getCreateUserId();
            User user = userClient.getUserById(createUserId);
            List<TopicTag> topicTags = topicTagService.list(new LambdaQueryWrapper<TopicTag>().eq(TopicTag::getTopicId, id));
            Long tagId = topicTags.get(0).getTagId();
//            topic.setTagName(tagClient.getTagName(tagId));
            topic.setTagId(tagId);
            topic.setNickName(user.getName());
            topic.setAvatar(user.getAvatar());
            addviews(id);
            resYes.put("topic",topic);
        } catch (Exception e) {
            resNo.put("msg","出现错误!");
            return Result.fail(ResponseCode.Code402,resNo);
        }
        return Result.suc(ResponseCode.Code200,resYes);
    }
    //官方发的帖子
    @Override
    public Result HostOfTopic(int curPage,int pageSize) {
        resNo = new HashMap();
        resYes = new HashMap();
        QueryWrapper<Topic> wrapper = new QueryWrapper<>();
        wrapper.eq("create_user_id","0")
                .orderByDesc("create_time");
        Page<Topic> page = new Page<>(curPage,pageSize);
        try {
            Page<Topic> topicPage = topicMapper.selectPage(page, wrapper);
            List<Topic> list = topicPage.getRecords();
            for(Topic tb : list){
                tb.setComments(commentClient.getCommentNum(tb.getId()));
            }
            long total = topicPage.getTotal();
            resYes.put("hostList",list);
            resYes.put("hostTotal",(int)total);
            if (list.size() == 0 || list == null){
                resNo.put("msg","没有查到或者发生错误!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(ResponseCode.Code402,resNo);
        }
        return Result.suc(ResponseCode.Code200,resYes);

    }
    //双重排序
    @Override
    public Result SelectChoose(Integer current) {
        resNo = new HashMap();
        resYes = new HashMap();
        QueryWrapper<Topic> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("is_top").orderByDesc(true,"number_of_likes");
        //30天内的帖子
        wrapper.apply(true, "DATE_SUB(CURDATE(), INTERVAL 30 DAY) <= date(create_time)");
        Page<Topic> page = new Page<>(current,6);
        try {
            Page<Topic> topic = topicMapper.selectPage(page, wrapper);
            List<Topic> records = topic.getRecords();
            resYes.put("total",(int)topic.getTotal());
            for (int i = 0; i < records.size(); i++) {
                User user = userClient.getUserById(records.get(i).getCreateUserId());
                records.get(i).setNickName(user.getName());
                records.get(i).setAvatar(user.getAvatar());
                LambdaQueryWrapper<TopicTag> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(TopicTag::getTopicId,records.get(i).getId());
                TopicTag topicTag = topicTagMapper.selectOne(queryWrapper);
                records.get(i).setTagName(tagClient.getTagName(topicTag.getTagId()));
                records.get(i).setComments(commentClient.getCommentNum(records.get(i).getId()));
            }
            resYes.put("list",records);

        } catch (Exception e) {
            resNo.put("msg","出现错误!");
            return Result.fail(ResponseCode.Code402,resNo);
        }
        return Result.suc(ResponseCode.Code200,resYes);
    }
    //热点资讯
    @Override
    public Result Focus() {
        resNo = new HashMap();
        resYes = new HashMap();
        QueryWrapper<Topic> wrapper = new QueryWrapper<>();
        //7天内的帖子
        wrapper.apply(true, "DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= date(create_time)");
        wrapper.orderByDesc("number_of_views").last("limit 0 ,6");
        try {
            List<Topic> topics = topicMapper.selectList(wrapper);
            resYes.put("hotList",topics);

        } catch (Exception e) {
            resNo.put("msg","出现错误!");
            return Result.fail(ResponseCode.Code402,resNo);
        }
        return Result.suc(ResponseCode.Code200,resYes);
    }


}




