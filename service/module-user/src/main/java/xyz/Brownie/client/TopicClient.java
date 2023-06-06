package xyz.Brownie.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import xyz.Brownie.bean.entity.Topic;

import java.util.List;

@Component
@FeignClient("module-topic")
@RequestMapping("/topic")
public interface TopicClient {
    @GetMapping("/getUserTopic/{userId}")
    public List<Topic> getUserTopicList(@PathVariable("userId") Long id);
}
