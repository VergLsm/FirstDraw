package com.example.vision.firstdraw;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Vision on 15/6/3.
 */
public class DrawViewGroup extends RelativeLayout {
    private DrawView view;
    private TextView[] tvTit;
    private Animation[] textViewAnimations;
    private DrawViewGroup.OnClickListener onClickListener;

    /**
     * 标题文字显示指针
     */
    private int textCurrent;
    /**
     * 图片标题名
     */
    private String[] titStr;

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            for (int i = 0; i < 3; i++) {
                tvTit[i].startAnimation(textViewAnimations[0]);
            }
            view.turn();
        }
    };

    public DrawViewGroup(Context context) {
        super(context);
    }

    public DrawViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(widthSize, widthSize * 5 / 8);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            int area = (int) event.getX() * 3 / getWidth();//点击了哪个区域？(0~2)
            onClickListener.onClick((textCurrent + titStr.length - 3 + area) % titStr.length);
        }
        return true;
    }

    public void setOnClickListener(DrawViewGroup.OnClickListener listener) {
        this.onClickListener = listener;
    }


    public void initView(int[] iconRes, String[] titStr, int delay, int duration) {
        this.titStr = titStr;
        view = (DrawView) getChildAt(0);
        view.initView(iconRes, titStr, duration);

        textViewAnimations = new Animation[2];
        for (int i = 0; i < 2; i++) {
            textViewAnimations[i] = new ScaleAnimation((i + 1) % 2, i % 2, (i + 1) % 2,
                    i % 2, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1f);
            textViewAnimations[i].setFillAfter(true);
            textViewAnimations[i].setDuration(duration / 3);
        }
        textViewAnimations[1].setStartOffset(duration / 2);
        textViewAnimations[0].setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                for (int i = 0; i < 3; i++) {
                    tvTit[i].setText(DrawViewGroup.this.titStr[textCurrent]);
                    textCurrent = (textCurrent + 1) % DrawViewGroup.this.titStr.length;
                    tvTit[i].startAnimation(textViewAnimations[1]);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        tvTit = new TextView[3];
        tvTit[0] = (TextView) getChildAt(2);
        tvTit[1] = (TextView) getChildAt(1);
        tvTit[2] = (TextView) getChildAt(3);
        textCurrent = titStr.length - 1;
        for (int i = 0; i < 3; i++) {
            tvTit[i].setText(this.titStr[textCurrent]);
            textCurrent = (textCurrent + 1) % titStr.length;
        }

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        }, delay, delay);

    }

    /**
     * Interface definition for a callback to be invoked when a view is clicked.
     */
    public interface OnClickListener {
        /**
         * Called when a view has been clicked.
         *
         * @param selectedItem The number that was clicked.
         */
        void onClick(int selectedItem);
    }

}
