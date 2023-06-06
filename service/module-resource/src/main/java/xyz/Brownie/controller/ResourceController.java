package xyz.Brownie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.Brownie.commonutils.Result;
import xyz.Brownie.bean.entity.Resource;
import xyz.Brownie.service.ResourceService;

@RestController
@RequestMapping("/resource")
@CrossOrigin
public class ResourceController {
    @Autowired
    private ResourceService service;
    //新建资源
    @PostMapping
    public Result AddResource(@RequestBody Resource resource){
        return service.AddResource(resource);
    }
    //删除资源
    @DeleteMapping("/{id}")
    public Result DeleteResource(@PathVariable("id") long id){
        return service.DeleteResource(id);
    }
    //修改资源
    @PutMapping
    public Result UpdateResource(@RequestBody Resource resource){
        return service.UpdateResource(resource);
    }
    //查询资源
    @GetMapping
    public Result SelectResult(){
        return service.SelectResult();
    }

}
