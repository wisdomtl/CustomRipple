package com.example.tangliang.myapplication;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

/**
 * 多重波纹
 */
public class Ripples
{
    /** 多重波纹集合 */
    private CustomRipple2[] ripples;
    /** 多重波纹最大个数 */
    private static final int MAX_RIPPLES_NUM = 5;
    /** 新产生单个波纹 */
    private CustomRipple2 curRipple;
    /** 多重波纹个数 */
    private int ripplesNum = 0;
    /** 多重波纹宿主 */
    private View host;
    /** 多重波纹宿主区域 */
    private Rect hostRect;

    public Ripples(View host)
    {
        this.host = host;
    }

    /**
     * 触摸事件处理
     *
     * @param event 触摸事件
     */
    public void onTouch(MotionEvent event)
    {
        int x = (int) event.getX();//dig into:up和down事件的坐标会不会不一样
        int y = (int) event.getY();
        Point center = new Point(x, y);
        final float maxRippleRadius = getMaxRippleRadius(hostRect, center);

        int action = event.getAction();
        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
                startSlowRipple(center, maxRippleRadius);
                break;
            case MotionEvent.ACTION_UP:
                startFastRipple();
                break;
        }
    }


    /**
     * 绘制多重波纹
     *
     * @param canvas 画布
     */
    public void draw(Canvas canvas)
    {
        //1.绘制快波纹
        if (ripplesNum > 0)
        {
            for (int i = 0; i < ripplesNum; i++)
            {
                ripples[i].draw(canvas);
            }
        }

        //2.绘制慢波纹
        if (curRipple != null)
        {
            curRipple.draw(canvas);
        }

    }

    /**
     * 设置波纹宿主控件区域
     *
     * @param left   控件左端
     * @param top    控件上端
     * @param right  控件右端
     * @param bottom 控件下段
     */
    public void setHostRect(int left,
                            int top,
                            int right,
                            int bottom)
    {
        this.hostRect = new Rect(left, top, right, bottom);
    }

    /**
     * 开启慢速波纹
     *
     * @param maxRippleRadius 波纹最大半径
     */
    private void startSlowRipple(Point center,
                                 float maxRippleRadius)
    {
        //当用户疯狂点击 波纹数量超过上限时 不在绘制
        if (ripplesNum >= MAX_RIPPLES_NUM)
        {
            return;
        }
        if (curRipple == null)
        {
            curRipple = new CustomRipple2(host, center, maxRippleRadius);
        }

        curRipple.startSlowRipple();
    }

    /**
     * 开启快速波纹
     */
    private void startFastRipple()
    {
        if (curRipple != null)
        {
            if (ripples == null)
            {
                ripples = new CustomRipple2[MAX_RIPPLES_NUM];
            }
            //把慢波纹动画记录在列表中
            ripples[ripplesNum++] = curRipple;
            curRipple.startFastRipple();
            curRipple = null; //当慢波纹变成快波纹后 慢波纹就该被销毁 以便再次点击时重新创建
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
}
