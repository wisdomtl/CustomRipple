package com.example.tangliang.myapplication;

import android.graphics.Bitmap;

/**
 * Created by Tangliang on 2015/5/15.
 */
public class ListItemInfo {

    String title;

    String category;

    String picPath;

    Bitmap pic;

    public ListItemInfo( String title , String category , String picPath )
    {
        this.title = title;
        this.category = category;
        this.picPath = picPath;
    }

    public String getPicPath()
    {
        return picPath;
    }

    public void setPicPath( String picPath )
    {
        this.picPath = picPath;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle( String title )
    {
        this.title = title;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory( String category )
    {
        this.category = category;
    }

    public Bitmap getPic()
    {
        return pic;
    }

    public void setPic( Bitmap pic )
    {
        this.pic = pic;
    }
}
