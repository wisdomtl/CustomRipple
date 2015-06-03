package com.example.tangliang.myapplication;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;


public class MainActivity extends Activity
{

    private static boolean themeSwitch = false;
    private Button btnChangeRippleColor;
    private Button btnRipple;
    private ListView lvRipple;
    private CustomRippleButton crb;
    /** 列表中当前显示表项数据 */
    private ArrayList<ListItemInfo> curListItemsInfo;
    /** 列表中所有表项数据 */
    private ArrayList<ListItemInfo> allListItemsInfo;
    /** 全部表项数 */
    private static final int allItemsNum = 52;
    /** 单次加载更多表项数 */
    private static final int followUpItemsNum = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ThemeManager.getInstance().onThemeChanged(this);
        setContentView(R.layout.activity_main);

        loadData();
        btnChangeRippleColor = (Button) findViewById(R.id.btn_changeRippleColor);
        btnRipple = (Button) findViewById(R.id.btnRipple);
//        lvRipple = (ListView) findViewById(R.id.lvRipple) ;
//        SmartAdapter adapter = new SmartAdapter(this , curListItemsInfo);
//        lvRipple.setAdapter(adapter);
        crb = (CustomRippleButton) findViewById(R.id.crb);
        boolean isFastView = crb.isHardwareAccelerated();
        System.out.println("isFastView = " + isFastView);
        Log.v("ttangliang", "isFastView = " + isFastView);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
    }

    /**
     * 加载控件数据并组织成ListItemInfo
     *
     * @author TangLiang 2015年4月27日
     */
    private final void loadData()
    {
        allListItemsInfo = new ArrayList<ListItemInfo>();
        curListItemsInfo = new ArrayList<ListItemInfo>();
        String[] titles = new String[allItemsNum];
        String[] categorys = new String[allItemsNum];
        String[] picPaths = new String[allItemsNum];
        // 1.加载数据
        for (int i = 0; i < allItemsNum; i++)
        {
            titles[i] = "标题" + i;
            categorys[i] = ((i % 2 == 0) ? "风景" : "人像");
            picPaths[i] = "c:\\telanx\\photos\\photo" + i;
            // 2.用ListItemInfo组织初始数据
            ListItemInfo info = new ListItemInfo(titles[i], categorys[i], picPaths[i]);
            allListItemsInfo.add(info);
            if (i < followUpItemsNum)
            {
                curListItemsInfo.add(info);
            }
        }
    }

    public void changeRippleColorByJava(View v)
    {
        RippleDrawable background = (RippleDrawable) btnRipple.getBackground();
        background.setColor(ColorStateList.valueOf(Color.GREEN));
    }

    public void changeRippleColorByTheme(View v)
    {
        ThemeManager.getInstance().setTheme(this, R.style.greenTheme);
    }


}
