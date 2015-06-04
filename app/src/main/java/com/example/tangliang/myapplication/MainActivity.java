package com.example.tangliang.myapplication;

import android.animation.TimeInterpolator;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity
{

    private Button btnCustomRippleColor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ThemeManager.getInstance().onThemeChanged(this);
        setContentView(R.layout.activity_main);
        btnCustomRippleColor = (Button) findViewById(R.id.btnCustomRippleColor);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
    }


    /**
     * 通过Java改变波纹颜色(onClick方法)
     * @param v
     */
    public void changeRippleColorByJava(View v)
    {
        RippleDrawable background = (RippleDrawable) btnCustomRippleColor.getBackground();
        background.setColor(ColorStateList.valueOf(Color.GREEN));
    }

    /**
     * 通过主题变更改变波纹颜色(onClick方法)
     * @param v
     */
    public void changeRippleColorByTheme(View v)
    {
        ThemeManager.getInstance().setTheme(this, R.style.greenTheme);
    }



}
