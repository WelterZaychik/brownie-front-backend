package xyz.Brownie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.Brownie.commonutils.ResponseCode;
import xyz.Brownie.commonutils.Result;
import xyz.Brownie.bean.entity.User;
import xyz.Brownie.service.UserService;


@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;

    //登录
    // @PostMapping("/login")
    // public Result login(@RequestBody User user){
    //     return userService.login(user);
    // }

    //登录
    @PostMapping("/login")
    public Result login(@RequestParam("account") String account,@RequestParam("password") String password) {
        User user = new User();
        user.setAccount(account);
        user.setPassword(password);
        return userService.login(user);
    }

    //新建用户
    @PostMapping("/add")
    public Result add(@RequestBody User user){//创建前十个,直接给管理员
        return userService.addUser(user);
    }

    // 通过id查询user
    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") Long id){
        return userService.getById(id);
    }

    //通过id查询文章列表
    @GetMapping("/getTopicList/{id}")
    public Result TopicById(@PathVariable("id") Long id){
        return userService.TopicById(id);
    }

    //根据用户id查询用户的总观看数和总点赞数
    @GetMapping("/lvn/{id}")
    public Result LikeViewNum(@PathVariable("id")Long id){
        return userService.LikeViewNum(id);
    }

    //修改密码
    @PostMapping("/change")
    public Result change(@RequestBody User user){
        return userService.change(user);
    }

    //用户信息修改
    @PutMapping("/revise")
    public Result revise(@RequestBody User user){
        return userService.revise(user);
    }
    //登出

    @GetMapping("/logout/{account}")
    public Result logout(@PathVariable("account") String account){
        return userService.logout(account);
    }

}
