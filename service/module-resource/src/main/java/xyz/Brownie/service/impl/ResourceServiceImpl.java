package xyz.Brownie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import xyz.Brownie.commonutils.EmptyContentException;
import xyz.Brownie.commonutils.ResponseCode;
import xyz.Brownie.commonutils.Result;
import xyz.Brownie.bean.entity.Resource;
import xyz.Brownie.mapper.ResourceMapper;
import xyz.Brownie.service.ResourceService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author 76650
* @description 针对表【resource】的数据库操作Service实现
* @createDate 2023-05-05 23:07:47
*/
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource>
        implements ResourceService{

    LambdaQueryWrapper<Resource> wrapper = null;
    private Map resNo;
    private Map resYes;
    //新增资源
    @Override
    public Result AddResource(Resource resource) {
        resNo = new HashMap();
        resYes = new HashMap();
        String msg = "";
        try {
            if (!StringUtils.hasText(resource.getTitle())){
                msg = "标题为空";
                resNo.put("msg",msg);
                throw new EmptyContentException(msg);
            }else if(resource.getTagflag()==null){
                msg = "选择的模块未知";
                resNo.put("msg",msg);
                throw new EmptyContentException(msg);
            }else if(!StringUtils.hasText(resource.getUrl())){
                msg = "url为空";
                resNo.put("msg",msg);
                throw new EmptyContentException(msg);
            } else if(resource.getIsDelete()==null){
                msg = "是否删除为空";
                resNo.put("msg",msg);
                throw new EmptyContentException(msg);
            } else if (save(resource)){
                resYes.put("resource",resource);
            }
        } catch (EmptyContentException e) {
            System.out.println(e.getMessage());
            return Result.fail(ResponseCode.Code402,resNo);
        }

        return Result.suc(ResponseCode.Code200,resYes);
    }
    //删除资源
    @Override
    public Result DeleteResource(long id) {
        resNo = new HashMap();
        resYes = new HashMap();
        try {
            removeById(id);
        } catch (Exception e) {
            resNo.put("msg","删除失败");
            return Result.fail(ResponseCode.Code402,resNo);
        }
        return Result.suc(ResponseCode.Code200);
    }
    //修改资源
    @Override
    public Result UpdateResource(Resource resource) {
        resNo = new HashMap();
        resYes = new HashMap();
        String msg = "";
        try {
            if (!StringUtils.hasText(resource.getTitle())){
                msg = "标题为空";
                resNo.put("msg",msg);
                throw new EmptyContentException(msg);
            }else if(resource.getTagflag()==null){
                msg = "选择的模块未知";
                resNo.put("msg",msg);
                throw new EmptyContentException(msg);
            }else if(!StringUtils.hasText(resource.getUrl())){
                msg = "url为空";
                resNo.put("msg",msg);
                throw new EmptyContentException(msg);
            } else if(resource.getIsDelete()==null){
                msg = "是否删除为空";
                resNo.put("msg",msg);
                throw new EmptyContentException(msg);
            } else if (updateById(resource)){
                resYes.put("resource",resource);
            }
        } catch (EmptyContentException e) {
            System.out.println(e.getMessage());
            return Result.fail(ResponseCode.Code402,resNo);
        }
        return Result.suc(ResponseCode.Code200,resYes);
    }
    //查询资源
    @Override
    public Result SelectResult() {
        resNo = new HashMap();
        resYes = new HashMap();
        try {
            wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Resource::getTagflag,1);
            List<Resource> one = list(wrapper);
            wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Resource::getTagflag,2);
            List<Resource> two = list(wrapper);
            wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Resource::getTagflag,3);
            List<Resource> three = list(wrapper);
            resYes.put("one",one);
            resYes.put("two",two);
            resYes.put("three",three);
        } catch (Exception e) {
            resNo.put("msg","某一个环节出现错误");
            return Result.fail(ResponseCode.Code402,resNo);
        }
        return Result.suc(ResponseCode.Code200,resYes);

    }
}




