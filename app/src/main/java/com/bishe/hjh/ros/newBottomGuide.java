package com.bishe.hjh.ros;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class newBottomGuide extends Fragment {

    private RadioButton home;
    private RadioButton order;
    private RadioButton me;
    private RadioGroup group;
    private MyOrderFragment f_order;
    private MeFragment f_me;
    private newHomeFragment f_home;
    public newBottomGuide() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.content_new_home, container, false);
        group= (RadioGroup) v.findViewById(R.id.group_home);
        home= (RadioButton) v.findViewById(R.id.rb_home_home);
        order= (RadioButton) v.findViewById(R.id.rb_order_home);
        me= (RadioButton) v.findViewById(R.id.rb_me_home);
        f_home=new newHomeFragment();
        getFragmentManager().beginTransaction().add(R.id.ll_fragment_container,f_home).commit();
        initBottom();
        return v;
    }
    private void initBottom() {

        final FragmentManager fm=getFragmentManager();
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_home_home:
                        if (f_home==null)
                            f_home=new newHomeFragment();
                        fm.beginTransaction().replace(R.id.ll_fragment_container,f_home).commit();
                        break;
                    case R.id.rb_order_home:
                        if (f_order==null) {
                            f_order = new MyOrderFragment();
                        }
                        fm.beginTransaction().replace(R.id.ll_fragment_container,f_order).commit();
                        break;
                    case R.id.rb_me_home:
                        if (f_me==null) {
                            f_me = new MeFragment();
                        }
                        fm.beginTransaction().replace(R.id.ll_fragment_container,f_me).commit();
                        break;
                }
            }
        });
    }
}
