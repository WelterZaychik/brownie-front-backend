package xyz.Brownie.bean.dto;

import lombok.Data;

@Data
public class TopicDto {
    private String keyWord;
    private String startTime;
    private String endTime;
    private Long tagId;
    private String isVideo;
    private Integer current;
    private String isTime;
    //分辨是一天,一周,还是一个月
    private String withTime;
}
