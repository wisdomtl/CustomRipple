package com.example.tangliang.myapplication;

import android.animation.TimeInterpolator;
import android.app.Activity;
import android.graphics.Color;
import android.view.animation.LinearInterpolator;

/**
 * 主题管理器
 */
public class ThemeManager
{

    /** 当前主题 */
    private int theme;
    /** 快波纹持续时间 默认400ms */
    private long fastRippleDuration = 2300;
    /** 慢波纹持续时间 默认4000ms */
    private long slowRippleDuration = 2800;
    /** 波纹透明度 默认为27 */
    private int rippleAlpha = 91;
    /** 当前RippleEffect颜色 默认为亮灰色 */
    private int rippleColor = Color.parseColor("#5BFF0000");
    /** 波纹类型 */
    private TLRipple.Style rippleStyle = TLRipple.Style.CIRCLE;
    /** 快波纹半径加速器 默认为AccelerateDecelerateInterpolator */
    private TimeInterpolator fastRippleRadiusInterpolator = new SmoothInterpolator();
    /** 慢波纹半径加速器 默认为LinearInterpolator */
    private TimeInterpolator slowRippleRadiusInterpolator = new LinearInterpolator();

    public static ThemeManager getInstance()
    {
        return ThemeManagerHolder.INSTANCE;
    }

    private ThemeManager()
    {
    }

    private static class ThemeManagerHolder
    {
        public static final ThemeManager INSTANCE = new ThemeManager();
    }

    /**
     * 设置主题
     *
     * @param activity 需要更换主题的Activity
     * @param theme    主题
     */
    public void setTheme(Activity activity,
                         int theme)
    {
        this.theme = theme;
        activity.recreate();
    }

    /**
     * 获得波纹类型
     *
     * @return 波纹类型
     */
    public TLRipple.Style getRippleStyle()
    {
        return rippleStyle;
    }

    /**
     * 设置波纹类型
     *
     * @param rippleStyle 波纹类型
     */
    public void setRippleStyle(TLRipple.Style rippleStyle)
    {
        this.rippleStyle = rippleStyle;
    }


    /**
     * 设置快波纹持续时间
     *
     * @param fastRippleDuration 波纹持续时间
     */
    public void setFastRippleDuration(long fastRippleDuration)
    {
        this.fastRippleDuration = fastRippleDuration;
    }

    /**
     * 设置慢波纹持续时间
     *
     * @param slowRippleDuration 慢波纹持续时间
     */
    public void setSlowRippleDuration(long slowRippleDuration)
    {
        this.slowRippleDuration = slowRippleDuration;
    }

    public void setRippleAlpha(int rippleAlpha)
    {
        this.rippleAlpha = rippleAlpha;
    }

    /**
     * 设置慢波纹半径加速器
     *
     * @param slowRippleRadiusInterpolator 慢波纹半径加速器
     */
    public void setSlowRippleRadiusInterpolator(TimeInterpolator slowRippleRadiusInterpolator)
    {
        this.slowRippleRadiusInterpolator = slowRippleRadiusInterpolator;
    }

    /**
     * 设置快波纹半径加速器
     *
     * @param fastRippleRadiusInterpolator 快波纹半径加速器
     */
    public void setFastRippleRadiusInterpolator(TimeInterpolator fastRippleRadiusInterpolator)
    {
        this.fastRippleRadiusInterpolator = fastRippleRadiusInterpolator;
    }

    /**
     * 设置波纹颜色
     *
     * @param rippleColor 波纹颜色
     */
    public void setRippleColor(int rippleColor)
    {
        this.rippleColor = rippleColor;
    }


    public int getRippleAlpha()
    {
        return rippleAlpha;
    }

    /**
     * 获得快波纹持续时间
     *
     * @return 快波纹持续时间
     */
    public long getFastRippleDuration()
    {
        return fastRippleDuration;
    }

    /**
     * 获得慢波纹持续时间
     *
     * @return 慢波纹持续时间
     */
    public long getSlowRippleDuration()
    {
        return slowRippleDuration;
    }


    /**
     * 获得快波纹半径加速器
     *
     * @return 快波纹半径加速器
     */
    public TimeInterpolator getFastRippleRadiusInterpolator()
    {
        return fastRippleRadiusInterpolator;
    }

    /**
     * 获得慢波纹半径加速器
     *
     * @return 慢波纹半径加速器
     */
    public TimeInterpolator getSlowRippleRadiusInterpolator()
    {
        return slowRippleRadiusInterpolator;
    }


    /**
     * 获得波纹颜色
     *
     * @return 波纹颜色
     */
    public int getRippleColor()
    {
        return rippleColor;
    }

    /**
     * 切换主题回调(在setContentView()之前被调用才能更换主题)
     *
     * @param activity
     */
    public void onThemeChanged(Activity activity)
    {
        activity.setTheme(theme);
    }


    /**
     * 平滑波纹半径加速器
     */
    private static final class SmoothInterpolator implements TimeInterpolator
    {
        @Override
        public float getInterpolation(float input) {
            return 1 - (float) Math.pow(400, -input * 1.4);
        }
    }
}
