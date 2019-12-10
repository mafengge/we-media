package com.media.utils;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import java.awt.Color;

public interface QrCodeCardTemplate {
    int border_space = 40;
    int double_border_space = 80;
    int h = 440;
    int w = 740;
    //Color bg_color = ColorUtil.int2color(-99999);
    int text_size = 256;
    int text_x = 40;
    int text_y = 80;
    int text_logo_size = 100;
    int text_line_space = 10;
    //Font text_nameFont = FontUtil.BIG_BOLD_DEFAULT_FONT;
    Color text_nameFont_color = Color.WHITE;
    //Font text_descFont = FontUtil.DEFAULT_FONT;
    //Color text_descFont_color = ColorUtil.OFF_WHITE;
    int line_w = 40;
    int line_h = 220;
    int line_x = 296;
    int line_y = 60;
    Color line_color = Color.LIGHT_GRAY;
    int qrcode_info_w = 224;
    int qrcode_size = 220;
    int qrcode_x = 356;
    int qrcode_y = 40;
    int qrcode_info_padding = 5;

    int title_padding = 20;
}
