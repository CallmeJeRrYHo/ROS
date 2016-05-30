package com.bishe.hjh.ros.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bishe.hjh.ros.R;
import com.bishe.hjh.ros.bean.caishi;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.List;

/**
 * Created by HJH on 2016/4/12.
 */
public class caishiAdapter extends BaseAdapter {
    private Context context;
    private List<caishi> list;
    public caishiAdapter(Context c, List<caishi> r){
        this.context=c;
        this.list=r;
    }
    @Override
    public int getCount() {
        return list.size();
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
        LinearLayout ll=null;
        ViewHolder v;
         caishi c=list.get(position);
        if (convertView!=null){
            ll= (LinearLayout) convertView;
            v= (ViewHolder) ll.getTag();
        }else {
            ll= (LinearLayout) LayoutInflater.from(context).inflate(R.layout.menu_list_item,null);
            v = new ViewHolder();
            v.image= (ImageView) ll.findViewById(R.id.iv_caishi_item);
            v.name= (TextView) ll.findViewById(R.id.tv_caiming_item);
            v.price= (TextView) ll.findViewById(R.id.tv_price_item);
            v.sell= (TextView) ll.findViewById(R.id.tv_sell_item);
            v.star= (RatingBar) ll.findViewById(R.id.rb_star_item);
            ImageLoaderConfiguration d=ImageLoaderConfiguration.createDefault(context);
            ImageLoader.getInstance().init(d);
            v.loader=ImageLoader.getInstance();
            v.o=new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.loading)
                    .showImageOnFail(R.drawable.loading_error)
                    .cacheInMemory(true)
                    .imageScaleType(ImageScaleType.NONE).build();
            ll.setTag(v);
        }

        v.loader.displayImage(c.getImageFile(),v.image,v.o);
        v.name.setText(c.getName());
        v.price.setText("￥"+c.getPrice());
        v.sell.setText("已售："+c.getSell());
        v.star.setRating(c.getPraise());
        v.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"add",Toast.LENGTH_LONG).show();
            }
        });
        return ll;
    }
    static class ViewHolder{
        TextView name;
        ImageView image;
        TextView sell;
        RatingBar star;
        TextView price;
        ImageButton add;
        ImageLoader loader;
        DisplayImageOptions o;
    }
}
