package xyz.Brownie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import xyz.Brownie.bean.dto.TopicDto;
import xyz.Brownie.bean.entity.Topic;


import java.util.List;

/**
* @author 76650
* @description 针对表【topic】的数据库操作Mapper
* @createDate 2023-04-25 15:50:42
* @Entity xyz.Brownie.abouttopic.domain.Topic
*/
@Mapper
public interface TopicMapper extends BaseMapper<Topic> {
    List<Topic> searchTopic(TopicDto topicDto);
    Integer updatelikes(Long id);
    Integer updateviews(Long id);
    //分页
    List<Topic> pagination(Page<Topic> page);

    // List<Topic> selectPages(Page<Topic> page);

}




