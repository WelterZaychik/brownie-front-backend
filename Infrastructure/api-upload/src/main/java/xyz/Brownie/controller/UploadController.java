package xyz.Brownie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.Brownie.commonutils.EmptyContentException;
import xyz.Brownie.commonutils.ResponseCode;
import xyz.Brownie.commonutils.Result;
import xyz.Brownie.service.OssService;
import xyz.Brownie.service.VodService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/upload")
@CrossOrigin
public class UploadController {

    @Autowired
    private OssService ossService;

    @Autowired
    private VodService vodService;

    private Map resNo;
    private Map resYes;

    @PostMapping("/uploadVideo")
    public Result uploadVideo(@RequestParam("file") MultipartFile file){
        resYes = new HashMap();
        String videoID = vodService.uploadVideo(file);
        resYes.put("videoId",videoID);
        return Result.suc(ResponseCode.Code200,resYes);
    }


    //根据视频列表删除阿里云中的视频
    @DeleteMapping("/removeAlyVideo")
    public Result deleteBatch(@RequestParam("videoIdList") List<String> videoIdList) {
        try {
            vodService.removeVideoByList(videoIdList);
        } catch (EmptyContentException e) {
            // throw new RuntimeException(e);
            return Result.suc(ResponseCode.Code402);
        }
        return Result.suc(ResponseCode.Code200);
    }

    @PostMapping("/uploadImg")
    public Result uploadOssFile(@RequestParam("img") MultipartFile file){
        resYes = new HashMap();
        String url = ossService.OssUpload(file);
        resYes.put("url",url);
        return Result.suc(ResponseCode.Code200,resYes);
    }

    @GetMapping("/getVideoPath/{id}")
    public Result getVideoPlayPath(@PathVariable("id") String id){
        resYes = new HashMap();
        String videoPath = vodService.getVideoPath(id);
        resYes.put("videoPath",videoPath);
        return Result.suc(ResponseCode.Code200,resYes);
    }
}
