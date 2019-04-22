package com.media.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MediaUtils {

    public static final String zimekaInfoPath = "D:" + File.separator + "zimeika" + File.separator + "info.txt";

    public static final String zimeikaVideoPath =
        "D:" + File.separator + "zimeika" + File.separator + "video" + File.separator + MediaUtils.getCurrDate()
            + File.separator;

    public static String getCurrDate() {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(now);
        return date;
    }
}
