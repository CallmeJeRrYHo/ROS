package com.bishe.hjh.ros.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bishe.hjh.ros.R;
import com.bishe.hjh.ros.Restaurant;

import java.util.List;

/**
 * Created by HJH on 2016/4/10.
 */
public class RestaurantAdapter extends BaseAdapter {

    List<Restaurant> restaurants;
    Context context;

    public RestaurantAdapter(List<Restaurant> r, Context c) {
        this.restaurants = r;
        this.context = c;
    }

    @Override
    public int getCount() {
        int i = restaurants.size();
        return i;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout ll = null;
        ViewHolder v;
        if (convertView != null) {
            ll = (LinearLayout) convertView;
            v = (ViewHolder) ll.getTag();
        } else {
            ll = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.restaurant_list_item, null);
            v = new ViewHolder();
            v.resName = (TextView) ll.findViewById(R.id.tv_restaurant_name);
            v.restaurantPhoto = (ImageView) ll.findViewById(R.id.iv_restaurant_photo);
            v.sell = (TextView) ll.findViewById(R.id.tv_sellNum_item);
            v.star = (RatingBar) ll.findViewById(R.id.rb_restaurant_star);
            ll.setTag(v);
        }
        Restaurant r = restaurants.get(position);
        ImageLoadUtils.Instance().setImage(context,r.getImageFile(),v.restaurantPhoto);
        v.sell.setText("已售：" + r.getSell());
        v.resName.setText(r.getName());
        v.star.setRating(r.getRatingStar());
        return ll;
    }

    static class ViewHolder {
        ImageView restaurantPhoto;
        TextView resName;
        RatingBar star;
        TextView sell;
    }
}
