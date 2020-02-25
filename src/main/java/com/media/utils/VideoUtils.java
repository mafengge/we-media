package com.media.utils;

import static com.media.utils.MediaUtils.ffmpegPath;
import static com.media.utils.Picture1.jointPic;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VideoUtils {

    /**
     * 获取音频的长度 用来计算帧速
     *
     * @param ffprobePath ffprobe的位置  public static final String ffprobepath = "D:\\ffmpeg\\bin\\ffprobe.exe";
     * @param filePath public static final String mp3path = "D:\\ffmpeg\\bin\\audio.mp3";
     */

    /*public static double getBitrate(String ffprobePath, String filePath) {
        String cmd = ffprobePath + "  -v quiet -show_format -print_format json " + filePath;
        log.info(cmd);
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
     * @param imagesPath 图片文件夹位置，这里图片下载保存格式是 0001.png ，所以获取到图片文件夹位置，批量读取图片
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
        command.add(imagesPath + "/%04d.png");
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
            log.info(line);
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

    /**
     * 视频添加字幕
     *
     * @param ffmpegPath public static final String ffmpegpath = "D:\\ffmpeg\\bin\\ffmpeg.exe";
     * @param intputMVPath public static final String lastvedioURL = "D:\\ffmpeg\\bin\\movie\\init.mp4";
     * 第一步合成的mp4(没有字幕)
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
            log.info(line);
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
        List<String> allFile = MediaFileUtils.getAllFile("D:\\youtube\\test\\", true);
        //视频批量截图，三张
        /*for (int p = 0; p < allFile.size(); p++) {
            //shearVideo(allFile.get(p));
            String path = allFile.get(p);
            if (path.contains(".mp4")) {
                log.info(allFile.get(p));
                processImg(allFile.get(p), "D:\\ffmpeg-4.1.1-win64-static\\bin\\ffmpeg.exe", "1", "aa.png");
                processImg(allFile.get(p), "D:\\ffmpeg-4.1.1-win64-static\\bin\\ffmpeg.exe", "2", "bb.png");
                processImg(allFile.get(p), "D:\\ffmpeg-4.1.1-win64-static\\bin\\ffmpeg.exe", "3", "cc.png");
                processImg(allFile.get(p), "D:\\ffmpeg-4.1.1-win64-static\\bin\\ffmpeg.exe", "4", "dd.png");
                processImg(allFile.get(p), "D:\\ffmpeg-4.1.1-win64-static\\bin\\ffmpeg.exe", "5", "ee.png");
                processImg(allFile.get(p), "D:\\ffmpeg-4.1.1-win64-static\\bin\\ffmpeg.exe", "6", "ff.png");
                processImg(allFile.get(p), "D:\\ffmpeg-4.1.1-win64-static\\bin\\ffmpeg.exe", "7", "gg.png");
                processImg(allFile.get(p), "D:\\ffmpeg-4.1.1-win64-static\\bin\\ffmpeg.exe", "8", "hh.png");
                processImg(allFile.get(p), "D:\\ffmpeg-4.1.1-win64-static\\bin\\ffmpeg.exe", "9", "hh.png");
                processImg(allFile.get(p), "D:\\ffmpeg-4.1.1-win64-static\\bin\\ffmpeg.exe", "10", "hh.png");
                processImg(allFile.get(p), "D:\\ffmpeg-4.1.1-win64-static\\bin\\ffmpeg.exe", "11", "hh.png");
                //listDir(new File("E:\\topic\\测试\\喜欢2\\v0200f6a0000blqtnicps0sghfamg6g011.png"),1);
            }

        }*/
        //将批量截图的三张图片拼接为一张
        for (int l = 0; l < allFile.size(); l++) {
            String path = allFile.get(l);
            if (path.contains(".mp4")) {
                List<File> files = new ArrayList<>();
                String newFileName = path.replace("mp4", "png");
                File file1 = new File(path.replace(".mp4", "aa.png"));
                File file2 = new File(path.replace(".mp4", "bb.png"));
                File file3 = new File(path.replace(".mp4", "cc.png"));
                File file4 = new File(path.replace(".mp4", "dd.png"));
                File file5 = new File(path.replace(".mp4", "ee.png"));
                File file6 = new File(path.replace(".mp4", "ff.png"));
                File file7 = new File(path.replace(".mp4", "gg.png"));
                File file8 = new File(path.replace(".mp4", "hh.png"));
                File file9 = new File(path.replace(".mp4", "yy.png"));
                File file10 = new File(path.replace(".mp4", "jj.png"));
                File file11 = new File(path.replace(".mp4", "kk.png"));
                File file12 = new File(path.replace(".mp4", "ll.png"));
                File file13 = new File(path.replace(".mp4", "mm.png"));
                File file14 = new File(path.replace(".mp4", "nn.png"));

                if (file1.exists()) {
                    files.add(file1);
                }
                if (file2.exists()) {
                    files.add(file2);
                }
                if (file3.exists()) {
                    files.add(file3);
                }
                if (file4.exists()) {
                    files.add(file4);
                }
                if (file5.exists()) {
                    files.add(file5);
                }
                if (file6.exists()) {
                    files.add(file6);
                }
                if (file7.exists()) {
                    files.add(file7);
                }
                if (file8.exists()) {
                    files.add(file8);
                }
                if (file9.exists()) {
                    files.add(file9);
                }
                if (file10.exists()) {
                    files.add(file10);
                }
                if (file11.exists()) {
                    files.add(file11);
                }
                if (file12.exists()) {
                    files.add(file12);
                }
                if (file13.exists()) {
                    files.add(file13);
                }
                if (file14.exists()) {
                    files.add(file14);
                }
                jointPic(files, newFileName);
            }
        }

       /*for (int a = 0 ;a<allFile.size();a++) {
            if (allFile.get(a).contains("aa.png")) {
                MediaFileUtils.delFile(allFile.get(a));
            }
            if (allFile.get(a).contains("bb.png")) {
                MediaFileUtils.delFile(allFile.get(a));
            }
            if (allFile.get(a).contains("cc.png")) {
                MediaFileUtils.delFile(allFile.get(a));
            }
           if (allFile.get(a).contains("dd.png")) {
               MediaFileUtils.delFile(allFile.get(a));
           }
           if (allFile.get(a).contains("ee.png")) {
               MediaFileUtils.delFile(allFile.get(a));
           }
           if (allFile.get(a).contains("ff.png")) {
               MediaFileUtils.delFile(allFile.get(a));
           }
           if (allFile.get(a).contains("gg.png")) {
               MediaFileUtils.delFile(allFile.get(a));
           }
           if (allFile.get(a).contains("hh.png")) {
               MediaFileUtils.delFile(allFile.get(a));
           }
           if (allFile.get(a).contains("yy.png")) {
               MediaFileUtils.delFile(allFile.get(a));
           }
           if (allFile.get(a).contains("jj.png")) {
               MediaFileUtils.delFile(allFile.get(a));
           }
           if (allFile.get(a).contains("kk.png")) {
               MediaFileUtils.delFile(allFile.get(a));
           }
           if (allFile.get(a).contains("ll.png")) {
               MediaFileUtils.delFile(allFile.get(a));
           }
           if (allFile.get(a).contains("mm.png")) {
               MediaFileUtils.delFile(allFile.get(a));
           }
           if (allFile.get(a).contains("nn.png")) {
               MediaFileUtils.delFile(allFile.get(a));
           }
        }*/
    }

    /**
     * 视频截图
     */
    public static boolean processImg(String veido_path, String ffmpeg_path, String seconds, String suffix) {
        File file = new File(veido_path);
        if (!file.exists()) {
            System.err.println("路径[" + veido_path + "]对应的视频文件不存在!");
            return false;
        }
        String cmm = ffmpeg_path + " -i " + veido_path + " -y -f image2 -ss " + seconds + " -s 360x640 " + veido_path
            .substring(0, veido_path.lastIndexOf("."))
            .replaceFirst("vedio", "file") + suffix;
        System.out.println(cmm);
        //MediaFileUtils.writeFile("E:\\bb.bat", cmm);
        List<String> commands = new java.util.ArrayList<String>();
        commands.add(ffmpeg_path);
        commands.add("-i");
        commands.add(veido_path);
        commands.add("-y");
        commands.add("-f");
        commands.add("image2");
        commands.add("-ss");
        commands.add(seconds);// 这个参数是设置截取视频多少秒时的画面
        // commands.add("-t");
        // commands.add("0.001");
        commands.add("-s");
        commands.add("360x640");
        commands.add(veido_path.substring(0, veido_path.lastIndexOf("."))
            .replaceFirst("vedio", "file") + suffix);
        System.out.println(commands);
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commands);
            builder.start();
            System.out.println("截取成功");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void shearVideo(String outPath) {
        try {
            long a = System.currentTimeMillis();
            MediaFileUtils.rename(outPath, MediaUtils.zimeikaVideoPath + a + ".mp4");
            Thread.sleep(1000);
            Runtime runtime = Runtime.getRuntime();
            //ffmpeg  -i aaa.mp4 -vcodec copy -acodec copy -ss 00:00:03 cc.mp4 -y
            String cut = ffmpegPath
                + " -i "
                + MediaUtils.zimeikaVideoPath + a + ".mp4"
                + " -vcodec copy -acodec copy -ss  00:00:03 "
                + outPath
                + " -y";
            Process exec = runtime.exec(cut);
            exec.isAlive();
            Thread.sleep(3000);
            MediaFileUtils.rename(MediaUtils.zimeikaVideoPath + a + ".mp4", outPath);
            MediaFileUtils.delFile(MediaUtils.zimeikaVideoPath + a + ".mp4");
            log.info("剪辑成功：" + outPath);
            /*final Thread t1 = new Thread(new Runnable() {

                @Override
                public void run() {
                    log.info("t1");
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
                        log.info(cut);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    log.info("t2");
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
                    log.info("t3");
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
