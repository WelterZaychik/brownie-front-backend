package xyz.Brownie.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("module-comment")
@Component
@RequestMapping("/comments")
public interface CommentClient {
    @GetMapping("/getCommentNum/{id}")
    public Long getCommentNum(@PathVariable("id") Long id);
}