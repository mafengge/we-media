package com.media.utils;

import com.media.bean.ZimeikaBean;
import com.media.spiders.producers.ExtractService;
import com.media.spiders.producers.LinkTypeData;
import com.media.spiders.producers.Rule;
import com.media.utils.DownloadZimeika;
import com.media.utils.GetImage;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.ba;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import java.util.Map;
import java.util.Set;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.apache.commons.io.FileUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class JsoupUtil {

    public static String filePathBase = "D:/home/work/pics";

	/*public static void main(String[] args) throws Exception {
		// getDatasByClass();
		// getDatasByTag();
		//getMusic();
		getPic();
		//getJDAN();
	}*/

    public static void getDatasByClass() {
        Rule rule = new Rule("http://www1.sxcredit.gov.cn/public/infocomquery.do?method=publicIndexQuery",
            new String[]{"query.enterprisename", "query.registationnumber"}, new String[]{"兴网", ""}, "cont_right",
            Rule.CLASS, Rule.POST);
        List<LinkTypeData> extracts = ExtractService.extract(rule);
        printf(extracts);
    }

    public static void getDatasByTag() {
        Rule rule = new Rule("http://www.zain800.com", null, null, "img[src]", Rule.SELECTION, Rule.GET);
        List<LinkTypeData> extracts = ExtractService.extract(rule);
        printf(extracts);
    }

    public static void getHtml() {
        Rule rule = new Rule("http://www.zain800.com/nhgx/623.htm", null, null, "img[src]", Rule.SELECTION, Rule.GET);
        String html = ExtractService.getHtml(rule);
        System.out.println(html);
    }

    public static void getPic() throws Exception {
        for (int i = 1; i < 1000; i++) {
            Rule rule = new Rule("http://shuabao.net/wuliaotu/page/" + i, null, null, "img[src]", Rule.SELECTION,
                Rule.GET);
            Document doc = ExtractService.getDoc(rule);
            System.out.println(doc);
            Element result = doc.getElementById("posts");
            Elements divs = result.getElementsByTag("div");
            int j = 1;
            for (Element div : divs) {
                String id = div.attr("id");
                if (!id.startsWith("post-")) {
                    continue;
                }
                Element title = div.getElementsByTag("a").get(0);
                System.out.println(title.attr("title"));
                FileUtils.writeStringToFile(new File(filePathBase + "/pic_title.txt"),
                    (i + "_" + j + "__" + title.attr("title") + "\n"), true);
                Element pic = div.getElementsByTag("p").get(0).getElementsByTag("img").get(0);
                String url = pic.attr("src");
                String suff = url.substring(url.lastIndexOf("."));
                byte[] btImg = GetImage.getImageFromNetByUrl(url);
                FileUtils.writeByteArrayToFile(new File(filePathBase + "/" + i + "_" + j + "__" + suff), btImg);
                j++;
            }
            Thread.sleep(2000);
        }
    }

    public static void getJDAN() throws Exception {
        int page = 428;//从第多少也开始
        for (int i = 0; i < 100; i++) {
            int b = 1;
            int a = page - i;
            Rule rule = new Rule("http://jandan.net/pic/page-" + a + "#comments", null, null, "img[src]",
                Rule.SELECTION, Rule.GET);
            Document doc = ExtractService.getDoc(rule);
            System.out.println(doc);
            Elements commlist = doc.getElementsByClass("commentlist");
            Elements lis = commlist.get(0).getElementsByTag("li");
            for (int j = 0; j < lis.size(); j++) {
                Elements img = lis.get(j).getElementsByTag("img");
                String url = img.attr("src");
                //String suff = url.substring(url.lastIndexOf("."));
                if (null != url && url.trim().length() != 0) {
                    System.out.println(url);
                    FileUtils.writeStringToFile(new File(filePathBase + "/pic_title.txt"), (url + "\n"), true);
                    String suff = url.substring(url.lastIndexOf("."));
                    byte[] btImg = GetImage.getImageFromNetByUrl(url.replace("thumb180", "large"));
                    System.out.println(filePathBase + "/jandan/" + i + "_" + b);
                    FileUtils
                        .writeByteArrayToFile(new File(filePathBase + "/jandan/" + i + "_" + b + "_" + suff), btImg);
                    b++;
                }
            }
            Thread.sleep(2000);
        }
    }

    static {
        try {
            Field e = ba.class.getDeclaredField("e");
            e.setAccessible(true);
            Field f = ba.class.getDeclaredField("f");
            f.setAccessible(true);
            Field modifersField = Field.class.getDeclaredField("modifiers");
            modifersField.setAccessible(true);
            modifersField.setInt(e, e.getModifiers() & ~Modifier.FINAL);
            modifersField.setInt(f, f.getModifiers() & ~Modifier.FINAL);
            e.set(null, new BigInteger("1"));
            f.set(null, new BigInteger("1"));
            modifersField.setAccessible(false);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public static void getZimeikaInfo(int startPageNo, int pageNo) throws Exception {
        for (int i = 0; i < pageNo; i++) {
            int a = startPageNo + i;
            //自媒咖 西瓜视频 五万以上播放量 内容
            Rule rule = new Rule(
                "http://zimeika.com/video/lists/xigua.html?cate_id=12&time_type=&read_order=&type=6&author_id=&author_name=&title=&p="
                    + a + "#comments");

            Document doc = ExtractService.getDoc(rule);
            //System.out.println(doc);
            Elements commlist = doc.getElementsByClass("video-list");
            Elements lis = commlist.get(0).getElementsByTag("li");
            for (int j = 0; j < lis.size(); j++) {
                Element element = lis.get(j);
                Elements ahref = element.getElementsByTag("a");
                String zimeikaUrl = ahref.attr("href");
                //System.out.println(zimeikaUrl);
                String videoTitle = element.getElementsByClass("video-title").text();
                String publishTime = element.getElementsByClass("publish-time").text();
                String watchCount = element.getElementsByClass("watch-count").text();
                String commentCount = element.getElementsByClass("comment-count").text();
                String author = element.getElementsByClass("author").tagName("a").text();
                String videoDuring = element.getElementsByClass("video-during").text();
                Elements elementsByClass = element.getElementsByClass("video-img");
                String imgUrl = elementsByClass.get(0).getElementsByTag("img").get(0).attr("src");
                ZimeikaBean zimeikaBean = new ZimeikaBean();
                zimeikaBean.setVideoTitle(videoTitle);
                zimeikaBean.setPublishTime(publishTime);
                zimeikaBean.setWatchCount(watchCount);
                zimeikaBean.setCommentCount(commentCount);
                zimeikaBean.setAuthor(author);
                zimeikaBean.setVideoDuring(videoDuring);
                zimeikaBean.setImgUrl(imgUrl);
                System.out.println(element.getElementsByClass("video-title").text());
                if (null != zimeikaUrl && zimeikaUrl.trim().length() != 0) {
                    zimeikaUrl = "http://zimeika.com/" + zimeikaUrl;
                    //System.out.println(zimeikaUrl);
                    Rule rule2 = new Rule(zimeikaUrl);
                    Document doc2 = ExtractService.getDoc(rule2);
                    String source_url = doc2.getElementById("source_url").text();
                    Rule rule3 = new Rule(source_url);
                    Document doc3 = ExtractService.getDoc(rule3);
                    Elements source = doc3.getElementsByTag("video");
                    Elements src = source.tagName("src");
                    System.out.println(source_url);
                    DownloadZimeika.getDownloadUrl(zimeikaBean, source_url);
                }
                System.out.println("读取：" + j);
            }
            Thread.sleep(2000);
        }
    }

    public static void main(String[] args) throws Exception {
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

        String currentWindow = driver.getWindowHandle(); //获取当前窗口的句柄
        Set<String> handles = driver.getWindowHandles(); //获取所有窗口的句柄
        Iterator<String> it = handles.iterator();
        while (it.hasNext()) {
            String handle = it.next();
            if (!handle.equals(currentWindow)) {
                driver = (FirefoxDriver) driver.switchTo().window(handle); //切换到新的句柄所指向的窗口
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

    public static void getMusic() throws Exception {
        int page = 2424;//从第多少也开始
        for (int i = 0; i < 1; i++) {
            int b = 1;
            int a = page - i;
            Rule rule = new Rule("https://page31.ctfile.com/u/1506031/17914322", null, null, "img[src]", Rule.SELECTION,
                Rule.GET);
            Document doc = ExtractService.getDoc(rule);
            System.out.println(doc);
			/*Elements commlist = doc.getElementsByClass("commentlist");
			Elements lis = commlist.get(0).getElementsByTag("li");
			for(int j=0;j<lis.size();j++){
				Elements img = lis.get(j).getElementsByTag("img");
				String url = img.attr("src");
				//String suff = url.substring(url.lastIndexOf("."));
				if(null!=url&&url.trim().length()!=0){
					System.out.println(url);
					FileUtils.writeStringToFile(new File(filePathBase + "/pic_title.txt"), (url + "\n"), true);
					String suff = url.substring(url.lastIndexOf("."));
					byte[] btImg = GetImage.getImageFromNetByUrl(url.replace("thumb180", "large"));
					System.out.println(filePathBase + "/jandan/"+ i + "_" + b);
					FileUtils.writeByteArrayToFile(new File(filePathBase + "/jandan/"+ i + "_" + b+"_"+suff), btImg);
					b++;
				}
			}*/
            Thread.sleep(2000);
        }
    }

    public static void getNHSQ() throws Exception {//内涵社区，这个他妈失败了，网站加载更多没法整，操他妈。
        Rule rule = new Rule("http://neihanshequ.com/pic/", null, null, "img[src]", Rule.SELECTION, Rule.GET);
        forDiv(rule);//第一次遍历
        String loadMoreUrl = "http://neihanshequ.com/pic/?is_json=1&app_name=neihanshequ_web&max_time=1480986907";
        String[] para = new String[]{"is_json", "app_name", "max_time"};
        String[] val = new String[]{"1", "neihanshequ_web", "1480986907"};
        for (int i = 1; i < 10; i++) {
            Rule rule2 = new Rule(loadMoreUrl, null, null, "img[src]", Rule.SELECTION, Rule.GET);
            forDiv(rule2);
        }
    }

    public static void forDiv(Rule rule) throws Exception {
        Document doc = ExtractService.getDoc(rule);
        System.out.println(doc);
        Elements options = doc.getElementsByClass("options");
        for (Element option : options) {
            Elements lis = option.getElementsByTag("li");
            if (null != lis) {
                Element li = lis.get(0);
                Elements span = li.getElementsByTag("span");
                String likeUp = span.get(0).text();
                if (Integer.parseInt(likeUp) > 20000) {
                    String url = lis.get(3).attr("data-pic");
                    String suff = url.substring(url.lastIndexOf("."));
                    System.out.println(lis.get(3).attr("data-pic"));
                    FileUtils.writeStringToFile(new File(filePathBase + "/pic_title.txt"), (url + "\n"), true);
                    byte[] btImg = GetImage.getImageFromNetByUrl(url);
                    FileUtils.writeByteArrayToFile(new File(filePathBase + "/nhsq" + suff + ".gif"), btImg);
                }
            }
        }
    }

    public static void printf(List<LinkTypeData> datas) {
        for (LinkTypeData data : datas) {
            System.out.println(data.getLinkText());
            System.out.println(data.getLinkHref());
            System.out.println("***********************************");
        }
    }
}
