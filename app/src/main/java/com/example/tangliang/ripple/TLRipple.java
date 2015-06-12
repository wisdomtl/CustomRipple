package com.example.tangliang.ripple;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;

import com.example.tangliang.myapplication.ThemeManager;

/**
 * 单个波纹
 */
public class TLRipple
{
    /** 波纹宿主控件 */
    private View host;
    /** 波纹半径 */
    private float radius;
    /** 波纹最大半径 */
    private float maxRadius;
    /** 波纹中心坐标 */
    private Point center;
    /** 波纹画笔 */
    private Paint paint;
    /** 半径动画 */
    private ObjectAnimator radiusAnimator;
    /** 单个波纹监听器 */
    private TLRipples.IRippleListener iRippleListener;
    /** 主题管理器 */
    private ThemeManager themeManager = ThemeManager.getInstance();
    /** 波纹类型 */
    private Style style = themeManager.getRippleStyle();
    /** 波纹颜色 */
    private int color = themeManager.getRippleColor();
    /** 波纹透明度 */
    private int alpha = themeManager.getRippleAlpha();
    /** 快速波纹持续时间 */
    private long fastDuration = themeManager.getFastRippleDuration();
    /** 慢速波纹持续时间 */
    private long slowDuration = themeManager.getSlowRippleDuration();
    /** 快速波纹半径加速器 */
    private TimeInterpolator fastInterpolator = themeManager.getFastRippleRadiusInterpolator();
    /** 慢速波纹半径加速器 */
    private TimeInterpolator slowInterpolator = themeManager.getSlowRippleRadiusInterpolator();

    /** 波纹类型 RECTANGLE:矩形波纹 CIRCLE:圆形波纹 */
    public enum Style
    {
        RECTANGLE, CIRCLE
    }

    public TLRipple(View host,
                    TLRipples.IRippleListener iRippleListener,
                    Point center,
                    float maxRadius)
    {
        this.host = host;
        this.iRippleListener = iRippleListener;
        this.center = center;
        this.maxRadius = maxRadius;
        initRipplePaint();
    }


    /**
     * 开启快速波纹 = 半径动画 + 透明度动画
     */
    protected void startFastRipple()
    {
        radiusAnimator = ObjectAnimator.ofFloat(this, "radius", radius, maxRadius);
        radiusAnimator.setDuration(fastDuration) ;
        radiusAnimator.setAutoCancel(true);
        ObjectAnimator alphaAnimator = ObjectAnimator.ofInt(this, "alpha", alpha, 0);
        alphaAnimator.setDuration((int)(fastDuration*0.5)) ; //透明度动画比半径动画提前结束 否则效果不好看
        alphaAnimator.setAutoCancel(true);
        AnimatorSet fastRippleAnimator = new AnimatorSet();
        fastRippleAnimator.playTogether(radiusAnimator, alphaAnimator);
        fastRippleAnimator.setInterpolator(fastInterpolator);
        fastRippleAnimator.addListener(rippleListener);
        fastRippleAnimator.start();
    }

    /**
     * 开启慢速波纹
     */
    protected void startSlowRipple()
    {
        radiusAnimator = ObjectAnimator.ofFloat(this, "radius", 0, maxRadius);
        radiusAnimator.setAutoCancel(true);
        radiusAnimator.setDuration(slowDuration);
        radiusAnimator.setInterpolator(slowInterpolator);
        radiusAnimator.start();
    }

    /**
     * Animator会自动调用这个方法来改变波纹透明度
     *
     * @param alpha 波纹透明度
     */
    public void setAlpha(int alpha)
    {
        paint.setAlpha(alpha);
    }

    /**
     * Animator会自动调用这个方法来改变波纹半径
     *
     * @param radius 波纹半径
     */
    public void setRadius(float radius)
    {
        this.radius = radius;
        //波纹脏区域:以减少每次绘制量
        Rect dirtyBounds = getDirtyBounds();
        host.invalidate(dirtyBounds);
    }

    
    /**
     * 获得波纹脏区域(脏区域为圆外包矩形)
     *
     * @return 波纹脏区域
     */
    private Rect getDirtyBounds()
    {
        int left = center.x - (int) radius;
        int top = center.y - (int) radius;
        int right = center.x + (int) radius;
        int bottom = center.y + (int) radius;
        return new Rect(left, top, right, bottom);
    }

    /**
     * 绘制单个波纹
     *
     * @param canvas 画布
     */
    protected void draw(Canvas canvas)
    {
        final Point _center = center;
        if (_center == null)
        {
            return;
        }
        //绘制单个波纹
        switch (style)
        {
            case RECTANGLE:
                break;
            case CIRCLE:
                canvas.drawCircle(_center.x, _center.y, radius, paint);
                break;
        }
    }


    /**
     * 初始化波纹画笔
     */
    private void initRipplePaint()
    {
        if (paint == null)
        {
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(color);
            paint.setAlpha(alpha);
        }
    }


    /**
     * 波纹状态监听器
     */
    private final Animator.AnimatorListener rippleListener = new Animator.AnimatorListener()
    {
        @Override
        public void onAnimationStart(Animator animation)
        {
        }

        @Override
        public void onAnimationEnd(Animator animation)
        {
            if (iRippleListener != null)
            {
                iRippleListener.onRippleEnd(TLRipple.this);
            }
        }

        @Override
        public void onAnimationCancel(Animator animation)
        {
        }

        @Override
        public void onAnimationRepeat(Animator animation)
        {
        }
    };
}
