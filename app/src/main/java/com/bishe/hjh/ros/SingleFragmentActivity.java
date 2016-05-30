package com.bishe.hjh.ros;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by HJH on 2016/1/17.
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {
    protected abstract Fragment CreateFragment();

    protected int getLayoutResId(){
        return R.layout.activity_menu_list;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        FragmentManager fm=getSupportFragmentManager();
        Fragment fragment=fm.findFragmentById(R.id.fragmentContainer);
        if(fragment==null){
            fragment= CreateFragment();
            fm.beginTransaction().add(R.id.fragmentContainer,fragment).commit();
        }
    }
}
