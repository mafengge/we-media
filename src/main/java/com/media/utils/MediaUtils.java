package com.media.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MediaUtils {

    public static final String zimekaInfoPath = "D:" + File.separator + "zimeika" + File.separator + "info.txt";

    public static final String zimeikaVideoPath =
        "D:" + File.separator + "zimeika" + File.separator + "video" + File.separator + MediaUtils.getCurrDate()
            + File.separator;

    public static final String ffmpegPath = "D:/ffmpeg-4.1.1-win64-static/bin/ffmpeg.exe";

    public static String getCurrDate() {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(now);
        return date;
    }
    public static void main(String[] args) {
        final Thread t1 = new Thread(new Runnable() {

            @Override
            public void run() {
                System.out.println("t1");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        final Thread t2 = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    t1.join();
                } catch (InterruptedException e) {
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
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("t3");
            }
        });
        t3.start();
        t2.start();
        t1.start();
    }

}
