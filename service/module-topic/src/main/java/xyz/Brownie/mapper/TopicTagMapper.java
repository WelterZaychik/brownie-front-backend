package xyz.Brownie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xyz.Brownie.bean.entity.TopicTag;


/**
* @author 76650
* @description 针对表【topic_tag】的数据库操作Mapper
* @createDate 2023-05-04 21:46:53
* @Entity xyz.Brownie.abouttopic.domain.TopicTag
*/
@Mapper
public interface TopicTagMapper extends BaseMapper<TopicTag> {

}




