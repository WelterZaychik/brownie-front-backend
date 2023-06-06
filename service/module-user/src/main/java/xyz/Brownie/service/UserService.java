package xyz.Brownie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.Brownie.commonutils.Result;
import xyz.Brownie.bean.entity.User;


/**
* @author 76650
* @description 针对表【user】的数据库操作Service
* @createDate 2023-04-25 15:09:04
*/
public interface UserService extends IService<User> {
    //登录
    public Result login(User user);
    //通过id查询文章列表
    Result TopicById(Long id);
    //根据用户id查询用户的总观看数和总点赞数
    Result LikeViewNum(Long id);

    Result addUser(User user);

    Result change(User user);

    Result revise(User user);
    //根据id查询部分信息(评论区见)
    Result part(Long id);
    Result logout(String account);
}
