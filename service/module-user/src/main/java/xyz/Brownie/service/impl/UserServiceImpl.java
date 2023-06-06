package xyz.Brownie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.util.StringUtils;
import xyz.Brownie.bean.entity.Topic;
import xyz.Brownie.client.TopicClient;
import xyz.Brownie.commonutils.*;
import xyz.Brownie.bean.dto.LVNumDTO;
import xyz.Brownie.bean.entity.User;
import xyz.Brownie.mapper.UserMapper;
import xyz.Brownie.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 76650
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2023-04-25 15:09:04
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService{
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TopicClient topicClient;
    @Autowired
    private RedisCache redisCache;
    private Map res;

    //登录
    @Override
    public Result login(User user) throws RuntimeException{
        res = new HashMap();
        //1.检查参数
        if (StringUtils.isEmpty(user.getAccount()) || StringUtils.isEmpty(user.getPassword())) {
            res.put("msg","用户名或密码错误");
            return Result.fail(ResponseCode.Code402,res);
        }
        //2.查询数据库中的用户信息
        List<User> list = null;
        list = list(Wrappers.<User>lambdaQuery().eq(User::getAccount, user.getAccount()));
        if(list != null && list.size() ==1){
            User loginuser = list.get(0);
            //3.比对密码
            //解密密码
            String decrypt = Sm4EncryptUtil.decrypt(user.getPassword());
            if(user.getPassword().equals(decrypt)){
                //4.返回数据jwt
                String token = AppJwtUtil.getToken(loginuser.getId());
                res.put("token",token);
                res.put("userInfo",loginuser);
                //将jwt存入redis
                redisCache.setCacheObject("token:"+loginuser.getAccount(),token);
            }else {
                res.put("msg","用户名或密码错误");
                return Result.fail(ResponseCode.Code402,res);
            }
        }else {
            res.put("msg","用户名或密码错误");
            return Result.fail(ResponseCode.Code402,res);
        }

        return Result.suc(ResponseCode.Code200,res);
    }
    //根据用户id查帖子
    @Override
    public Result TopicById(Long id) {
        res = new HashMap();
        try {
            List<Topic> userTopicList = topicClient.getUserTopicList(id);
            // List<Topic> topicList = topicClient.getUserTopicList(id);
            res.put("list",userTopicList);
        } catch (Exception e) {
            res.put("msg","未知错误");
            return Result.fail(ResponseCode.Code402,res);
        }
        return Result.suc(ResponseCode.Code200,res);
    }
    //根据用户id查询用户的总观看数和总点赞数
    @Override
    public Result LikeViewNum(Long id) {
        res = new HashMap();
        try {
            LVNumDTO lvNumDTO = userMapper.LikeViewNum(id);
            Long UserId = lvNumDTO.getId();
            List<Topic> topics = topicClient.getUserTopicList(UserId);
            User user = userMapper.selectUser(id);
            lvNumDTO.setName(user.getName());
            lvNumDTO.setEmail(user.getEmail());
            lvNumDTO.setPhone(user.getPhone());
            lvNumDTO.setSynopsis(user.getSynopsis());
            int size = topics.size();
            lvNumDTO.setTotal(size);
            res.put("DTO",lvNumDTO);
        } catch (Exception e) {
            res.put("msg","查询错误");
            return Result.fail(ResponseCode.Code402,res);
        }
        return Result.suc(ResponseCode.Code200,res);
    }

    @Override
    public Result addUser(User user) {
        res = new HashMap();
        String msg = "";
        try {
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("account",user.getAccount());
            List<User> users = userMapper.selectList(wrapper);
            if (users.size() == 1){
                msg = "账号重复,请输入其他字符组成账号!";
                throw  new RuntimeException();
            }
            if (StringUtils.isEmpty(user.getAccount())){
                msg = "输入的账号的为空!";
                throw  new RuntimeException();
            }
            if (StringUtils.isEmpty(user.getPassword())){
                msg = "密码为空!";
                throw new RuntimeException();
            }
            //密码加密
            user.setPassword(Sm4EncryptUtil.encrypt(user.getPassword()));
            if (StringUtils.isEmpty(user.getName())){
                msg = "昵称为空";
                throw new RuntimeException();
            }
            if (StringUtils.isEmpty(user.getSynopsis())){
                user.setSynopsis("描述为空");
            }
            if (StringUtils.isEmpty(user.getPhone())&&StringUtils.isEmpty(user.getEmail())){
                msg = "电话号码或者邮箱其中一个不能为空!";
                throw new RuntimeException();
            }
            if(StringUtils.isEmpty(user.getAvatar())){
                //设置默认头像地址
                user.setAvatar("");
            }
            int count = userMapper.insert(user);
            if (count == 0){
                msg = "新建失败!";
                throw new RuntimeException();
            }
            res.put("userInfo",user);

        } catch (RuntimeException e) {
            res.put("msg",msg);
            return Result.fail(ResponseCode.Code402,res);
        }
        return Result.suc(ResponseCode.Code200,res);
    }

    //修改密码
    @Override
    public Result change(User user) {
        res = new HashMap();
        String msg = "";
        try {
            if (StringUtils.isEmpty(user.getId())) {
                msg = "id为空!";
                throw new RuntimeException();
            }
            if (StringUtils.isEmpty(user.getPassword())){
                msg = "密码为空!";
                throw new RuntimeException();
            }
            UpdateWrapper<User> wrapper = new UpdateWrapper<>();
            wrapper.eq("id",user.getId());
            user.setPassword(user.getPassword());
            userMapper.update(user,wrapper);
            // redisCache.deleteObject("login:"+user.getId());
        } catch (RuntimeException e) {
            res.put("msg","修改密码失败!");
            return Result.fail(ResponseCode.Code402,res);
        }
        return Result.suc(ResponseCode.Code200,res);
    }
    //用户信息修改
    @Override
    public Result revise(User user) {
        res = new HashMap();
        String msg = "";
        try {
            //.eq("account",user.getAccount());
            if (StringUtils.isEmpty(user.getId())) {
                msg = "id为空!";
                throw new RuntimeException();
            }
            if (StringUtils.isEmpty(user.getName())){
                msg = "修改的昵称为空";
                throw new RuntimeException();
            }
            if(StringUtils.isEmpty(user.getAvatar())){
                msg = "修改的头像为空!";
            }else {
                user.setAvatar(user.getAvatar());
            }
            UpdateWrapper<User> wrapper = new UpdateWrapper<>();
            wrapper.eq("id",user.getId());
            int update = userMapper.update(user, wrapper);
        } catch (Exception e) {
            res.put("msg",msg);
            return Result.fail(ResponseCode.Code402,res);
        }
        return Result.suc(ResponseCode.Code200,res);

    }

    @Override
    public Result part(Long id) throws RuntimeException{
        res = new HashMap();
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.select("name","synopsis","email","phone","avatar").eq("id",id);
        User user = userMapper.selectOne(wrapper);
        res.put("userinfo",user);
        return Result.suc(ResponseCode.Code200,res);
    }
    @Override
    public Result logout(String account) {
        res = new HashMap();
        try {
            boolean b = redisCache.deleteObject("token:"+account);
        } catch (Exception e) {
            res.put("msg","登出失败,请手动关闭网页!");
            return Result.fail(ResponseCode.Code402,res);
        }

        res.put("msg","登出成功");
        return Result.suc(ResponseCode.Code200,res);
    }
}




