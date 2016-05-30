package com.bishe.hjh.ros;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.bishe.hjh.ros.bean.Order;

public class CommentActivity extends SingleFragmentActivity {


    @Override
    protected Fragment CreateFragment() {
        Intent i=getIntent();
        if (i!=null){
            Order order= (Order) i.getSerializableExtra(MyOrderFragment.ORDER);
            return CommentFragment.newInstance(order);
        }
        return new CommentFragment();
    }
}
