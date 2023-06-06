package xyz.Brownie.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("module-tag")
@RequestMapping("/tag")
@Component
public interface TagClient {
    @GetMapping("/getTagName/{id}")
    public String getTagName(@PathVariable("id") Long id);
}
