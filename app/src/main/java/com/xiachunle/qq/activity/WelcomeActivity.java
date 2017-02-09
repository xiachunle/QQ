package com.xiachunle.qq.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.xiachunle.qq.R;
import com.xiachunle.qq.services.NormalLoadPicture;
import com.xiachunle.qq.services.SharedHelper;
import com.xiachunle.qq.widget.CountDownView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WelcomeActivity extends BaseActivity {
    private static final int TIMEFLAG = 0x0001;
    private static final int IMAGEFLAG = 0x0000;
    private static final String imageUrl = "http://xiachunle.com/images/kenan.jpg";

    private CountDownView mCountDownView;//进度条跳转
    private Button timeBtn;//倒计时跳转
    private ImageView startImage;

    private byte[] picByte;
    private Bitmap saveedBitmap;
    private SharedHelper sh;
    /**
     * 用于更新界面
     */
    private Handler timeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == TIMEFLAG) {
                timeBtn.setText(((long) msg.obj / 1000) + "s");
            } else if (msg.what == IMAGEFLAG) {
                if (picByte != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(picByte, 0, picByte.length);
                    startImage.setImageBitmap(bitmap);
                    savePicture(bitmap);
                }
            }
        }
    };

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sh = new SharedHelper(getApplicationContext());
        //透明化状态栏和导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        mCountDownView = (CountDownView) findViewById(R.id.id_countDown_text);

        mCountDownView.setCountDownTimerListener(new CountDownView.CountDownTimerListener() {
            @Override
            public void onStatrtCount() {
            }

            @Override
            public void onFinishCount() {

            }
        });
        mCountDownView.start();

        timeBtn = (Button) findViewById(R.id.id_countDown_btn);
        startImage = (ImageView) findViewById(R.id.id_image);
        startImage.setBackgroundColor(getColor(R.color.red));
        if(isNetWorkAvailable(getApplicationContext())){
            saveedBitmap=null;
        }else {
            saveedBitmap = sh.getBitmap();
        }
        if (saveedBitmap == null) {
            new Thread(runnable).start();
        } else {
            startImage.setImageBitmap(saveedBitmap);
        }
        CountDownTimer downTimer = new CountDownTimer(6000, 1000) {
            @Override
            public void onTick(final long l) {
                Message message = new Message();
                message.what = 1;
                message.obj = l;
                timeHandler.sendMessage(message);
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setReadTimeout(10000);
                if (conn.getResponseCode() == 200) {
                    InputStream fis = conn.getInputStream();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] bytes = new byte[1024];
                    int length = -1;
                    while ((length = fis.read(bytes)) != -1) {
                        bos.write(bytes, 0, length);
                    }
                    picByte = bos.toByteArray();
                    bos.close();
                    fis.close();
                    Message message = new Message();
                    message.what = IMAGEFLAG;
                    timeHandler.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private void savePicture(Bitmap bitmap) {
        String savePictureName = getApplicationContext().getFilesDir().getPath() + File.separator + "welcome" + ".jpg";
        Log.e("tt", savePictureName);
        sh.saveBitmap(savePictureName);
        File file = new File(savePictureName);
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {

        }

    }

    private static boolean isNetWorkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

}
