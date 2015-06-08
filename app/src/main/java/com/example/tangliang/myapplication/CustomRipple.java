package com.example.tangliang.myapplication;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import static android.util.Log.i;

/**
 * 自定义Ripple
 */
public class CustomRipple
{
    /** 波纹宿主控件 */
    private View host;
    /** 波纹宿主控件区域 */
    private Rect hostRect;
    /** 波纹半径 */
    private float radius;
    /** 波纹中心坐标 */
    private Point center;
    /** 波纹画笔 */
    private Paint paint;
    /** 快速波纹半径动画 = 半径动画+透明度动画 */
    private AnimatorSet fastRippleAnimator;
    /** 慢速波纹半径动画 */
    private ObjectAnimator slowRippleAnimator;
    /** 波纹状态监听器 */
    private IRippleStateListener stateListener;
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

    public CustomRipple(View host)
    {
        this.host = host;
        initRipplePaint();
        i("ttangliang", "CustomRipple()");
    }


    public void onTouch(MotionEvent event)
    {
        int x = (int) event.getX();//dig into:up和down事件的坐标会不会不一样
        int y = (int) event.getY();
        center = new Point(x, y);
        final float maxRippleRadius = getMaxRippleRadius(hostRect, center);

        int action = event.getAction();
        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
                cancelPreRipple();
                startSlowRipple(slowDuration, maxRippleRadius, slowInterpolator);
                break;
            case MotionEvent.ACTION_UP:
                cancelSlowRipple();
                startFastRipple(fastDuration, maxRippleRadius, fastInterpolator, rippleListener);
                break;
        }
    }

    /**
     * 开启快速波纹
     *
     * @param rippleDuration   波纹持续时间
     * @param maxRippleRadius  波纹最大半径
     * @param interpolator     波纹加速器
     * @param animatorListener 波纹状态监听器
     */
    private void startFastRipple(long rippleDuration,
                                 float maxRippleRadius,
                                 TimeInterpolator interpolator,
                                 Animator.AnimatorListener animatorListener)
    {
        //若慢速波纹未结束 则启动快速波纹动画
        if (radius < maxRippleRadius)
        {
            ObjectAnimator fastRadiusAnimator = ObjectAnimator.ofFloat(this, "radius", radius, maxRippleRadius);
            ObjectAnimator alphaAnimator = ObjectAnimator.ofInt(this, "alpha", alpha, 0);
            fastRippleAnimator = new AnimatorSet();
            fastRippleAnimator.playTogether(fastRadiusAnimator, alphaAnimator);
            fastRippleAnimator.setDuration(rippleDuration);
            fastRippleAnimator.setInterpolator(interpolator);
            fastRippleAnimator.addListener(animatorListener);
            fastRippleAnimator.start();
        }
        //若慢速波纹结束 则启动透明度动画
        else
        {
            ObjectAnimator alphaAnimator = ObjectAnimator.ofInt(this, "alpha", alpha, 0);
            alphaAnimator.setDuration(rippleDuration);
            alphaAnimator.setInterpolator(new LinearInterpolator());
            alphaAnimator.start();
        }
    }

    /**
     * 开启慢速波纹
     *
     * @param rippleDuration  波纹持续时间
     * @param maxRippleRadius 波纹最大半径
     * @param interpolator    波纹加速器
     */
    private void startSlowRipple(long rippleDuration,
                                 float maxRippleRadius,
                                 TimeInterpolator interpolator)
    {
        slowRippleAnimator = ObjectAnimator.ofFloat(this, "radius", 0, maxRippleRadius);
        slowRippleAnimator.setDuration(rippleDuration);
        slowRippleAnimator.setInterpolator(interpolator);
        slowRippleAnimator.start();
    }

    /**
     * 取消慢波纹
     */
    private void cancelSlowRipple()
    {
        //1.取消慢波纹动画
        if (slowRippleAnimator != null)
        {
            slowRippleAnimator.cancel();
            slowRippleAnimator = null;
        }
    }

    /**
     * 取消之前波纹 youhua:做成可接受多重波纹
     */
    private void cancelPreRipple()
    {
        if (fastRippleAnimator != null)
        {
            fastRippleAnimator.cancel();
            fastRippleAnimator = null;
        }
    }

    /**
     * 设置波纹宿主控件区域
     *
     * @param hostRect 波纹宿主控件区域
     */
    /**
     * 设置波纹宿主控件区域
     * @param left 控件左端
     * @param top 控件上端
     * @param right 控件右端
     * @param bottom 控件下段
     */
    public void setHostRect(int left,
                            int top,
                            int right,
                            int bottom)
    {
        this.hostRect = new Rect(left , top , right ,bottom);
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
     * Animator会自动调用这个方法来改变波纹透明度
     *
     * @param alpha 波纹透明度
     */
    public void setAlpha(int alpha)
    {
        paint.setAlpha(alpha);
    }

    /**
     * 绘制波纹
     *
     * @param canvas 画笔
     */
    protected void draw(Canvas canvas)
    {
        if (center == null)
        {
            return;
        }
        //绘制波纹
        switch (style)
        {
            case RECTANGLE:
                break;
            case CIRCLE:
                canvas.drawCircle(center.x, center.y, radius, paint);
                break;
        }
    }

    /**
     * 获得波纹最大半径(最大半径必须全部覆盖宿主控件)
     *
     * @param bounds     宿主控件边界
     * @param clickPoint 点击横坐标
     * @return 最大半径
     */
    private float getMaxRippleRadius(Rect bounds,
                                     Point clickPoint)
    {
        Point farmostPoint = getFarmostPoint(bounds, clickPoint);
        float dx = clickPoint.x - farmostPoint.x;
        float dy = clickPoint.y - farmostPoint.y;
        return (float) Math.sqrt(dx * dx + dy * dy);
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
     * 在区域内获得离点击位置最远的边界点
     *
     * @param bounds     区域
     * @param clickPoint 点击位置
     * @return 最远边界点
     */
    private Point getFarmostPoint(Rect bounds,
                                  Point clickPoint)
    {
        float centerToTop = clickPoint.y - bounds.top;
        float centerToBottom = bounds.bottom - clickPoint.y;
        float centerToLeft = clickPoint.x - bounds.left;
        float centerToRight = bounds.right - clickPoint.x;
        Point point = null;

        //A.常规嵌套if-else
        if (centerToLeft > centerToRight)
        {
            if (centerToTop > centerToBottom)
            {
                point = new Point(bounds.left, bounds.top);
            }
            else
            {
                point = new Point(bounds.left, bounds.bottom);
            }
        }
        else
        {
            if (centerToTop > centerToBottom)
            {
                point = new Point(bounds.right, bounds.top);
            }
            else
            {
                point = new Point(bounds.right, bounds.bottom);
            }
        }

//        //B.表驱动法
//        Point[][] boundsPoints = new Point[2][2] ;
//        boundsPoints[0][0] = new Point(bounds.left , bounds.top)  ;
//        boundsPoints[0][1] = new Point(bounds.right , bounds.top) ;
//        boundsPoints[1][0] = new Point(bounds.left , bounds.bottom)  ;
//        boundsPoints[1][1] = new Point(bounds.right , bounds.bottom) ;
//        int LeftOrRight = centerToRight > centerToLeft ? 1 : 0;
//        int topOrBottom = centerToBottom > centerToTop ? 1 : 0 ;
//        point = boundsPoints[LeftOrRight][topOrBottom] ;

        return point;
    }


    /**
     * 波纹状态监听器
     */
    private Animator.AnimatorListener rippleListener = new Animator.AnimatorListener()
    {
        @Override
        public void onAnimationStart(Animator animation)
        {
        }

        @Override
        public void onAnimationEnd(Animator animation)
        {
            restore();
            if (stateListener != null)
            {
                stateListener.onRippleEnd();
            }

        }

        @Override
        public void onAnimationCancel(Animator animation)
        {
            restore();
        }

        @Override
        public void onAnimationRepeat(Animator animation)
        {
        }
    };

    /**
     * 将波纹状态还原成动画开始之前
     */
    private void restore()
    {
        setRadius(0);
        setAlpha(themeManager.getRippleAlpha());
        host.setPressed(false);
    }

    /**
     * 波纹状态监听器
     */
    public interface IRippleStateListener
    {
        void onRippleEnd();
    }
}
