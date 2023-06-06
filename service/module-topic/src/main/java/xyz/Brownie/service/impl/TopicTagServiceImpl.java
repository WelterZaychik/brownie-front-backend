package xyz.Brownie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.Brownie.bean.entity.TopicTag;
import xyz.Brownie.mapper.TopicTagMapper;
import xyz.Brownie.service.TopicTagService;

/**
* @author 76650
* @description 针对表【topic_tag】的数据库操作Service实现
* @createDate 2023-05-04 21:46:53
*/
@Service
public class TopicTagServiceImpl extends ServiceImpl<TopicTagMapper, TopicTag>
    implements TopicTagService{

}




