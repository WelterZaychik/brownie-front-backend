package xyz.Brownie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.Brownie.commonutils.Result;
import xyz.Brownie.bean.entity.Resource;


/**
* @author 76650
* @description 针对表【resource】的数据库操作Service
* @createDate 2023-05-05 23:07:47
*/
public interface ResourceService extends IService<Resource> {
    //新建资源
    Result AddResource(Resource resource);
    //删除资源
    Result DeleteResource( long id);
    //修改资源
    Result UpdateResource( Resource resource);
    //查询资源
    Result SelectResult();
}
