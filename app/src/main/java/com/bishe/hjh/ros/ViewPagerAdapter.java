package com.bishe.hjh.ros;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by HJH on 2016/3/4.
 */
public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private List<View> list;
    public ViewPagerAdapter(Context c,List<View> list){
        this.context=c;
        this.list=list;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(list.get(position%4));
        return list.get(position%4);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(list.get(position%4));
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view==object);
    }
}
