package com.media.start;

    import com.media.bean.VideoUploadBean;
    import com.media.utils.DownloadZimeika;
    import com.media.utils.DriverListener;
    import com.media.utils.FileUtils;
    import com.media.utils.JsoupUtil;
    import com.media.utils.MediaUtils;
    import com.media.youtube.consumer.UploadVideo;
    import java.io.File;
    import java.util.List;
    import org.json.JSONArray;
    import org.json.JSONObject;
    import org.openqa.selenium.By;
    import org.openqa.selenium.WebDriver;
    import org.openqa.selenium.WebElement;
    import org.openqa.selenium.chrome.ChromeDriver;
    import org.openqa.selenium.support.events.EventFiringWebDriver;
    import org.springframework.boot.SpringApplication;
    import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MediaApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(MediaApplication.class, args);
        //获取视频信息，下载地址、标题、作者等，放入TXT
        //JsoupUtil.getZimeikaInfo(2,10);
        getPoints();
        //读取txt中视频下载地址，下载视频
        //downLoadZimeikaVideo();

        //将视频上传youtube
        //uploadYoutube("mafengge", "mafengge", 8);
    }

    public static void uploadYoutube(String proName, String userId, int uploadCount) {
        List<String> allFile = FileUtils.getAllFile(MediaUtils.zimeikaVideoPath, true);
        for (int i = 0; i < uploadCount; i++) {
            String filePath = allFile.get(i);
            String title = filePath.substring(filePath.lastIndexOf(File.separator) + 1, filePath.indexOf(".mp4"));
            System.out.println(title);
            System.out.println(title.split("--")[1]);
            VideoUploadBean videoUploadBean = new VideoUploadBean();
            videoUploadBean.setCredentialDatastore(proName);
            videoUploadBean.setUserId(userId);
            videoUploadBean.setTitle(title);
            videoUploadBean.setDescription(title);
            videoUploadBean.setVideoPath(filePath);
            videoUploadBean.setTags1("美食");
            videoUploadBean.setTags2("chinafood");
            videoUploadBean.setTags3("今日头条");
            videoUploadBean.setTags4(title.split("--")[1]);
            UploadVideo.videoUpload(videoUploadBean);

        }

    }

    public static void downLoadZimeikaVideo() {
        String info = FileUtils.readFile(MediaUtils.zimekaInfoPath);
        JSONArray jsonArray = new JSONArray("[" + info + "]");
        System.out.println(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String author = jsonObject.getString("author");
            String videoTitle = jsonObject.getString("videoTitle") + "--" + author;
            String videoUrl = jsonObject.getString("videoUrl");
            DownloadZimeika.downloadVideo(MediaUtils.zimeikaVideoPath, videoTitle, videoUrl);

            System.out.println(jsonObject.getString("publishTime"));
        }
    }

    public static void getPoints() throws Exception{
        WebDriver driver = new EventFiringWebDriver(new ChromeDriver()).register(new DriverListener());
        driver.get("https://www.youlikehits.com/login.php");//打开指定的网站
        WebElement username = driver.findElement(By.id("username"));
        WebElement password = driver.findElement(By.id("password"));
        username.sendKeys("mafengge");
        password.sendKeys("maniqiu5");
        driver.findElement(By.id("loginform")).submit();
        driver.get("https://www.youlikehits.com/youtubenew2.php");
        Thread.sleep(6000);
        for(int i=0;i<100;i++) {
            driver.findElement(By.className("followbutton")).click();
            Thread.sleep(140000);
        }
    }

}
