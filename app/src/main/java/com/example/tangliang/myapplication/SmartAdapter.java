package com.example.tangliang.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class SmartAdapter extends BaseAdapter
{

    private LayoutInflater inflater;
    private ArrayList<ListItemInfo> itemInfos;

    public SmartAdapter(Context context,
                        ArrayList<ListItemInfo> itemInfos)
    {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.itemInfos = itemInfos;
    }

    @Override
    public int getCount()
    {
        return itemInfos.size();
    }

    @Override
    public Object getItem(int position)
    {
        return itemInfos.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position,
                        View convertView,
                        ViewGroup parent)
    {
        ChildViewManager childViewManager = null;
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.smartlist_item, null);
            childViewManager = new ChildViewManager(convertView);
            convertView.setTag(childViewManager);
        }
        else
        {
            childViewManager = (ChildViewManager) convertView.getTag();
        }
        ListItemInfo itemInfo = itemInfos.get(position);
        childViewManager.getTvCategory().setText(itemInfo.getCategory());
        childViewManager.getTvTitle().setText(itemInfo.getTitle());
        return convertView;
    }


    private class ChildViewManager
    {
        private View vFather;
        private TextView tvTitle;
        private TextView tvCategory;
        private ImageView ivPic;

        public ChildViewManager(View vFather)
        {
            this.vFather = vFather;
        }

        public TextView getTvTitle()
        {
            if (tvTitle == null)
            {
                tvTitle = (TextView) vFather.findViewById(R.id.tvTitle_SmartList);
            }
            return tvTitle;
        }

        public TextView getTvCategory()
        {
            if (tvCategory == null)
            {
                tvCategory = (TextView) vFather.findViewById(R.id.tvCategory_SmartList);
            }
            return tvCategory;
        }

        public ImageView getIvPic()
        {
            if (ivPic == null)
            {
                ivPic = (ImageView) vFather.findViewById(R.id.iv_SmartList);
            }
            return ivPic;
        }
    }

}
