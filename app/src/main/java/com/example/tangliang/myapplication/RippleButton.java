package com.example.tangliang.myapplication;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.RippleDrawable;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * 自定义波纹按钮(通过RippleDrawable自定义)
 */
public class RippleButton extends Button {

    /**控件波纹*/
    private RippleDrawable rippleDrawable  ;

    public RippleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        customRipple();
    }

    /**
     * 定制控件波纹
     */
    private void customRipple() {
        int rippleColor = ThemeManager.getInstance().getRippleColor() ;
        ColorStateList colorStateList = ColorStateList.valueOf(rippleColor) ;
        ColorDrawable background = new ColorDrawable(Color.GRAY);
        rippleDrawable = new RippleDrawable(colorStateList , background , null );
        this.setBackground(rippleDrawable);
    }
}
