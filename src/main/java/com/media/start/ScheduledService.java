package com.media.start;

import com.media.bean.VideoUploadBean;
import com.media.utils.MediaFileUtils;
import com.media.utils.MediaUtils;
import com.media.youtube.consumer.UploadVideo;
import java.io.File;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ScheduledService {

    //每天上午10:15触发  0 */1 * * * ?
    @Scheduled(cron = "0 15 22 * * ?")
    public void scheduled() {
        //将视频上传youtube
        uploadYoutube("mafengge", "mafengge", 1);
        log.info("=====>>>>>使用cron  {}", System.currentTimeMillis());
    }

    public static void uploadYoutube(String proName, String userId, int uploadCount) {
        List<String> allFile = MediaFileUtils.getAllFile(MediaUtils.zimeikaVideoPath, true);
        for (int i = 0; i < uploadCount; i++) {
            String filePath = allFile.get(i);
            String title = filePath.substring(filePath.lastIndexOf(File.separator) + 1, filePath.indexOf(".mp4"));
            log.info(title);
            VideoUploadBean videoUploadBean = new VideoUploadBean();
            videoUploadBean.setCredentialDatastore(proName);
            videoUploadBean.setUserId(userId);
            videoUploadBean.setTitle(title);
            videoUploadBean.setDescription(title);
            videoUploadBean.setVideoPath(filePath);
            videoUploadBean.setTags1("歌曲");
            videoUploadBean.setTags2("火爆");
            videoUploadBean.setTags3("最热门");
            //videoUploadBean.setTags4(title.split("--")[1]);
            videoUploadBean.setTags4("马蜂哥");
            UploadVideo.videoUpload(videoUploadBean);

        }

    }
}