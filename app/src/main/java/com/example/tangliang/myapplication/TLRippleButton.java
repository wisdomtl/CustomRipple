package com.example.tangliang.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

import com.example.tangliang.ripple.TLRipples;

import static android.util.Log.i;
import static android.util.Log.v;
import static android.util.Log.w;

/**
 * 自定义波纹按钮(通过TLRipple自定义)
 */
public class TLRippleButton extends Button
{
    private TLRipples ripples = new TLRipples(this);

    public TLRippleButton(Context context,
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
}
