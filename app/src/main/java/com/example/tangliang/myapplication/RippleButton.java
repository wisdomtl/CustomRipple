package com.example.tangliang.myapplication;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.RippleDrawable;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by qq on 2015/5/24.
 */
public class RippleButton extends Button {

    /**�ؼ�����*/
    private RippleDrawable rippleDrawable  ;

    public RippleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        customRipple();
    }

    /**
     * ���ƿؼ�����
     */
    private void customRipple() {
        int rippleColor = ThemeManager.getInstance().getRippleColor() ;
        ColorStateList colorStateList = ColorStateList.valueOf(rippleColor) ;
        ColorDrawable background = new ColorDrawable(Color.GRAY);
        rippleDrawable = new RippleDrawable(colorStateList , background , null );
        this.setBackground(rippleDrawable);
    }
}
