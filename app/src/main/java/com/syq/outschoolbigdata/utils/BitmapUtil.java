package com.syq.outschoolbigdata.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by yfb on 2018/3/9.
 */

public class BitmapUtil {
    /**
     * 显示图片
     * @param context 上下文
     * @param url     图片地址
     * @param imageView
     */
    public static void displayBitmap(Context context, String url, ImageView imageView){
        Glide.with(context).load(url).into(imageView);
    }

    public static void displayBitmapFromDrawable(Context context, int drawable, ImageView imageView){
        Glide.with(context).load("android.resource://com.syq.outschoolbigdata/drawable/"+drawable).into(imageView);
    }
}
