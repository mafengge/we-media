package com.media.utils;

import static com.media.utils.MediaUtils.ffmpegPath;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class VideoUtils {

    /**
     * 获取音频的长度 用来计算帧速
     *
     * @param ffprobePath ffprobe的位置  public static final String ffprobepath = "D:\\ffmpeg\\bin\\ffprobe.exe";
     * @param filePath public static final String mp3path = "D:\\ffmpeg\\bin\\audio.mp3";
     */

    /*public static double getBitrate(String ffprobePath, String filePath) {
        String cmd = ffprobePath + "  -v quiet -show_format -print_format json " + filePath;
        System.out.println(cmd);
        try {
            Runtime run = Runtime.getRuntime();
            Process p = run.exec(cmd);
            BufferedInputStream in = new BufferedInputStream(p.getInputStream());
            BufferedReader inBr = new BufferedReader(new InputStreamReader(in));
            StringBuffer sb = new StringBuffer();
            String lineStr;
            while ((lineStr = inBr.readLine()) != null) {
                sb.append(lineStr);
            }
            if (p.waitFor() != 0) {
                if (p.exitValue() == 1) {
                    System.err.println("命令执行失败!");
                }
            }
            inBr.close();
            in.close();
            return analyseInfo(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }*/


    /**
     * 图片+音频合成初步MP4
     *
     * @param duration 时间
     * @param ffmpegPath ffmpeg的位置 public static final String ffmpegpath = "D:\\ffmpeg\\bin\\ffmpeg.exe";
     * @param MP3Path MP3位置  public static final String mp3path = "D:\\ffmpeg\\bin\\audio.mp3";
     * @param initMP4Path 要合成的 mp4 放置位置（无字幕）
     * @param imagesPath 图片文件夹位置，这里图片下载保存格式是 0001.jpg ，所以获取到图片文件夹位置，批量读取图片
     */

    public static void makeMP4(double duration, String ffmpegPath, String MP3Path, String initMP4Path,
        String imagesPath) throws IOException {
        int imageSizes = 0;
        //一秒都多少帧
        double frameRate = 1 / (duration / imageSizes);
        List<String> command = new ArrayList<>();
        command.add(ffmpegPath);
        command.add("-r");
        command.add(frameRate + "");
        command.add("-i");
        command.add(imagesPath + "/%04d.jpg");
        command.add("-i");
        command.add(MP3Path);
        command.add("-c:a");
        command.add("copy");
        command.add("-shortest");
        command.add("-s");
        command.add("1080x1920");
        command.add("-v");
        command.add("quiet");
        command.add("-r");
        command.add("5");
        command.add(initMP4Path);
        // 执行操作
        ProcessBuilder builder = new ProcessBuilder(command);
        Process process = builder.start();
        InputStream errorStream = process.getErrorStream();
        InputStreamReader isr = new InputStreamReader(errorStream);
        BufferedReader br = new BufferedReader(isr);
        String line = "";
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
        if (br != null) {
            br.close();
        }
        if (isr != null) {
            isr.close();
        }
        if (errorStream != null) {
            errorStream.close();
        }
    }

    public static void main(String[] args) throws Exception {
        List<String> allFile = FileUtils.getAllFile(MediaUtils.zimeikaVideoPath, true);
        for(int p=0;p<allFile.size();p++) {
            shearVideo(allFile.get(p));
            System.out.println(allFile.get(p));
        }
    }



    /**
     * 视频添加字幕
     *
     * @param ffmpegPath public static final String ffmpegpath = "D:\\ffmpeg\\bin\\ffmpeg.exe";
     * @param intputMVPath public static final String lastvedioURL = "D:\\ffmpeg\\bin\\movie\\init.mp4";
     * 第一步合成的mp4(没有字幕)
     * @param contentPath public static final String contentURL = "D\\:\\\\ffmpeg\\\\bin\\\\content.txt"; 字幕位置
     * @param outputMVPath public static final String firstmp4URL = "D:\\ffmpeg\\bin\\output.mp4"; 最后视频保存位置（有字幕）
     */

    public static void addTexttoMP4(String ffmpegPath, String intputMVPath, String outputMVPath)
        throws IOException {
        List<String> command = new ArrayList<>();
        command.add(ffmpegPath);
        command.add("-i");
        command.add(intputMVPath);
        command.add("-filter:v");
        command.add(
            "drawtext=\"fontfile=D://msjh.ttf:textfile='欢迎订阅chinafood，每天更新优质美食视频！':fontcolor=white:fontsize=60:box=1:x=10:y=350:boxcolor=black@0.5:y=h-line_h-150:x=w-(t+0)*w/6.9\"");
        command.add("-codec:v");
        command.add("libx264");
        command.add("-codec:a");
        command.add("copy");
        command.add("-y");
        command.add(outputMVPath);
        // 执行操作
        ProcessBuilder builder = new ProcessBuilder(command);
        Process process = builder.start();
        InputStream errorStream = process.getErrorStream();
        InputStreamReader isr = new InputStreamReader(errorStream);
        BufferedReader br = new BufferedReader(isr);
        String line = "";
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
        if (br != null) {
            br.close();
        }
        if (isr != null) {
            isr.close();
        }
        if (errorStream != null) {
            errorStream.close();
        }

    }

    public static void shearVideo(String outPath){
        try {
            long a = System.currentTimeMillis();
            FileUtils.rename(outPath, MediaUtils.zimeikaVideoPath + a + ".mp4");
            Thread.sleep(1000);
            Runtime runtime = Runtime.getRuntime();
            //ffmpeg  -i aaa.mp4 -vcodec copy -acodec copy -ss 00:00:03 cc.mp4 -y
            String cut = ffmpegPath
                + " -i "
                + MediaUtils.zimeikaVideoPath + a + ".mp4"
                + " -vcodec copy -acodec copy -ss  00:00:03 "
                + outPath
                + " -y";
            runtime.exec(cut);
            Thread.sleep(3000);
            FileUtils.rename(MediaUtils.zimeikaVideoPath + a + ".mp4",outPath);
            FileUtils.delFile(MediaUtils.zimeikaVideoPath + a + ".mp4");
            System.out.println("剪辑成功：" + outPath);
            /*final Thread t1 = new Thread(new Runnable() {

                @Override
                public void run() {
                    System.out.println("t1");
                    try {

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
            final Thread t2 = new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        t1.join();
                        Runtime runtime = Runtime.getRuntime();
                        //ffmpeg  -i aaa.mp4 -vcodec copy -acodec copy -ss 00:00:03 cc.mp4 -y
                        String cut = ffmpegPath
                            + " -i "
                            + MediaUtils.zimeikaVideoPath + i + ".mp4"
                            + " -vcodec copy -acodec copy -ss  00:00:03 "
                            + outPath
                            + " -y";
                            runtime.exec(cut);
                        System.out.println(cut);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("t2");
                }
            });
            Thread t3 = new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        t2.join();
                        FileUtils.delFile(MediaUtils.zimeikaVideoPath + i + ".mp4");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("t3");
                }
            });
            t3.start();
            Thread.sleep(1000);
            t2.start();
            Thread.sleep(3000);
            t1.start();
            Thread.sleep(1000);*/
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
