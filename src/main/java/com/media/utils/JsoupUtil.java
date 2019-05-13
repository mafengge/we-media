package com.media.utils;

import com.media.bean.ZimeikaBean;
import com.media.spiders.producers.ExtractService;
import com.media.spiders.producers.LinkTypeData;
import com.media.spiders.producers.Rule;
import com.teamdev.jxbrowser.chromium.ba;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

@Slf4j
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
        log.info(html);
    }

    public static void getPic() throws Exception {
        for (int i = 1; i < 1000; i++) {
            Rule rule = new Rule("http://shuabao.net/wuliaotu/page/" + i, null, null, "img[src]", Rule.SELECTION,
                Rule.GET);
            Document doc = ExtractService.getDoc(rule);
            Element result = doc.getElementById("posts");
            Elements divs = result.getElementsByTag("div");
            int j = 1;
            for (Element div : divs) {
                String id = div.attr("id");
                if (!id.startsWith("post-")) {
                    continue;
                }
                Element title = div.getElementsByTag("a").get(0);
                log.info(title.attr("title"));
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
            Elements commlist = doc.getElementsByClass("commentlist");
            Elements lis = commlist.get(0).getElementsByTag("li");
            for (int j = 0; j < lis.size(); j++) {
                Elements img = lis.get(j).getElementsByTag("img");
                String url = img.attr("src");
                //String suff = url.substring(url.lastIndexOf("."));
                if (null != url && url.trim().length() != 0) {
                    log.info(url);
                    FileUtils.writeStringToFile(new File(filePathBase + "/pic_title.txt"), (url + "\n"), true);
                    String suff = url.substring(url.lastIndexOf("."));
                    byte[] btImg = GetImage.getImageFromNetByUrl(url.replace("thumb180", "large"));
                    log.info(filePathBase + "/jandan/" + i + "_" + b);
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
        ChromeOptions option = new ChromeOptions();
        //后台运行
        option.addArguments("headless");
        //取消"Chrome正在受到自动软件的控制"提示
        option.addArguments("disable-infobars");
        WebDriver driver = new ChromeDriver(option);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        for (int i = 0; i < pageNo; i++) {
            int a = startPageNo + i;
            //自媒咖 西瓜视频 五万以上播放量 美食
            //Rule rule = new Rule("http://zimeika.com/video/lists/xigua.html?cate_id=12&time_type=&read_order=&type=6&author_id=&author_name=&title=&p=" + a + "#comments");
            //自媒咖 西瓜视频 五万以上播放量 内容
            /*Rule rule = new Rule(
                "http://zimeika.com/video/lists/xigua.html?cate_id=&time_type=&read_order=&type=6&author_id=&author_name=&title=&p=1"
                    + a + "#comments");*/
            Rule rule = new Rule(
                "http://zimeika.com/video/lists/xigua.html?cate_id=6&time_type=&read_order=&type=6&author_id=8752&author_name=%E5%A5%BD%E6%AD%8C%E5%9D%8A&title=&p="
                    + a);
            Document doc = ExtractService.getDoc(rule);
            Elements commlist = doc.getElementsByClass("video-list");
            Elements lis = commlist.get(0).getElementsByTag("li");
            for (int j = 0; j < lis.size(); j++) {
                Element element = lis.get(j);
                Elements ahref = element.getElementsByTag("a");
                String zimeikaUrl = ahref.attr("href");
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
                log.info(element.getElementsByClass("video-title").text());
                zimeikaUrl = "http://zimeika.com/" + zimeikaUrl;
                DownloadZimeika.getDownloadUrl(zimeikaBean, driver, zimeikaUrl);
            }
            Thread.sleep(1000);
            log.info("读取第：" + i + "页");
        }
        driver.close();
    }


    public static void main(String args) throws Exception {
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
        log.info(thumbnail.getAttribute("a"));
        String windowHandle = driver.getWindowHandle();
        log.info(windowHandle);

        String currentWindow = driver.getWindowHandle(); //获取当前窗口的句柄
        Set<String> handles = driver.getWindowHandles(); //获取所有窗口的句柄
        Iterator<String> it = handles.iterator();
        while (it.hasNext()) {
            String handle = it.next();
            if (!handle.equals(currentWindow)) {
                //driver = driver.switchTo().window(handle); //切换到新的句柄所指向的窗口
                log.info(driver.getCurrentUrl());
                //log.info(driver.getPageSource());
                String pageSource = driver.getPageSource();
                String substring = pageSource.substring(pageSource.indexOf("vjs-tech"), pageSource.indexOf("%3D"));
                String http = substring.substring(substring.indexOf("http"), substring.length());
                log.info(http);
                /*WebElement tt_video_1a6fa = driver.findElement(By.id("vjs_video_3"));
                log.info(tt_video_1a6fa.getText());*/
                break;
            }
        }
    }

    public static void getMusic() throws Exception {
        int page = 2424;//从第多少也开始
        for (int i = 0; i < 1; i++) {
            int b = 1;
            int a = page - i;
            Rule rule = new Rule("https://page31.ctfile.com/u/1506031/17914322", null, null, "img[src]", Rule.SELECTION,
                Rule.GET);
            Document doc = ExtractService.getDoc(rule);
			/*Elements commlist = doc.getElementsByClass("commentlist");
			Elements lis = commlist.get(0).getElementsByTag("li");
			for(int j=0;j<lis.size();j++){
				Elements img = lis.get(j).getElementsByTag("img");
				String url = img.attr("src");
				//String suff = url.substring(url.lastIndexOf("."));
				if(null!=url&&url.trim().length()!=0){
					log.info(url);
					FileUtils.writeStringToFile(new File(filePathBase + "/pic_title.txt"), (url + "\n"), true);
					String suff = url.substring(url.lastIndexOf("."));
					byte[] btImg = GetImage.getImageFromNetByUrl(url.replace("thumb180", "large"));
					log.info(filePathBase + "/jandan/"+ i + "_" + b);
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
                    log.info(lis.get(3).attr("data-pic"));
                    FileUtils.writeStringToFile(new File(filePathBase + "/pic_title.txt"), (url + "\n"), true);
                    byte[] btImg = GetImage.getImageFromNetByUrl(url);
                    FileUtils.writeByteArrayToFile(new File(filePathBase + "/nhsq" + suff + ".gif"), btImg);
                }
            }
        }
    }

    public static void printf(List<LinkTypeData> datas) {
        for (LinkTypeData data : datas) {
            log.info(data.getLinkText());
            log.info(data.getLinkHref());
            log.info("***********************************");
        }
    }
}
