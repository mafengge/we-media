package com.media.start;

import com.media.bean.VideoUploadBean;
import com.media.utils.MediaFileUtils;
import com.media.youtube.consumer.UploadVideo;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ScheduledService {

    //Tik Tok Girls
    public void mafengge() throws InterruptedException {
        System.out.println("scheduled开始执行自动上传任务");
        //将视频上传youtube
        uploadYoutube("mafengge", "mafengge", 1, "mafengge.json",
            "/root/mafengge11/", 8082,
            "/root/youtube/street/"
            //  "E:\\topic\\likefinish\\finish3\\"
        );
        log.info("=====>>>>>使用cron  {}" + System.currentTimeMillis());
    }

    //Asia Video
    public void mafengge1() {
        try {
            System.out.println("scheduled1开始执行自动上传任务");
            //将视频上传youtube
            uploadYoutube("mafengge1", "mafengge1", 1, "mafengge1.json",
                "/root/mafengge11/"
                //System.getProperty("user.home")
                , 8083,
                "/root/youtube/street/"
                //"D:\\aa\\"
            );
        } catch (Exception e) {
            System.out.println(e);
        }

        log.info("=====>>>>>使用cron  {}" + System.currentTimeMillis());
    }

    //亚洲TV
    public void mafengge2(){
        System.out.println("scheduled4开始执行自动上传任务");
        //将视频上传youtube
        uploadYoutube("mafengge2", "mafengge2", 1, "mafengge2.json",
            "/root/mafengge11/"
            //"E:\\"
            , 8085,
            "/root/youtube/street/"
            //"E:\\topic\\test\\"
        );
        log.info("=====>>>>>使用cron  {}" + System.currentTimeMillis());
    }

    //大马视频
    public void mafengge3() throws InterruptedException {
        System.out.println("scheduled6开始执行自动上传任务");
        //将视频上传youtube
        uploadYoutube("mafengge3", "mafengge3", 1, "mafengge3.json",
            "/root/mafengge11/"
            //"E:\\"
            , 8087,
            "/root/youtube/street/"
            //"E:\\topic\\test\\"
        );
        log.info("=====>>>>>使用cron  {}" + System.currentTimeMillis());
    }

    //英文
    @Scheduled(cron = "0 0 1 * * ?")
    public void mafenggeA() throws InterruptedException {
        mafengge();
    }

    /*@Scheduled(cron = "0 0 19 * * ?")
    public void mafenggeB() throws InterruptedException {
        mafengge();
    }

    @Scheduled(cron = "0 0 3 * * ?")
    public void mafenggeC() throws InterruptedException {
        mafengge();
    }*/

    /*@Scheduled(cron = "0 0 13 * * ?")
    public void mafenggeD() throws InterruptedException {
        mafengge();
    }*/

    /*@Scheduled(cron = "0 0 1 * * ?")
    public void mafengge1A() {
        mafengge1();
    }*/

   /* @Scheduled(cron = "0 0 19 * * ?")
    public void mafengge1B() {
        mafengge1();
    }
*/
    @Scheduled(cron = "0 0 3 * * ?")
    public void mafengge1C() {
        mafengge1();
    }

    /*@Scheduled(cron = "0 0 13 * * ?")
    public void mafengge1D() {
        mafengge1();
    }*/

    /*@Scheduled(cron = "0 0 1 * * ?")
    public void mafengge2A() throws InterruptedException {
        mafengge2();
    }*/

    @Scheduled(cron = "0 0 19 * * ?")
    public void mafengge2B() throws InterruptedException {
        mafengge2();
    }

    /*@Scheduled(cron = "0 0 3 * * ?")
    public void mafengge2C() throws InterruptedException {
        mafengge2();
    }*/

    /*@Scheduled(cron = "0 0 13 * * ?")
    public void mafengge2D() throws InterruptedException {
        mafengge2();
    }*/

    /*@Scheduled(cron = "0 0 1 * * ?")
    public void mafengge3A() throws InterruptedException {
        mafengge3();
    }*/

    /*@Scheduled(cron = "0 0 19 * * ?")
    public void mafengge3B() throws InterruptedException {
        mafengge3();
    }*/

    /*@Scheduled(cron = "0 0 3 * * ?")
    public void mafengge3C() throws InterruptedException {
        mafengge3();
    }*/

    @Scheduled(cron = "0 0 13 * * ?")
    public void mafengge3D() throws InterruptedException {
        mafengge3();
    }


    public static void main(String[] args) {
        while (true){
            DateFormat format = new SimpleDateFormat("yyyyMMddHHmSSS");
            String settleDateStr = format.format(new Date());
            System.out.println(settleDateStr);
        }

    }

    public static void uploadYoutube(String proName, String userId, int uploadCount, String authName, String oauthName,
        Integer port, String videoPath) {
        List<String> allFile = MediaFileUtils.getAllFile(videoPath, true);
        for (int i = 0; i < uploadCount; i++) {
            String filePath = allFile.get(i);
            System.out.println(filePath);
            String title = filePath.substring(filePath.lastIndexOf(File.separator) + 1, filePath.length() - 4);
            System.out.println(title);
            VideoUploadBean videoUploadBean = new VideoUploadBean();
            videoUploadBean.setCredentialDatastore(proName);
            videoUploadBean.setUserId(userId);
            videoUploadBean.setAuthName(authName);
            videoUploadBean.setOauthName(oauthName);
            videoUploadBean.setPort(port);
            videoUploadBean.setVideoPath(videoPath + title + ".mp4");
            videoUploadBean.setPngPath(videoPath + title + ".jpg");
            /*if (proName.equals("mafengge2") || proName.equals("mafengge3")) {
                int temp = (int) (50 * Math.random() + 1);
                videoUploadBean.setPngPath("/root/youtube/fire/" + temp + ".png");
            } else {
                videoUploadBean.setPngPath(videoPath + title + ".png");
            }*/

            UploadVideo.videoUpload(videoUploadBean);
        }
    }
}