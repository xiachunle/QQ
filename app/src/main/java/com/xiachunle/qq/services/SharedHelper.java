package com.xiachunle.qq.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by xiachunle on 2017/2/9.
 */

public class SharedHelper {

    private static String TAG="welcome";
    private Context mContext;

    public SharedHelper(Context mContext) {
        this.mContext = mContext;
    }

    public void saveBitmap(String path){
        SharedPreferences sp=mContext.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString(TAG,path);
        editor.commit();
    }

    public Bitmap getBitmap(){
        SharedPreferences sp=mContext.getSharedPreferences(TAG,Context.MODE_PRIVATE);
       String path= sp.getString(TAG,"");
        Log.e("Test======",path);
        if(path==null||path.equals("")){
            return null;
        }else {
            try {
                FileInputStream fis=new FileInputStream(path);
                return BitmapFactory.decodeStream(fis);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }


        }


    }
}
