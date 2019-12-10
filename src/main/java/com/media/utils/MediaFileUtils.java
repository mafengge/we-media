package com.media.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

/**
 * @author: mafengge
 * @description:
 * @date: 21:18 2019/4/21
 */
@Slf4j
public class MediaFileUtils {

    public static void main(String args[]) {
        //writeFile(MediaUtils.zimekaInfoPath + "aa.txt", "aaa");
        //readFile(MediaUtils.zimekaInfoPath + "aa.txt");
        /*List<String> allFile = getAllFile(MediaUtils.zimeikaVideoPath, true);
        for (String str : allFile) {
            log.info(str);
        }*/
    }

    /**
     * 读入TXT文件
     */
    public static String readFile(String filePath) {
        String charset = "UTF-8";
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, charset);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuffer sb = new StringBuffer();
                String text = null;
                while ((text = bufferedReader.readLine()) != null) {
                    sb.append(text);
                }
                return sb.toString();
            } catch (Exception e) {
                log.info(e.getMessage());
            }
        } else {
            try {
                file.createNewFile();
                writeFile(filePath,"1");
            } catch (Exception e){
                System.out.println(e);
            }
        }
        return null;
    }

    /**
     * 清空文件
     * @param fileName
     */
    public static void clearInfoForFile(String fileName) {
        File file =new File(fileName);
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter =new FileWriter(file);
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 写入TXT文件
     */
    public static void writeFile(String txtPath, String str) {
        FileWriter fw = null;
        try {
            //如果文件存在，则追加内容；如果文件不存在，则创建文件
            File f = new File(txtPath);
            if (!f.exists())
                FileUtils.forceMkdirParent(f);
            fw = new FileWriter(f, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter pw = new PrintWriter(fw);
        pw.println(str);
        pw.flush();
        try {
            fw.flush();
            pw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取路径下的所有文件/文件夹
     *
     * @param directoryPath  需要遍历的文件夹路径
     * @param isAddDirectory 是否将子文件夹的路径也添加到list集合中
     */
    public static List<String> getAllFile(String directoryPath, boolean isAddDirectory) {
        List<String> list = new ArrayList<String>();
        File baseFile = new File(directoryPath);
        if (baseFile.isFile() || !baseFile.exists()) {
            return list;
        }
        File[] files = baseFile.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                if (isAddDirectory) {
                    list.add(file.getAbsolutePath());
                }
                list.addAll(getAllFile(file.getAbsolutePath(), isAddDirectory));
            } else {
                String absolutePath = file.getAbsolutePath();
                list.add(absolutePath);
            }
        }
        return list;
    }

    /**
     * 删除文件
     *
     * @return boolean
     */
    public static void delFile(String filePath) {
        try {
            File myDelFile = new File(filePath);
            myDelFile.delete();
            System.out.println("本地文件已删除：" + filePath);
        } catch (Exception e) {
            System.out.println("删除文件操作出错");
            e.printStackTrace();
        }
    }

    public static void rename(String file1, String file2) {
        try {
            File file = new File(file1);
            file.renameTo(new File(file2));
        } catch (Exception e) {
            log.info("修改文件名报错");
            e.printStackTrace();
        }
    }

    /**
     * 通过文件路径直接修改文件名
     *
     * @param filePath 需要修改的文件的完整路径
     * @param newFileName 需要修改的文件的名称
     */
    public static String FixFileName(String filePath, String newFileName) {
        File f = new File(filePath);
        if (!f.exists()) { // 判断原文件是否存在（防止文件名冲突）
            return null;
        }
        newFileName = newFileName.trim();
        if ("".equals(newFileName) || newFileName == null) // 文件名不能为空
        {
            return null;
        }
        String newFilePath = null;
        if (f.isDirectory()) { // 判断是否为文件夹
            newFilePath = filePath.substring(0, filePath.lastIndexOf("/")) + "/" + newFileName;
        } else {
            newFilePath = filePath.substring(0, filePath.lastIndexOf("/")) + "/" + newFileName
                    + filePath.substring(filePath.lastIndexOf("."));
        }
        File nf = new File(newFilePath);
        try {
            f.renameTo(nf); // 修改文件名
        } catch (Exception err) {
            err.printStackTrace();
            return null;
        }
        return newFilePath;
    }


}


