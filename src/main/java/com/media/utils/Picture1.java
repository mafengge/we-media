package com.media.utils;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;


public class Picture1 {

/**
 * 竖向合成
 * 注意：该方法合成图片需要图片的宽度一样,高度不一致也可以（查阅网上知道的，实际上有时候就是不行，蓝瘦香菇。）
 * @param files	传入的图片
 */
public static void jointPic(List<File> files, String path) {
    try {
        Integer allWidth = 0;	// 图片总宽度
        Integer allHeight = 0;	// 图片总高度
        List<BufferedImage> imgs = new ArrayList<>();
        for(int i=0; i<files.size(); i++){
            System.out.println(files.get(i));
            imgs.add(ImageIO.read(files.get(i)));
            // 横向
            if (i==0) {
                allHeight = imgs.get(0).getHeight();
            }
            allWidth += imgs.get(i).getWidth();
        }
        BufferedImage combined = new BufferedImage(allWidth, allHeight, BufferedImage.TYPE_INT_RGB);
        // paint both images, preserving the alpha channels
        Graphics g = combined.getGraphics();
        // 横向合成
        Integer width = 0;
        for(int i=0; i< imgs.size(); i++){
            g.drawImage(imgs.get(i), width, 0, null);
            width +=  imgs.get(i).getWidth();
        }
        ImageIO.write(combined, "jpg", new File(path));
        resizeImage(path,1280,720);
        System.out.println("===合成成功====");
    } catch (Exception e) {
        System.out.println("===合成失败====");
        e.printStackTrace();
    }
}

    public static void resizeImage(String srcPath,
        int width, int height) throws IOException {

        File srcFile = new File(srcPath);
        Image srcImg = ImageIO.read(srcFile);
        BufferedImage buffImg = null;
        buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        //使用TYPE_INT_RGB修改的图片会变色
        buffImg.getGraphics().drawImage(
            srcImg.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0,
            0, null);

        ImageIO.write(buffImg, "JPEG", new File(srcPath));
    }
    public static void listDir(File dir, int level) throws IOException {
        String dirString = dir.toString();
        File files[] = dir.listFiles();
        level++;
                BufferedImage bufferedimage = ImageIO.read(dir);
                int width = bufferedimage.getWidth();
                int height = bufferedimage.getHeight();
                System.out.println(width);
                System.out.println(height);
                //目标将图片裁剪成 宽500，高300
                bufferedimage = cropImage(bufferedimage, 0, (int) ((height - 300) / 2), (int) (width),
                    (int) (height - (height - 300) / 2)
                );
                ImageIO.write(bufferedimage, "jpg", dir);    //输出裁剪图片
    }

    /**
     * 裁剪图片方法
     *
     * @param bufferedImage 图像源
     * @param startX 裁剪开始x坐标
     * @param startY 裁剪开始y坐标
     * @param endX 裁剪结束x坐标
     * @param endY 裁剪结束y坐标
     */
    public static BufferedImage cropImage(BufferedImage Image, int startX, int startY, int endX, int endY) {
        int width = Image.getWidth();
        int height = Image.getHeight();
        if (startX == -1) {
            startX = 0;
        }
        if (startY == -1) {
            startY = 0;
        }
        if (endX == -1) {
            endX = width - 1;
        }
        if (endY == -1) {
            endY = height - 1;
        }
        BufferedImage result = new BufferedImage(endX - startX, endY - startY, 4);
        for (int x = startX; x < endX; ++x) {
            for (int y = startY; y < endY; ++y) {
                int rgb = Image.getRGB(x, y);
                result.setRGB(x - startX, y - startY, rgb);
            }
        }
        return result;
    }
    public static void main(String[] args) throws IOException {
        //listDir(new File("E:\\topic\\测试\\喜欢2\\v0200f6a0000blqtnicps0sghfamg6g011.jpg"),1);
        List<File> files = new ArrayList<>();
        resizeImage("E:\\topic\\测试\\喜欢2\\199fde215e6b450ea4c403e33a0f792c.jpg",1280,720);
    }
}

