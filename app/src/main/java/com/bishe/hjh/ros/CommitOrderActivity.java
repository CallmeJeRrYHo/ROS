package com.bishe.hjh.ros;

import android.support.v4.app.Fragment;

import com.bishe.hjh.ros.bean.Order;

public class CommitOrderActivity extends SingleFragmentActivity {


    @Override
    protected Fragment CreateFragment() {
        Order o= (Order) getIntent().getSerializableExtra("order");
        return CommitOrderActivityFragment.newInstance(o);
    }
}
