package com.media.start;

import com.media.bean.VideoUploadBean;
import com.media.utils.DownloadZimeika;
import com.media.utils.MediaUtils;
import com.media.youtube.consumer.UploadVideo;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MediaApplication {

	public static void main(String[] args) throws Exception{
        /*String uriAPI = "http://zimeika.com/index/video/parser.html";// Post方式没有参数在这里
        HttpPost httpRequst = new HttpPost(uriAPI);// 创建HttpPost对象

        String link = "https://www.ixigua.com/group/6681773825517945358/";
        //Map<String, String> map = excuteJs(link);

        httpRequst.setHeader("Origin", "http://zimeika.com");
        httpRequst.setHeader("Content-Type","application/json");
        httpRequst.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.81 Safari/537.36");
        httpRequst.setHeader("Cookie","s007df62c=8ppq55v4amd2sp3mdasjq6uhf5; Hm_lvt_c501e68ec4f8d48b652a9d50c8401586=1555695242,1555733501,1555743304,1555749401; menu-style=full; Hm_lpvt_c501e68ec4f8d48b652a9d50c8401586=1555768990");
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("source", link));
        params.add(new BasicNameValuePair("site", "xigua"));
        //params.add(new BasicNameValuePair("s", map.get("s")));

        httpRequst.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
        HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequst);
        String result = "";
        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            HttpEntity httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity);// 取出应答字符串
        }
        System.out.println(result);*/
        FirefoxBinary firefoxBinary = new FirefoxBinary();
        firefoxBinary.addCommandLineOptions("--headless");
       // System.setProperty("webdriver.gecko.driver", "C:\\ChromedDriver\\geckodriver.exe");
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setBinary(firefoxBinary);
        FirefoxDriver driver = new FirefoxDriver(firefoxOptions);
		//System.setProperty("webdriver.chrome.driver","C:\\ChromedDriver\\chromedriver.exe");//chromedriver服务地址
		//WebDriver driver =new ChromeDriver(); //新建一个WebDriver 的对象，但是new 的是FirefoxDriver的驱动
		driver.get("http://zimeika.com/video/detail/xigua.html?id=6772810");//打开指定的网站
		WebElement d_parser_video = driver.findElement(By.id("origin_url"));
		d_parser_video.click();
        WebElement thumbnail = driver.findElement(By.className("thumbnail"));
        System.out.println(thumbnail.getAttribute("a"));
        String windowHandle = driver.getWindowHandle();
        System.out.println(windowHandle);

        String currentWindow=driver.getWindowHandle(); //获取当前窗口的句柄
        Set<String> handles=driver.getWindowHandles(); //获取所有窗口的句柄
        Iterator<String> it=handles.iterator();
        while (it.hasNext()){
            String handle=it.next();
            if(!handle.equals(currentWindow)){
                driver= (FirefoxDriver) driver.switchTo().window(handle); //切换到新的句柄所指向的窗口
                System.out.println(driver.getCurrentUrl());
                //System.out.println(driver.getPageSource());
                String pageSource = driver.getPageSource();
                String substring = pageSource.substring(pageSource.indexOf("vjs-tech"), pageSource.indexOf("%3D"));
                String http = substring.substring(substring.indexOf("http"), substring.length());
                System.out.println(http);
                /*WebElement tt_video_1a6fa = driver.findElement(By.id("vjs_video_3"));
                System.out.println(tt_video_1a6fa.getText());*/
                break;
            }
        }

        //driver.close();
		//SpringApplication.run(MediaApplication.class, args);
		/*VideoUploadBean videoUploadBean = new VideoUploadBean();
		videoUploadBean.setCredentialDatastore("mafengge");
		videoUploadBean.setUserId("mafengge");
		videoUploadBean.setTitle("视频标题");
		videoUploadBean.setDescription("视频描述");
		videoUploadBean.setVideoPath("D:\\video\\zimeika\\20190419\\1.mp4");
		videoUploadBean.setTags1("标签1");
		videoUploadBean.setTags2("标签2");
		videoUploadBean.setTags3("标签3");
		UploadVideo.videoUpload(videoUploadBean);*/
		//String videoUrl = "http://v1-default.ixigua.com/6eb7a8b227778c7ef87f6f0ae9a1abef/5cbae999/video/m/220a094517280fc4538a49c5318ecfdb59a1161d7f7000005395f7745815/?rc=Mzw4ZnRlbWx1bDMzODczM0ApQHRAbzM3NTk2MzUzMzM2NDUzNDVvQGgzdSlAZjN1KWRzcmd5a3VyZ3lybHh3ZjUzQDBfYG1sL25mcF8tLTMtL3NzLW8jbyMuLzUtMzAtLjI0My0xNi06I28jOmEtcSM6YHZpXGJmK2BeYmYrXnFsOiMuL14%3D";
		//DownloadZimeika.downloadVideo("D:\\video\\zimeika\\" + MediaUtils.getCurrDate() +"\\1", "自媒体" , videoUrl);
	}
    private static Map<String, String> excuteJs(String link) throws ScriptException,
        FileNotFoundException, NoSuchMethodException {
        ScriptEngineManager engineManager = new ScriptEngineManager();
        ScriptEngine engine = engineManager.getEngineByName("JavaScript"); // 得到脚本引擎
        engine.eval(new FileReader(
            "E:\\application\\workspace\\test\\test\\src\\test\\test.js"));
        Invocable inv = (Invocable) engine;
        Object test2 = inv.invokeFunction("test2");
        String param = link + "@" + test2.toString();
        Object a = inv.invokeFunction("test", param);
        Map<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("r", test2.toString());
        hashMap.put("s", a.toString());
        return hashMap;
    }

}
