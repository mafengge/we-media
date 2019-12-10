package com.media.spiders.producers;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;

public class DouYinQushuiyin {

    public static void main(String[] args) throws Exception{
        String charset = "gbk";
        File file = new File("E:\\youtube\\ss.txt");
        if (file.isFile() && file.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, charset);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuffer sb = new StringBuffer();
                String text = null;
                int b = 1200;
                while ((text = bufferedReader.readLine()) != null) {
                    try {
                        tet(text,++b+"");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    sb.append(text);
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public static String decodeHttpUrl(String url) {
        int start = url.indexOf("http");
        int end = url.lastIndexOf("/");
        String decodeurl = url.substring(start, end);
        return decodeurl;
    }
    public static void tet(String matchUrl,String name) throws Exception{
        Map<String, String> headers = new HashMap<>();
        headers.put("Connection", "keep-alive");
        //headers.put("Host", "aweme.snssdk.com");
        headers.put("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.108 Mobile Safari/537.36");
        headers.put("upgrade-insecure-requests","1");
        //●抖音链接(使用手机分享功能,复制链接)
        String url = "https://aweme.snssdk.com/aweme/v1/playwm/?video_id=v0300f160000bm60300e74g3024qtmf0&line=0";

        String url1 ="#在抖音，记录美好生活#男女之别，孰是孰非，大家去评说吧！如果有下辈子，我还想做女人@抖音小助手 #脱口秀 #波波脱口秀 http://v.douyin.com/9AoPrQ/ 复制此链接，打开【抖音短视频】，直接观看视频！";


        //过滤链接，获取http连接地址
        String finalUrl = decodeHttpUrl(url1);

        //1.利用Jsoup抓取抖音链接
        //抓取抖音网页
        //String htmls = Jsoup.connect(finalUrl).timeout(10000).ignoreContentType(true).execute().body();
        //System.out.println(htmls); //做测试时使用

        //2.利用正则匹配可以抖音下载链接
        //playAddr: "https://aweme.snssdk.com/aweme/v1/playwm/?video_id=v0200ffc0000bfil6o4mavffbmroeo80&line=0",
        //具体匹配内容格式：「https://aweme.snssdk.com/aweme/...line=0」
        Pattern patternCompile = Pattern.compile("(?<=playAddr: \")https?://.+(?=\",)");
        //利用Pattern.compile("正则条件").matcher("匹配的字符串对象")方法可以将需要匹配的字段进行匹配封装 返回一个封装了匹配的字符串Matcher对象

        //3.匹配后封装成Matcher对象
        //Matcher m = patternCompile.matcher(htmls);

        //4.①利用Matcher中的group方法获取匹配的特定字符串 ②利用String的replace方法替换特定字符,得到抖音的去水印链接
        //String matchUrl ="http://v26-dy.ixigua.com/80b6519619b06bfbbf3b56ff84cb3715/5db057da/video/m/2209c132f10dd6f492993158e12315598ff1163d99ed000062a4800a105c/?a=1128&br=840&cr=0&cs=0&dr=0&ds=6&er=&l=201910232038220100200470723D334162&lr=&qs=0&rc=amQ2dWtubDg6cDMzaWkzM0ApNmc0OWg6ZTw8NzM1aGQ7OGdhYWtvcV9jL2ZfLS01LS9zczUxXi0yNTNgY19hMDIvM186Yw%3D%3D";
        /*while(m.find()) {
            matchUrl = m.group(0).replaceAll("playwm", "play");
        }*/
        System.out.println(matchUrl);
        //5.将链接封装成流
        //注:由于抖音对请求头有限制,只能设置一个伪装手机浏览器请求头才可实现去水印下载

        //6.利用Joup获取视频对象,并作封装成一个输入流对象
        BufferedInputStream in = Jsoup.connect(matchUrl).headers(headers).timeout(10000).ignoreContentType(true).execute().bodyStream();

        Long timetmp = System.currentTimeMillis();
        String fileAddress = "d:/抖音视频/"+name+".mp4";

        //7.封装一个保存文件的路径对象
        File fileSavePath = new File(fileAddress);

        //注:如果保存文件夹不存在,那么则创建该文件夹
        File fileParent = fileSavePath.getParentFile();
        if(!fileParent.exists()){
            fileParent.mkdirs();
        }

        //8.新建一个输出流对象
        OutputStream out =
            new BufferedOutputStream(
                new FileOutputStream(fileSavePath));

        //9.遍历输出文件
        int b ;
        while((b = in.read()) != -1) {
            out.write(b);
        }

        out.close();//关闭输出流
        in.close(); //关闭输入流

        //注:打印获取的链接
        System.out.println("-----抖音去水印链接-----\n"+matchUrl);
        System.out.println("\n-----视频保存路径-----\n"+fileSavePath.getAbsolutePath());
    }
}
