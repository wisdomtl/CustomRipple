package com.example.tangliang.myapplication;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

/**
 * ���ز���
 */
public class TLRipples
{
    /** ���ز��Ƽ��� */
    private TLRipple[] ripples;
    /** ���ز��������� */
    private static final int MAX_RIPPLES_NUM = 10;
    /** ��ǰ���ز��Ƹ��� */
    private int ripplesNum = 0;
    /** �²����������� */
    private TLRipple curRipple;
    /** ���ز������� */
    private View host;
    /** ���ز����������� */
    private Rect hostRect;
    /**���ز��Ƽ�����*/
    private IRipplesListener iRipplesListener ;

    public TLRipples(View host)
    {
        this.host = host;
    }

    /**
     * �����¼�����
     *
     * @param event �����¼�
     */
    public void onTouch(MotionEvent event)
    {
        int x = (int) event.getX();//dig into:up��down�¼�������᲻�᲻һ��
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
     * ���ƶ��ز���
     *
     * @param canvas ����
     */
    public void draw(Canvas canvas)
    {
//        final int _ripplesNum = ripplesNum ;
//        final TLRipple[] _ripples = ripples ;
//        final TLRipple _curRipple = curRipple ;
//
//        //1.���ƿ첨��
//        if (_ripplesNum > 0)
//        {
//            for (int i = 0; i < _ripplesNum; i++)
//            {
//                _ripples[i].draw(canvas);
//            }
//        }
//
//        //2.����������
//        if (_curRipple != null)
//        {
//            _curRipple.draw(canvas);
//        }

         //1.���ƿ첨��
        if (ripplesNum > 0)
        {
            for (int i = 0; i < ripplesNum; i++)
            {
                ripples[i].draw(canvas);
            }
        }

        //2.����������
        if (curRipple != null)
        {
            curRipple.draw(canvas);
        }
    }


    /**
     * ���ò��������ؼ�����
     *
     * @param left   �ؼ����
     * @param top    �ؼ��϶�
     * @param right  �ؼ��Ҷ�
     * @param bottom �ؼ��¶�
     */
    public void setHostRect(int left,
                            int top,
                            int right,
                            int bottom)
    {
        this.hostRect = new Rect(left, top, right, bottom);
    }

    /**
     * ���ö��ز��Ƽ�����
     * @param iRipplesListener ���ز��Ƽ�����
     */
    public void setiRipplesListener(IRipplesListener iRipplesListener)
    {
        this.iRipplesListener = iRipplesListener;
    }

    /**
     * �������ٲ���
     *
     * @param maxRippleRadius �������뾶
     */
    private void startSlowRipple(Point center,
                                 float maxRippleRadius)
    {
        //���û������ ����������������ʱ ���ڻ���
        if (ripplesNum >= MAX_RIPPLES_NUM)
        {
            return;
        }

        if (curRipple == null)
        {
            curRipple = new TLRipple(host, rippleListener, center, maxRippleRadius);
        }

        curRipple.startSlowRipple();
    }

    /**
     * �������ٲ���
     */
    private void startFastRipple()
    {
        if (curRipple != null)
        {
            if (ripples == null)
            {
                ripples = new TLRipple[MAX_RIPPLES_NUM];
            }
            //�������ƶ�����¼���б���
            ripples[ripplesNum++] = curRipple;
            curRipple.startFastRipple();
            curRipple = null; //�������Ʊ�ɿ첨�ƺ� �����ƾ͸ñ����� �Ա��ٴε��ʱ���´���
        }
    }


    /**
     * ��ò������뾶(���뾶����ȫ�����������ؼ�)
     *
     * @param bounds     �����ؼ��߽�
     * @param clickPoint ���������
     * @return ���뾶
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
     * �������ڻ������λ����Զ�ı߽��
     *
     * @param bounds     ����
     * @param clickPoint ���λ��
     * @return ��Զ�߽��
     */
    private Point getFarmostPoint(Rect bounds,
                                  Point clickPoint)
    {
        float centerToTop = clickPoint.y - bounds.top;
        float centerToBottom = bounds.bottom - clickPoint.y;
        float centerToLeft = clickPoint.x - bounds.left;
        float centerToRight = bounds.right - clickPoint.x;
        Point point = null;

        //A.����Ƕ��if-else
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

//        //B.��������
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
     * ɾ����������(���������ƶ������� ��Ӷ��ز��Ƽ�����ɾ��)
     *
     * @param ripple ��������
     */
    private void removeRipple(TLRipple ripple)
    {
        final TLRipple[] tmpRipples = ripples;
        final int tmpRipplesNum = ripplesNum;
        final int rippleIndex = getRippleIndex(ripple);
        if (rippleIndex >= 0)
        {
            System.arraycopy(tmpRipples, rippleIndex + 1, tmpRipples, rippleIndex, tmpRipplesNum - (rippleIndex + 1));
            tmpRipples[ripplesNum - 1] = null;
            ripplesNum--;
            host.invalidate();
        }
    }

    /**
     * ��õ�����������(�������ز��Ƽ���)
     *
     * @param ripple ��������
     * @return ������������
     */
    private int getRippleIndex(TLRipple ripple)
    {
        final TLRipple[] tmpRipples = ripples;
        final int tmpRipplesNum = ripplesNum;
        for (int i = 0; i < tmpRipplesNum; i++)
        {
            if (tmpRipples[i] == ripple)
            {
                return i;
            }
        }
        return -1;
    }

    /**
     * ���ز���״̬������
     */
    public interface IRipplesListener{
        /**
         * ���ز��ƽ���
         */
        void onRipplesEnd() ;
    }

    /**
     * ��������״̬������
     */
    public interface IRippleListener
    {
        /**
         * �������ƽ���
         */
        void onRippleEnd(TLRipple ripple);
    }

    private IRippleListener rippleListener = new IRippleListener()
    {
        @Override
        public void onRippleEnd(TLRipple ripple)
        {
            removeRipple(ripple);
            //���е������ƶ���������ζ�Ŷ��ز��ƽ���
            if (ripplesNum == 0 && iRipplesListener != null)
            {
                iRipplesListener.onRipplesEnd();
            }
        }
    };

}
