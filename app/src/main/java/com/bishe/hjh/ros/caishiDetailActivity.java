package com.bishe.hjh.ros;

import android.support.v4.app.Fragment;

import com.bishe.hjh.ros.bean.Order;
import com.bishe.hjh.ros.bean.caishi;

public class caishiDetailActivity extends SingleFragmentActivity {

    @Override
    protected Fragment CreateFragment() {
        caishi c= (caishi) getIntent().getSerializableExtra(menuFragment.CAISHI);
        Order o= (Order) getIntent().getSerializableExtra(menuFragment.ORDER);
        Restaurant r= (Restaurant) getIntent().getSerializableExtra("res");
        return caishiDetailActivityFragment.newInstance(c,o,r);
    }

}
