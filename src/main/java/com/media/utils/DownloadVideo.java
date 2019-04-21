package com.media.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

public class DownloadVideo {

    private static String videoUrl = "http://v6-default.ixigua.com/ffcc97887419f86373cc10353fb95747/5cbaf230/video/m/220cb4ec0f662474640b93e92ec71d22be61161d5ae60000719a2c79beca/?rc=ajVndGl4dmpzbDMzNjczM0ApQHRAbzwzNjY3MzMzMzM0NDUzNDVvQGgzdSlAZjN1KWRzcmd5a3VyZ3lybHh3ZjUzQGpoaTIvYi5pcF8tLWAtMHNzLW8jbyM2LS0uLzAtLjIyNC0xNi06I28jOmEtcSM6YHZpXGJmK2BeYmYrXnFsOiMuL14=xigua-%E7%BE%8E%E9%A3%9F-%E6%89%93%E5%B7%A5%E5%A6%B9%E5%87%BA%E7%A7%9F%E5%B1%8B%E5%81%9A%E9%A6%99%E8%BE%A3%E7%8C%AA%E8%82%9D%EF%BC%8C%E4%B8%80%E5%A0%86%E8%BE%A3%E6%A4%92%E6%90%AD%E9%85%8D%E7%82%92%EF%BC%8C%E9%A6%99%E8%BE%A3%E5%AB%A9%E6%BB%91%EF%BC%8C%E7%B1%B3%E9%A5%AD%E4%B8%8D%E5%A4%9F%E5%90%83-109975-720p.mp4";
    private static final int MAX_BUFFER_SIZE = 1000000;

    public static void main(String[] args) throws Exception {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        RandomAccessFile randomAccessFile = null;
        try {
            // 1.获取连接对象
            URL url = new URL(URLDecoder.decode(videoUrl, "UTF-8"));
            connection = (HttpURLConnection) url.openConnection();
            System.out.println(url);
            connection.setRequestProperty("Range", "bytes=0-");
            connection.connect();
            if (connection.getResponseCode() / 100 != 2) {
                System.out.println("连接失败...");
                return;
            }
            // 2.获取连接对象的流
            inputStream = connection.getInputStream();
            //已下载的大小
            int downloaded = 0;
            //总文件的大小
            int fileSize = connection.getContentLength();
            String fileName = url.getFile();
            fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
            // 3.把资源写入文件
            randomAccessFile = new RandomAccessFile(fileName, "rw");
            while (downloaded < fileSize) {
                // 3.1设置缓存流的大小
                byte[] buffer = null;
                if (fileSize - downloaded >= MAX_BUFFER_SIZE) {
                    buffer = new byte[MAX_BUFFER_SIZE];
                } else {
                    buffer = new byte[fileSize - downloaded];
                }
                // 3.2把每一次缓存的数据写入文件
                int read = -1;
                int currentDownload = 0;
                long startTime = System.currentTimeMillis();
                while (currentDownload < buffer.length) {
                    read = inputStream.read();
                    buffer[currentDownload++] = (byte) read;
                }
                long endTime = System.currentTimeMillis();
                double speed = 0.0;
                if (endTime - startTime > 0) {
                    speed = currentDownload / 1024.0 / ((double) (endTime - startTime) / 1000);
                }
                randomAccessFile.write(buffer);
                downloaded += currentDownload;
                randomAccessFile.seek(downloaded);
                System.out
                    .printf("下载了进度:%.2f%%,下载速度：%.1fkb/s(%.1fM/s)%n", downloaded * 1.0 / fileSize * 10000 / 100, speed,
                        speed / 1000);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.disconnect();
                inputStream.close();
                randomAccessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
