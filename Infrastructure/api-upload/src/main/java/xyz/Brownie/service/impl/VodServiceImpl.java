package xyz.Brownie.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import xyz.Brownie.commonutils.EmptyContentException;
import xyz.Brownie.commonutils.Result;
import xyz.Brownie.service.VodService;
import xyz.Brownie.util.UploadUtils;

import java.io.InputStream;
import java.util.List;

@Service
public class VodServiceImpl implements VodService {
    @Override
    public String uploadVideo(MultipartFile file) {
        try {
            //上传的文件的原始名称(如:01.mp4)
            String fileName = file.getOriginalFilename();
            //上传后在阿里云显示的名称(带不带后缀都行,我这里没带)
            String title = fileName.substring(0, fileName.lastIndexOf("."));
            //上传的文件的输入流
            InputStream inputStream = file.getInputStream();

            UploadStreamRequest request = new UploadStreamRequest(
                    UploadUtils.ACCESS_KEY_ID,
                    UploadUtils.ACCESS_KEY_SECRET,
                    title,
                    fileName,
                    inputStream);
            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);

            String videoId = null;
            if (response.isSuccess()) {
                videoId = response.getVideoId();
                System.out.print("VideoId=" + response.getVideoId() + "\n");
            } else { //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
                videoId = response.getVideoId();
                System.out.print("VideoIdError=" + response.getVideoId() + "\n");
                System.out.print("ErrorCode=" + response.getCode() + "\n");
                System.out.print("ErrorMessage=" + response.getMessage() + "\n");
            }

            return videoId;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void removeVideoByList(List videoIdList) throws EmptyContentException {
        Result resNo;
        try {

            //1.创建初始化对象
            String regionId = "cn-shanghai";  // 点播服务接入区域
            DefaultProfile profile = DefaultProfile.getProfile(regionId, UploadUtils.ACCESS_KEY_ID,UploadUtils.ACCESS_KEY_SECRET);
            DefaultAcsClient client = new DefaultAcsClient(profile);
            //2.创建删除的request
            DeleteVideoRequest request = new DeleteVideoRequest();

            //3.向request对象里面设置视频id
            //①需要先将videoIdList集合中的id遍历为1,2,3的形式
            String videoIds = StringUtils.join(videoIdList.toArray(), ",");
            //②设置视频id
            request.setVideoIds(videoIds);

            //4.调用初始化对象里面的方法,实现删除
            client.getAcsResponse(request);
        } catch(Exception e) {
            e.printStackTrace();
            throw new EmptyContentException("视频上传异常");
        }
    }

    @Override
    public void removeVideoById(String id) {
        Result resNo;
        try {
            //1.创建初始化对象
            String regionId = "cn-shanghai";  // 点播服务接入区域
            DefaultProfile profile = DefaultProfile.getProfile(regionId, UploadUtils.ACCESS_KEY_ID,UploadUtils.ACCESS_KEY_SECRET);
            DefaultAcsClient client = new DefaultAcsClient(profile);
            //2.创建删除的request
            DeleteVideoRequest request = new DeleteVideoRequest();
            //3.向request对象里面设置视频id
            request.setVideoIds(id);
            //4.调用初始化对象里面的方法,实现删除
            client.getAcsResponse(request);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getVideoPath(String id) {
        String regionId = "cn-shanghai";  // 点播服务接入区域
        DefaultProfile profile = DefaultProfile.getProfile(regionId, UploadUtils.ACCESS_KEY_ID,UploadUtils.ACCESS_KEY_SECRET);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        GetPlayInfoResponse response = new GetPlayInfoResponse();
        String vodUrl = "";
        try {
            GetPlayInfoRequest request = new GetPlayInfoRequest();
            request.setVideoId(id);
            response = client.getAcsResponse(request);

            List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
            //播放地址
            GetPlayInfoResponse.PlayInfo playInfo = playInfoList.get(0);
            System.out.print("PlayInfo.PlayURL = " + playInfo.getPlayURL() + "\n");
            vodUrl=playInfo.getPlayURL();
            //Base信息
            System.out.print("VideoBase.Title = " + response.getVideoBase().getTitle() + "\n");
        } catch (Exception e) {
            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
        }
        System.out.print("RequestId = " + response.getRequestId() + "\n");
        return vodUrl;
    }
}
