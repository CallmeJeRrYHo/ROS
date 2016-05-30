package com.bishe.hjh.ros;

import android.support.v4.app.Fragment;

public class MenuList extends SingleFragmentActivity {

    @Override
    protected Fragment CreateFragment() {
        Restaurant r= (Restaurant) getIntent().getSerializableExtra(Home.RESTAURANT);
        Fragment fragment=MenuListFragment.newInstance(r);
        return fragment;
    }


}
