package xyz.Brownie.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xyz.Brownie.bean.entity.Resource;


/**
* @author 76650
* @description 针对表【resource】的数据库操作Mapper
* @createDate 2023-05-05 23:07:47
* @Entity xyz.Brownie.bean.entity.Resource
*/
@Mapper
public interface ResourceMapper extends BaseMapper<Resource> {

}




