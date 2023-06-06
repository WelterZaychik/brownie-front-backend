package xyz.Brownie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.Brownie.bean.entity.Tag;
import xyz.Brownie.commonutils.ResponseCode;
import xyz.Brownie.commonutils.Result;
import xyz.Brownie.service.TagService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/tag")
@CrossOrigin
public class TagController {

    @Autowired
    private TagService tagService;

    private Map resYes;

    @GetMapping
    public Result getTagList(){
        resYes = new HashMap();
        resYes.put("tagList",tagService.list());
        return Result.suc(ResponseCode.Code200,resYes);
    }

    @DeleteMapping("/{id}")
    public Result delTag(@PathVariable("id") Long id){
        tagService.removeById(id);
        return Result.suc(ResponseCode.Code200);
    }

    @PostMapping
    public Result addTag(Tag tag){
        boolean save = tagService.save(tag);
        if (save){
            return Result.suc(ResponseCode.Code200);
        }else{
            return Result.suc(ResponseCode.Code402);
        }
    }

    @GetMapping("/getTagName/{id}")
    public String getTagName(@PathVariable("id") Long id){
        Tag byId = tagService.getById(id);
        return  byId.getContent();
    }

}
