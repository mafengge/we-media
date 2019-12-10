package com.media.spiders.producers;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.mobile.NetworkConnection.ConnectionType;

public class DouYinDown {
    String URl_Id = null;
    int count = 0;// 计数
    // 根据主页获取每个视频的id
    public  static void DownHtml(String url) throws InterruptedException {
        ArrayList<String> alURl = new ArrayList<>();//视频id集合
        ArrayList<String> alMP4 = new ArrayList<>();//视频下载URL集合
        System.setProperty("webdriver.chrome.driver", "C:\\ChromedDriver\\chromedriver.exe");
        // 实例化一个浏览器对象
        WebDriver driver = new ChromeDriver();
        driver.get(url);
        //Thread.sleep(8000);// 休眠等待页面加载
        //driver.findElement(By.className("like-tab tab get-list")).click();
        ConnectionType networkConnection = ((ChromeDriver) driver).getNetworkConnection();
        List<WebElement> elements = driver.findElements(By.cssSelector("li.item,goWork"));// 获取到每个视频的模块
        System.out.println(elements.size());
        //获取每个URl的ID
        for (WebElement we : elements) {
            String ids = we.getAttribute("data-id").toString();// 获取模块的data-id的属性值
            alURl.add("https://www.iesdouyin.com/share/video/" + ids);
        }
        driver.get("http://douyin.iiilab.com/");// 打开可以将每个视频链接转化成可以下载的链接的网页
        Thread.sleep(8000);// 休眠等待页面加载
        //获取可以下载的url
        for (int i = 0; i < alURl.size(); i++) {
            driver.findElement(By.cssSelector("input.form-control.link-input")).clear();// 清空这个输入框
            driver.findElement(By.cssSelector("input.form-control.link-input")).sendKeys(alURl.get(i));// 将需要转换的链接放入该输入框中
            driver.findElement(By.cssSelector("button.btn.btn-default")).click();// 点击解析
            Thread.sleep(4000);// 休眠等待页面加载
            alMP4.add( driver.findElement(By.cssSelector("a.btn.btn-success")).getAttribute("href").toString());// 获取解析后的链接
        }
        driver.close();
        //下载
        for (int i = 0; i < alMP4.size(); i++) {
            DownloadFile df = new DownloadFile();
            df.run(alMP4.get(i));
        }
    }

    public static void main(String[] args) {
        DownloadUrl dl = new DownloadUrl();
        String ID = "80602533314";// 人物ID
        try {
            DownHtml("https://www.douyin.com/share/user/" + ID + "/?share_type=link");
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 根据视频下载链接 下载视频 后缀为 .mp4 等
     *
     * @author lenovo
     *
     */
    public static class DownloadFile {
        public  void downLoadFromUrl(String urlStr, String fileName, String savePath) throws IOException {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(3000);
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            InputStream inputStream = conn.getInputStream();
            byte[] getData = readInputStream(inputStream);
            java.io.File saveDir = new java.io.File(savePath);
            if (!saveDir.exists()) {
                saveDir.mkdir();
            }
            java.io.File file = new java.io.File(saveDir + java.io.File.separator + fileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(getData);
            if (fos != null) {
                fos.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        public  byte[] readInputStream(InputStream inputStream) throws IOException {
            byte[] buffer = new byte[1024];
            int len = 0;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            while ((len = inputStream.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            bos.close();
            return bos.toByteArray();
        }


        /**
         * 程序入口
         * @param urlStr
         */
        public void run(String urlStr) {
            long imageTitile = System.currentTimeMillis();
            String fileName = imageTitile + "." + "mp4";
            String savePath = "G:\\VidioVidioVidioVidioVidioVidio";
            try {
                downLoadFromUrl(urlStr, fileName, savePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
