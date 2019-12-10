package com.media.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FFMpegUtils {

    private String ffmpegEXE;

    public FFMpegUtils(String ffmpegEXE) {
        super();
        this.ffmpegEXE = ffmpegEXE;
    }

    public static void searFile() {

    }

    /**
     * 遍历目录下所有mp4转换成ts格式
     */
    public static void copy(String path, String outPath) throws Exception {
        //ffmpeg.exe -i D://douyin//GetDouYin//download//meinv//meinv3.mp4 -c copy -vbsf h264_mp4toannexb D://douyin//GetDouYin//download//meinv//meinv3.ts
        File file = new File(path);
        File[] files = file.listFiles();
        for (int a = 0; a < files.length; a++) {
            String fileName = files[a].getName().replace(" ", "");
            files[a].renameTo(new File(path + fileName.replaceAll(".ts",".mp4")));
                System.out.println("已拷贝：" + fileName);
        }
    }

    public static void convertor(String path, String outPath) throws Exception {
        //ffmpeg.exe -i D://douyin//GetDouYin//download//meinv//meinv3.mp4 -c copy -vbsf h264_mp4toannexb D://douyin//GetDouYin//download//meinv//meinv3.ts
        File file = new File(path);
        File[] files = file.listFiles();
        for (int a = 0; a < files.length; a++) {
            String fileName = files[a].getName().replace(" ", "");
                if (fileName.contains(".mp4")) {
                    String commands = MediaUtils.ffmpegPath + " -i "
                        + path + fileName + " -c copy -vbsf h264_mp4toannexb "
                        + outPath + fileName.replace(".mp4", "") + ".ts";
                    System.out.println(commands);
                    MediaFileUtils.writeFile("E:\\dd.bat",commands);
                    /*new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Runtime runtime = Runtime.getRuntime();
                                Process exec = runtime.exec(commands);
                                *//*if (exec.waitFor() == 0) {
                                MediaFileUtils.delFile(path + fileName);
                            }*//*
                            } catch (Exception e) {
                                System.out.println(e);
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    Thread.sleep(3000);*/
            }
        }
    }

    public static void tsMarge(String tsPath, String videoOutputPath, int len) throws InterruptedException {
        File file = new File(tsPath);
        File[] files = file.listFiles();
        List<String> list = new ArrayList<>();
        /*File file1 = new File(tsPath + "搞笑");
        String[] fileName = file1.list();
        System.out.println(fileName[0]);
        sb.append(tsPath + "搞笑\\" + fileName[0] + "|");*/
        File file1 = new File("E:\\topic\\喜欢成品\\封面\\");
        File[] files1 = file1.listFiles();
        int b = 0;
        for (int a = 0; a < files.length; a++) {
            String name = files[a].getName();
            if (name.contains(".ts")) {
                list.add(name);
                if (list.size() == len) {
                    b = ++b;
                    //System.out.println(b);
                    StringBuffer sb = new StringBuffer();
                    for (String l : list) {
                        sb.append(tsPath + l + "|");
                    }
                    //sb.append(tsPath + "搞笑\\" + fileName[1] + "|");
                    String sbs = sb.toString();
                    sbs = sbs.substring(0, sbs.length() - 1);
                    String[] split = sbs.split("\\|");
                    list.clear();
                    //System.out.println(sbs);
                    String commands = MediaUtils.ffmpegPath + " -i \"concat:"
                        + sbs
                        + "\" -acodec copy -vcodec copy -absf aac_adtstoasc "
                        + videoOutputPath + "s"+b + ".mp4";
                    System.out.println(b);
                    System.out.println(commands);
                    MediaFileUtils.writeFile("E:\\dd.bat",commands);
                    /*Thread.sleep(2000);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //System.out.println(commands + " ");
                                Runtime runtime = Runtime.getRuntime();
                                Process exec = null;

                                exec = runtime.exec(commands);
                                if (exec.waitFor() == 0) {
                                    *//*for (int b=0;b<split.length;b++) {
                                        MediaFileUtils.delFile(split[b]);
                                    }*//*
                                }
                            } catch (Exception e) {
                                System.out.println(e);
                                e.printStackTrace();
                            }

                        }
                    }).start();*/
                }
            }

        }
    }

    public static void lastMarge(String tsPath, String videoOutputPath, int len)
        throws InterruptedException, IOException {
        File file = new File(tsPath);
        File[] files = file.listFiles();

        File file1 = new File("E:\\topic\\500bb\\");
        File[] files1 = file1.listFiles();

        File file2 = new File("E:\\topic\\个人\\");
        File[] files2 = file2.listFiles();

        /*File file3 = new File("E:\\topic\\喜欢2\\");
        File[] files3 = file3.listFiles();*/

        List<String> list = new ArrayList<>();
        /*File file1 = new File(tsPath + "搞笑");
        String[] fileName = file1.list();
        System.out.println(fileName[0]);
        sb.append(tsPath + "搞笑\\" + fileName[0] + "|");*/
        int b = 0;
        for (int a = 0; a < files.length; a++) {
            String name = file.getPath() + "\\" + files[a].getName();
            if (name.contains(".ts")) {
                System.out.println(files1[b].getName());
                String name1 = file1.getPath() + "\\" + files1[b].getName();
                //String name2 = file2.getPath() + "\\" + files2[a].getName();
                //String name3 = file3.getPath() + "\\" + files3[a].getName();
                list.add(name);
                if (list.size() == 2) {
                    list.add(name1);
                }
                //list.add(name2);
                //list.add(name3);
                String namm = "Mejores Street Fashion Tik Tok Douyin China Ep." + a;
                if (list.size() == len) {
                    b = ++b;
                    System.out.println(b);
                    StringBuffer sb = new StringBuffer();
                    for (String l : list) {
                        sb.append(l + "|");
                    }
                    //sb.append(tsPath + "搞笑\\" + fileName[1] + "|");
                    String sbs = sb.toString();
                    sbs = sbs.substring(0, sbs.length() - 1);
                    String[] split = sbs.split("\\|");
                    list.clear();
                    System.out.println(sbs);
                    String commands = MediaUtils.ffmpegPath + " -i \"concat:"
                        + sbs
                        + "\" -acodec copy -vcodec copy -absf aac_adtstoasc "
                        + videoOutputPath + files1[b-1].getName().replace(".ts",".mp4");
                    System.out.println(commands);
                    MediaFileUtils.writeFile("E:\\bb.bat",commands);
                    /*File f = new File("E:\\topic\\likefinish\\cover1\\"+files[a].getName().replace(".ts",".png"));
                    f.renameTo(new File("E:\\topic\\likefinish\\cover1\\"+namm+".png"));
                    */
                    /*new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                System.out.println(commands + " ");
                                Runtime runtime = Runtime.getRuntime();
                                Process exec = null;

                                exec = runtime.exec(commands);
                                if (exec.waitFor() == 0) {
                                    *//*for (int b=0;b<split.length;b++) {
                                        MediaFileUtils.delFile(split[b]);
                                    }*//*
                                }
                            } catch (Exception e) {
                                System.out.println(e);
                                e.printStackTrace();
                            }

                        }
                    }).start();*/

                }
            }

        }
    }

    public static void renameFile() {
        try {
            File file = new File("E:\\topic\\likefinish\\finish2\\");
            File[] files = file.listFiles();
            int b = 1;
           /* Arrays.sort(files, new Comparator<File>() {
                @Override
                public int compare(File f1, File f2) {
                    long diff = f1.lastModified() - f2.lastModified();
                    if (diff > 0) {
                        return 1;
                    } else if (diff == 0) {
                        return 0;
                    } else {
                        return -1;//如果 if 中修改为 返回-1 同时此处修改为返回 1  排序就会是递减
                    }
                }

                @Override
                public boolean equals(Object obj) {
                    return true;
                }

            });*/
            for (int i = 0; i < files.length; i++) {
                String name = files[i].getName();
                String substring = name.substring(name.indexOf("."), name.length());
                String newName = "Street Style Funny Tik Tok Douyin China" + substring;
                if (name.contains(".png")) {
                    File file1 = new File("E:\\topic\\likefinish\\finish2\\" + newName);
                    File file2 = files[i];
                    if(file2.exists()) {
                        file2.renameTo(file1);
                    }
                    b = ++b;

                    /*File file3 = new File("E:\\topic\\likefinish\\cover2\\"+name.replace(".mp4",".png"));
                    File file4 = new File("E:\\topic\\likefinish\\cover2\\" + newName + ".png");
                    if(file3.exists()){
                        file3.renameTo(file4);
                    }*/
                }
            /*if (name.contains(".png")) {
                File file1 = new File("E:\\topic\\喜欢成品\\成品\\" + newName + ".png");
                files[i].renameTo(file1);
            }*/
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public static void main(String[] args) {
        try {
            String path = "E:\\topic\\500ts\\";
            String outPath = "E:\\topic\\500finish\\";
            //convertor(path, outPath);
            //tsMarge(path, outPath, 20);
            //lastMarge(path,outPath,5);
            //renameFile();
            int b=1;
            File file = new File("D:\\youtube\\street22\\like47\\");
            File[] files1 = file.listFiles();
            for(File f :files1){
                if (f.getName().contains(".mp4")){
                    b++;
                    System.out.println(f.getName());
                    System.out.println(f.getPath());
                    String a = f.getName();
                    //Long legs|Bikini|Street Fashion|Dance Collection Tik Tok China.
                    f.renameTo(new File("D:\\youtube\\street22\\like47\\"+b+".mp4"));
                    //String aaa = "D:/ffmpeg-4.1.1-win64-static/bin/ffmpeg.exe -i E:\\topic\\likefinish\\finish4\\"+a+" -vf scale=1080:1920 D:\\youtube\\finish4\\"+a+" -hide_banner";
                    //MediaFileUtils.writeFile("E:\\bb.bat",aaa);
                }
            }
            //copy(path,outPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}