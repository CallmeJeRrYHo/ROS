package com.bishe.hjh.ros;

import android.support.v4.app.Fragment;

public class RegisterActivity extends SingleFragmentActivity {


    @Override
    protected Fragment CreateFragment() {
        return new RegisterFragment();
    }
}