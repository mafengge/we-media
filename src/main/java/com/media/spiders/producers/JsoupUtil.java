package com.media.spiders.producers;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
		Rule rule = new Rule("http://www1.sxcredit.gov.cn/public/infocomquery.do?method=publicIndexQuery", new String[] { "query.enterprisename", "query.registationnumber" }, new String[] { "兴网", "" }, "cont_right",
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
			Rule rule = new Rule("http://shuabao.net/wuliaotu/page/" + i, null, null, "img[src]", Rule.SELECTION, Rule.GET);
			Document doc = ExtractService.getDoc(rule);
			System.out.println(doc);
			Element result = doc.getElementById("posts");
			Elements divs = result.getElementsByTag("div");
			int j = 1;
			for (Element div : divs) {
				String id = div.attr("id");
				if (!id.startsWith("post-"))
					continue;
				Element title = div.getElementsByTag("a").get(0);
				System.out.println(title.attr("title"));
				FileUtils.writeStringToFile(new File(filePathBase + "/pic_title.txt"), (i + "_" + j + "__" + title.attr("title") + "\n"), true);
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
	
	public static void getJDAN() throws Exception{
		int page = 428;//从第多少也开始
		for (int i = 0; i < 100; i++) {
			int b = 1;
			int a = page-i;
			Rule rule = new Rule("http://jandan.net/pic/page-"+a+"#comments",null,null,"img[src]",Rule.SELECTION,Rule.GET);
			Document doc = ExtractService.getDoc(rule);
			System.out.println(doc);
			Elements commlist = doc.getElementsByClass("commentlist");
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
			}
			Thread.sleep(2000);
		}
	}
	public static void main(String[] args) throws Exception{
		int page = 10;//从第多少也开始
		for (int i = 0; i < 10; i++) {
			int b = 1;
			int a = page-i;
			Rule rule = new Rule("http://zimeika.com/video/lists/xigua.html?cate_id=12&time_type=&read_order=&type=6&author_id=&author_name=&title=&p="+a+"#comments");

			Document doc = ExtractService.getDoc(rule);
			//System.out.println(doc);
			Elements commlist = doc.getElementsByClass("video-list");
			Elements lis = commlist.get(0).getElementsByTag("li");
			for(int j=0;j<lis.size();j++){
				Elements img = lis.get(j).getElementsByTag("a");
				String url = img.attr("href");
				if(null!=url&&url.trim().length()!=0){
					url = "http://zimeika.com/" + url;
					System.out.println(url);
					Rule rule2 = new Rule(url);
					Document doc2 = ExtractService.getDoc(rule2);
					String source_url = doc2.getElementById("source_url").text();
					System.out.println(source_url);
					Rule rule3 = new Rule(source_url);
					Document doc3 = ExtractService.getDoc(rule3);
					Elements source = doc3.getElementsByTag("source");
					Elements src = source.tagName("src");
					System.out.println(src);
				}
			}
			Thread.sleep(2000);
		}
	}
	public static void getMusic() throws Exception{
		int page = 2424;//从第多少也开始
		for (int i = 0; i < 1; i++) {
			int b = 1;
			int a = page-i;
			Rule rule = new Rule("https://page31.ctfile.com/u/1506031/17914322",null,null,"img[src]",Rule.SELECTION,Rule.GET);
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
    public static void getNHSQ() throws Exception{//内涵社区，这个他妈失败了，网站加载更多没法整，操他妈。
    	Rule rule = new Rule("http://neihanshequ.com/pic/",null,null,"img[src]",Rule.SELECTION,Rule.GET);
    	forDiv(rule);//第一次遍历
    	String loadMoreUrl = "http://neihanshequ.com/pic/?is_json=1&app_name=neihanshequ_web&max_time=1480986907";
    	String[] para = new String[]{"is_json","app_name","max_time"};
    	String[] val = new String[]{"1","neihanshequ_web","1480986907"};
    	for (int i = 1; i < 10; i++) {
    		Rule rule2 = new Rule(loadMoreUrl,null,null,"img[src]",Rule.SELECTION,Rule.GET);
        	forDiv(rule2);
    	}
    }
    public static void forDiv(Rule rule) throws Exception{
    	Document doc = ExtractService.getDoc(rule);
    	System.out.println(doc);
    	Elements options = doc.getElementsByClass("options");
    	for(Element option : options){
    		Elements lis = option.getElementsByTag("li");
    		if(null!=lis){
    			Element li = lis.get(0);
    			Elements span = li.getElementsByTag("span");
    			String likeUp = span.get(0).text();
    			if(Integer.parseInt(likeUp)>20000){
    				String url = lis.get(3).attr("data-pic");
    				String suff = url.substring(url.lastIndexOf("."));
    				System.out.println(lis.get(3).attr("data-pic"));
    				FileUtils.writeStringToFile(new File(filePathBase + "/pic_title.txt"), (url + "\n"), true);
    				byte[] btImg = GetImage.getImageFromNetByUrl(url);
    				FileUtils.writeByteArrayToFile(new File(filePathBase + "/nhsq" +suff+".gif"), btImg);
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
