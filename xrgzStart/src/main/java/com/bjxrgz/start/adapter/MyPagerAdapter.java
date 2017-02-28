package com.bjxrgz.start.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JiangZhiGuo on 2016-11-9.
 * describe 通用ViewPager适配器
 */
public class MyPagerAdapter<T> extends PagerAdapter {

    private View.OnClickListener clickListener;
    private View.OnLongClickListener longClickListener;

    private Context context;
    private List<T> data;
    private int itemLayoutId;

    public MyPagerAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
    }

    public void setData(int itemLayoutId, List<T> data) {
        this.itemLayoutId = itemLayoutId;
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        clickListener = listener;
    }

    public void setOnLongClickListener(View.OnLongClickListener listener) {
        longClickListener = listener;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView view = (ImageView) View.inflate(context, itemLayoutId, null);
        T item = data.get(position);

        if (item instanceof String) {
            Glide.with(context).load((String) item)
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(view);
        } else if (item instanceof Bitmap) {
            Glide.with(context).load((Bitmap) item)
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(view);
        } else if (item instanceof File) {
            Glide.with(context).load((File) item)
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(view);
        } else if (item instanceof Integer) {
            Glide.with(context).load((Integer) item)
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(view);
        }
        if (clickListener != null) {
            clickListener.onClick(view);
        }
        if (longClickListener != null) {
            longClickListener.onLongClick(view);
        }
        container.addView(view);
        return view;
    }
}
