package com.example.tangliang.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Debug;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

import static android.util.Log.i;
import static android.util.Log.v;
import static android.util.Log.w;

/**
 * Created by Tangliang on 2015/5/22.
 */
public class CustomRippleButton extends Button
{
    private CustomRipple ripple = new CustomRipple(this);

    public CustomRippleButton(Context context,
                              AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event)
    {
        ripple.onTouch(event);
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        ripple.draw(canvas);
    }
}
