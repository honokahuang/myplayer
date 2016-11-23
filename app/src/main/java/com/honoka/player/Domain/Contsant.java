package com.honoka.player.Domain;

import android.content.Context;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.honoka.player.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Honoka on 2016/11/6.
 */

public class Contsant {
    /**
     * 获取屏幕的大小0：宽度 1：高度
     * */
    public static int[] getScreen(Context context) {
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        return new int[] { (int) (outMetrics.density * outMetrics.widthPixels),
                (int) (outMetrics.density * outMetrics.heightPixels) };
    }
    /**
     * 获取文件的后缀名，返回大写
     * */
    public static String getSuffix(String str) {
        int i = str.lastIndexOf('.');
        if (i != -1) {
            return str.substring(i + 1).toUpperCase();
        }
        return str;
    }
    /**
     * 格式化毫秒->00:00
     * */
    public static String formatSecondTime(int millisecond) {
        if (millisecond == 0) {
            return "00:00";
        }
        millisecond = millisecond / 1000;
        int m = millisecond / 60 % 60;
        int s = millisecond % 60;
        return (m > 9 ? m : "0" + m) + ":" + (s > 9 ? s : "0" + s);
    }

    /**
     * 格式化文件大小 Byte->MB
     * */
    public static String formatByteToMB(long l){
        float mb=l/1024f/1024f;
        return String.format("%.2f",mb);
    }
    /**
     * 根据文件路径获取文件目录
     * */
    public static String clearFileName(String str) {
        int i = str.lastIndexOf(File.separator);
        if (i != -1) {
            return str.substring(0, i + 1);
        }
        return str;
    }
    /**
     * 根据文件名获取不带后缀名的文件名
     * */
    public static String clearSuffix(String str) {
        int i = str.lastIndexOf(".");
        if (i != -1) {
            return str.substring(0, i);
        }
        return str;
    }
    /**
     * 根据文件路径获取不带后缀名的文件名
     * */
    public static String clearDirectory(String str) {
        int i = str.lastIndexOf(File.separator);
        if (i != -1) {
            return clearSuffix(str.substring(i + 1, str.length()));
        }
        return str;
    }
    /**
     * 检查SD卡是否已装载
     * */
    public static boolean isExistSdCard(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
    /**
     * 获得SD目录路径
     * */
    public static String getSdCardPath(){
        return Environment.getExternalStorageDirectory().getPath();
    }

    /**
     * 判断文件是否存在
     * */
    public static boolean isExistFile(String file){
        return new File(file).exists();
    }
    /**
     * 判断目录是否存在，不在则创建
     * */
    public static void isExistDirectory(String directoryName) {
        File file = new File(directoryName);
        if (!file.exists()) {
            file.mkdirs();
        }
    }
    /**
     * 修改文件名
     * */
    public static String renameFileName(String str){
        int i=str.lastIndexOf('.');
        if(i!=-1){
            File file=new File(str);
            file.renameTo(new File(str.substring(0,i)));
            return str.substring(0,i);
        }
        return str;
    }
}
