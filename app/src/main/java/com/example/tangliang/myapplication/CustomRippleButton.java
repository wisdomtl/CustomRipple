package com.example.tangliang.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Debug;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;

import static android.util.Log.i;
import static android.util.Log.v;
import static android.util.Log.w;

/**
 * 自定义波纹按钮(通过CustomRipple自定义)
 */
public class CustomRippleButton extends Button
{
    private Ripples ripples = new Ripples(this);

    public CustomRippleButton(Context context,
                              AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event)
    {
        ripples.onTouch(event);
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onLayout(boolean changed,
                            int left,
                            int top,
                            int right,
                            int bottom)
    {
        super.onLayout(changed, left, top, right, bottom);
        ripples.setHostRect(left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        ripples.draw(canvas);
    }

//    private CustomRipple ripple = new CustomRipple(this);
//
//    public CustomRippleButton(Context context,
//                              AttributeSet attrs)
//    {
//        super(context, attrs);
//    }
//
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent event)
//    {
//        ripple.onTouch(event);
//        return super.dispatchTouchEvent(event);
//    }
//
//    @Override
//    protected void onLayout(boolean changed,
//                            int left,
//                            int top,
//                            int right,
//                            int bottom)
//    {
//        super.onLayout(changed, left, top, right, bottom);
//        ripple.setHostRect(left, top, right, bottom);
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas)
//    {
//        super.onDraw(canvas);
//        ripple.draw(canvas);
//    }
}
