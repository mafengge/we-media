package com.media.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MediaUtils {
    public static String getCurrDate() {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format( now );
        return date;
    }
}
