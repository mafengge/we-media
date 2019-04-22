package com.media.bean;

import lombok.Data;

/**
 * @author: mafengge
 * @description: 自媒咖网站信息实体bean
 * @date: 20:44 2019/4/21
 */
@Data
public class ZimeikaBean {

    private String videoTitle;
    //评论数
    private String commentCount;
    //观看数
    private String watchCount;
    //发布时间
    private String publishTime;
    //作者
    private String author;
    //封面图片地址
    private String imgUrl;
    //视频时长
    private String videoDuring;
    //视频下载地址
    private String videoUrl;
}
