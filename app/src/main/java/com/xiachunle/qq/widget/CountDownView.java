package com.xiachunle.qq.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.xiachunle.qq.R;

import org.w3c.dom.Text;

/**
 * Created by xiachunle on 2017/2/9.
 * 自定义的计时按钮
 */

public class CountDownView extends View {
    private static final String TAG = CountDownView.class.getSimpleName();
    private static final int BACKGROUND_COLOR = 0x50555555;
    private static final float BORDER_WIDTH = 15f;
    private static final int BORDER_COLOR = 0xFF6ADBFE;
    private static final String TEXT = "跳过";
    private static final float TEXT_SIZE = 50f;
    private static final int TEXT_COLOR = 0xFFFFFFFF;

    private int backgroundColor;
    private float borderWidth;
    private int borderColor;
    private String text;
    private int textColor;
    private float textSize;

    private Paint circlePaint;
    private TextPaint textPaint;
    private Paint borderPaint;

    private float progress = 0;
    private StaticLayout staticLayout;

    private CountDownTimerListener listener;


    public CountDownView(Context context) {
        this(context, null);
    }
    public CountDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CountDownView);
        backgroundColor = typedArray.getColor(R.styleable.CountDownView_background_color, BACKGROUND_COLOR);
        borderWidth = typedArray.getDimension(R.styleable.CountDownView_border_width, BORDER_WIDTH);
        borderColor = typedArray.getColor(R.styleable.CountDownView_border_color, BORDER_COLOR);
        text = typedArray.getString(R.styleable.CountDownView_text);
        if (text == null) {
            text = TEXT;
        }
        textSize = typedArray.getDimension(R.styleable.CountDownView_text_size, TEXT_SIZE);
        textColor = typedArray.getColor(R.styleable.CountDownView_text_color, TEXT_COLOR);
        typedArray.recycle();
        init();

    }

    private void init() {
        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setDither(true);
        circlePaint.setColor(backgroundColor);
        circlePaint.setStyle(Paint.Style.FILL);

        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setDither(true);
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.CENTER);

        borderPaint = new Paint();
        borderPaint.setAntiAlias(true);
        borderPaint.setDither(true);
        borderPaint.setColor(borderColor);
        borderPaint.setStrokeWidth(borderWidth);
        borderPaint.setStyle(Paint.Style.STROKE);

        int textWidth = (int) textPaint.measureText(text.substring(0, (text.length() + 1) / 2));
        staticLayout = new StaticLayout(text, textPaint, textWidth, Layout.Alignment.ALIGN_NORMAL, 1F, 0, false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode != MeasureSpec.EXACTLY) {
            width = staticLayout.getWidth();
        }
        if (heightMode != MeasureSpec.EXACTLY) {
            height = staticLayout.getHeight();
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int min = Math.min(width, height);

        //底盘
        canvas.drawCircle(width / 2, height / 2, min / 2, circlePaint);
        //边框
        RectF rect;
        if (width > height) {
            rect = new RectF(width / 2 - min / 2 + borderWidth / 2, 0 + borderWidth / 2, width / 2 + min / 2 - borderWidth / 2, height - borderWidth / 2);
        } else {
            rect = new RectF(borderWidth / 2, height / 2 - min / 2 + borderWidth / 2, width - borderWidth / 2, height / 2 - borderWidth / 2 + min / 2);
        }
        canvas.drawArc(rect, -90, progress, false, borderPaint);
        //文字
        canvas.translate(width / 2, height / 2 - staticLayout.getHeight() / 2);
        staticLayout.draw(canvas);

    }

    public void start(){
        if(listener!=null){
            listener.onStatrtCount();
        }
        CountDownTimer countDownTimer=new CountDownTimer(6000,1000) {
            @Override
            public void onTick(long l) {
                progress=((6000-l)/3600f)*360;
                invalidate();
            }

            @Override
            public void onFinish() {
                progress=1000;
                invalidate();
                if(listener!=null){
                    listener.onFinishCount();
                }
            }
        }.start();
    }

    public void setCountDownTimerListener(CountDownTimerListener listener) {
        this.listener = listener;
    }

    public interface CountDownTimerListener{
        void onStatrtCount();
        void onFinishCount();
    }

}
