package com.media.start;

import com.media.bean.VideoUploadBean;
import com.media.utils.MediaFileUtils;
import com.media.youtube.consumer.UploadVideo;
import java.io.File;
import java.util.List;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ScheduledService {

    //英文
    @Scheduled(cron = "0 28 04 * * ?")
    public void scheduled() throws InterruptedException {
        Random random = new Random();
        Thread.sleep(random.nextInt(1000000));
        System.out.println("scheduled开始执行自动上传任务");
        //将视频上传youtube
        uploadYoutube("mafengge", "mafengge", 1, "mafengge.json",
            "/root/mafengge11/", 8081,
            "/root/youtube/fire1/"
            //  "E:\\topic\\likefinish\\finish3\\"
        );
        log.info("=====>>>>>使用cron  {}" + System.currentTimeMillis());
    }

    @Scheduled(cron = "0 25 08 * * ?")
    public void scheduled3() throws InterruptedException {
        Random random = new Random();
        Thread.sleep(random.nextInt(1000000));
        System.out.println("scheduled开始执行自动上传任务");
        //将视频上传youtube
        uploadYoutube("mafengge", "mafengge", 1, "mafengge.json",
            "/root/mafengge11/", 8082,
            "/root/youtube/fire1/"
            //  "E:\\topic\\likefinish\\finish3\\"
        );
        log.info("=====>>>>>使用cron  {}" + System.currentTimeMillis());
    }

    @Scheduled(cron = "0 30 02 * * ?")
    public void scheduled1() {
        try {
            Random random = new Random();
            Thread.sleep(random.nextInt(1000000));
            System.out.println("scheduled1开始执行自动上传任务");
            //将视频上传youtube
            uploadYoutube("mafengge1", "mafengge1", 1, "mafengge1.json",
                "/root/mafengge11/"
                //System.getProperty("user.home")
                , 8083,
                "/root/youtube/fire2/"
                //"E:\\topic\\likefinish\\test\\"
            );
        } catch (Exception e) {
            System.out.println(e);
        }

        log.info("=====>>>>>使用cron  {}" + System.currentTimeMillis());
    }

    @Scheduled(cron = "0 20 04 * * ?")//美国时间
    public void scheduled2() throws InterruptedException {
        Random random = new Random();
        Thread.sleep(random.nextInt(1000000));
        System.out.println("scheduled2开始执行自动上传任务");
        //将视频上传youtube
        uploadYoutube("mafengge1", "mafengge1", 1, "mafengge1.json",
            "/root/mafengge11/"
            //System.getProperty("user.home")
            , 8084,
            "/root/youtube/fire2/"
            //"E:\\topic\\likefinish\\test\\"
        );
        log.info("=====>>>>>使用cron  {}" + System.currentTimeMillis());
    }

    @Scheduled(cron = "0 20 21 * * ?")
    public void scheduled4() throws InterruptedException {
        Random random = new Random();
        Thread.sleep(random.nextInt(1000000));
        System.out.println("scheduled4开始执行自动上传任务");
        //将视频上传youtube
        uploadYoutube("mafengge2", "mafengge2", 1, "mafengge2.json",
            "/root/mafengge11/"
            //"E:\\"
            , 8085,
            "/root/youtube/fire3/"
            //"E:\\topic\\test\\"
        );
        log.info("=====>>>>>使用cron  {}" + System.currentTimeMillis());
    }

    @Scheduled(cron = "0 20 09 * * ?")//美国时间
    public void scheduled5() throws InterruptedException {
        Random random = new Random();
        Thread.sleep(random.nextInt(1000000));
        System.out.println("scheduled5开始执行自动上传任务");
        //将视频上传youtube
        uploadYoutube("mafengge2", "mafengge2", 1, "mafengge2.json",
            "/root/mafengge11/"
            //System.getProperty("user.home")
            , 8086,
            "/root/youtube/fire3/"
            //"E:\\topic\\likefinish\\test\\"
        );
        log.info("=====>>>>>使用cron  {}" + System.currentTimeMillis());
    }

    @Scheduled(cron = "0 20 08 * * ?")
    public void scheduled6() throws InterruptedException {
        Random random = new Random();
        Thread.sleep(random.nextInt(1000000));
        System.out.println("scheduled6开始执行自动上传任务");
        //将视频上传youtube
        uploadYoutube("mafengge3", "mafengge3", 1, "mafengge3.json",
            "/root/mafengge11/"
            //"E:\\"
            , 8087,
            "/root/youtube/fire4/"
            //"E:\\topic\\test\\"
        );
        log.info("=====>>>>>使用cron  {}" + System.currentTimeMillis());
    }

    @Scheduled(cron = "0 23 01 * * ?")
    public void scheduled7() throws InterruptedException {
        Random random = new Random();
        Thread.sleep(random.nextInt(1000000));
        System.out.println("scheduled7开始执行自动上传任务");
        //将视频上传youtube
        uploadYoutube("mafengge3", "mafengge3", 1, "mafengge3.json",
            "/root/mafengge11/"
            //System.getProperty("user.home")
            , 8088,
            "/root/youtube/fire4/"
            //"E:\\topic\\likefinish\\test\\"
        );
        log.info("=====>>>>>使用cron  {}" + System.currentTimeMillis());
    }

    public static void main(String[] args) {
        String a = "/root/youtube/street/Mejores Street Fashion Tik Tok  Douyin China Ep.102.png";
        String substring = a.substring(a.lastIndexOf("/") + 1, a.length() - 4);
        System.out.println(substring);
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
            videoUploadBean.setPngPath(videoPath + title + ".png");

            UploadVideo.videoUpload(videoUploadBean);
        }


    }
}