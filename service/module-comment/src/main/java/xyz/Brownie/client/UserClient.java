package xyz.Brownie.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import xyz.Brownie.bean.entity.User;

@Component
@FeignClient("module-user")
@RequestMapping("/user")
public interface UserClient {
    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") Long id);
}
