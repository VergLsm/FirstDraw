package com.example.vision.firstdraw;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

/**
 * Created by Vision on 15/6/2.
 */
public class DrawView extends View {
    private Animation[] animations;
    private int degMultiple;
    public Context context;
    private int count = 12; //总刻度数
    private Bitmap[] landmarks;
    private Bitmap earth;
    private int[] iconRes;
    private Bitmap[] appIcons;
    private Matrix matrix = new Matrix();
    private int x;
    private String[] titStr;


    public DrawView(Context context) {
        super(context);
        this.context = context;
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),
                MeasureSpec.getSize(widthMeasureSpec));
        x = MeasureSpec.getSize(widthMeasureSpec);
        this.setTranslationY(x / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(x / 2, x / 2); //将位置移动画纸的坐标点:150,150
        //初始化图片，禁止重入!!!!!
        if (earth == null) {
            landmarks = new Bitmap[count];
            appIcons = new Bitmap[count];
            earth = BitmapFactory.decodeResource(getResources(), R.mipmap.shenbian_lbs_earth);
            matrix.postScale((float) x / (float) earth.getWidth(), (float) x / (float) earth.getWidth());
            earth = Bitmap.createBitmap(earth, 0, 0, earth.getWidth(), earth.getHeight(),
                    matrix, true);
//            matrix.reset();
            matrix.postScale(x / 1.3f / (float) earth.getWidth(), x / 1.3f / (float) earth.getWidth());
            for (int i = 0; i < count; i++) {
                landmarks[i] = BitmapFactory.decodeResource(getResources(), R.mipmap.shenbian_lbs_landmark_1 + (i % 6));
                landmarks[i] = Bitmap.createBitmap(landmarks[i], 0, 0, landmarks[i].getWidth(), landmarks[i].getHeight(),
                        matrix, true);
                if (iconRes == null) continue;
                appIcons[i] = BitmapFactory.decodeResource(getResources(), iconRes[i]);
                appIcons[i] = toRoundCorner(appIcons[i], landmarks[i].getWidth());
            }
        }

        canvas.drawBitmap(earth, earth.getWidth() / -2, earth.getHeight() / -2, null);
        for (int i = 0; i < count; i++) {
            canvas.drawBitmap(landmarks[i], landmarks[i].getWidth() / -2, x / -1.38f, null);
//            canvas.drawLine(0f, y, 0, y + 12f, paint);
            if (null != appIcons[i])
                canvas.drawBitmap(appIcons[i], appIcons[i].getWidth() / -2, x / -1.427f, null);
            canvas.rotate(360 / count, 0f, 0f); //旋转画纸
        }
    }

    /**
     * 绘制圆形图标
     *
     * @param oldBitmap
     * @param pixels
     * @return Bitmap
     */
    private Bitmap toRoundCorner(Bitmap oldBitmap, int pixels) {
        // 定义矩阵对象
        Matrix matrix = new Matrix();
        // 缩放原图
        matrix.postScale(pixels / 1.4f / oldBitmap.getWidth(), pixels / 1.4f / oldBitmap.getWidth());
        //bmp.getWidth(), bmp.getHeight()分别表示缩放后的位图宽高
        Bitmap bitmap = Bitmap.createBitmap(oldBitmap, 0, 0, oldBitmap.getWidth(), oldBitmap.getHeight(),
                matrix, true);
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        int x = bitmap.getWidth();
        canvas.drawCircle(x / 2, x / 2, x / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        paint.setColor(Color.parseColor("#ffffff"));
        paint.setStrokeWidth(4);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(x / 2, x / 2, x / 2, paint);
        return output;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }


    public void initView(int[] iconRes, String[] titStr, int duration) {
        this.iconRes = iconRes;
        this.titStr = titStr;
        animations = new Animation[4];
        for (int i = 0; i < 4; i++) {
            animations[i] = new RotateAnimation(i * -90, ((i + 1) * -90), Animation.RELATIVE_TO_SELF,
                    0.5f, Animation.RELATIVE_TO_SELF, 1f);
            animations[i].setDuration(duration);
            animations[i].setFillAfter(true);
        }
    }

    public void turn() {
        this.startAnimation(animations[degMultiple]);
        degMultiple = (degMultiple + 1) % 4;
    }

}
