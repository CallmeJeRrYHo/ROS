package com.bishe.hjh.ros.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bishe.hjh.ros.R;
import com.bishe.hjh.ros.bean.OrderFood;

import java.util.List;

/**
 * Created by HJH on 2016/4/25.
 */
public class OrderListAdapter extends BaseAdapter {

    Context c;
    List<OrderFood> list;
    public OrderListAdapter(Context c,List<OrderFood> l){
        this.c=c;
        this.list=l;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout ll=null;
        ViewHolder v;
        OrderFood o=list.get(position);
        if (convertView!=null){
            ll= (LinearLayout) convertView;
            v= (ViewHolder) ll.getTag();
        }else {
            ll= (LinearLayout) LayoutInflater.from(c).inflate(R.layout.order_list_item,null);
            v = new ViewHolder();
            v.name= (TextView) ll.findViewById(R.id.tv_order_list_name_item);
            v.num= (TextView) ll.findViewById(R.id.tv_order_num_item);
            v.price= (TextView) ll.findViewById(R.id.tv_order_price_item);
            ll.setTag(v);
        }
        v.name.setText(o.getFoodId().getName());
        v.num.setText("x"+o.getNum());
        v.price.setText(o.getFoodId().getPrice()+"￥");
        return ll;
    }
    static class ViewHolder{
        TextView name;
        TextView num;
        TextView price;
    }
}
