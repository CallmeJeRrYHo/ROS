package com.bishe.hjh.ros;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * A placeholder fragment containing a simple view.
 */
public class MenuListFragment extends Fragment implements View.OnClickListener {

    private static final java.lang.String RESID = "resid";
    private TextView tv_resName;
    private TextView tv_resDes;
    private TextView tv_sell;
    private RatingBar rt_resStar;
    private TextView tv_address;
    private ImageButton btn_phone;
    private ImageView iv_photo;
    private Button btn_order;
    private Button btn_appraise;
    private LinearLayout linearLayout;
    private Restaurant r;
    private menuFragment f_menu;
    private appraiseFragment f_appraise;

    public MenuListFragment() {
    }

    public static MenuListFragment newInstance(Restaurant r) {

        Bundle args = new Bundle();
        args.putSerializable(RESID,r);
        MenuListFragment fragment = new MenuListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            r= (Restaurant) getArguments().get(RESID);
        }
        f_menu=menuFragment.newInstance(r);
        FragmentManager fragmentManager=getFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fl_order_appraise_menu,f_menu).commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_menu_list, container, false);


        tv_resName= (TextView) v.findViewById(R.id.tv_resName_menu);
        tv_resDes= (TextView) v.findViewById(R.id.tv_resDes_menu);
        tv_sell= (TextView) v.findViewById(R.id.tv_sell_menu);
        tv_address= (TextView) v.findViewById(R.id.tv_address_menu);
        rt_resStar= (RatingBar) v.findViewById(R.id.rt_ratingBar_menu);
        btn_appraise= (Button) v.findViewById(R.id.btn_appraise_menu);
        iv_photo= (ImageView) v.findViewById(R.id.iv_resPhoto_menu);
        btn_appraise.setOnClickListener(this);
        btn_order= (Button) v.findViewById(R.id.btn_order_menu);
        btn_order.setOnClickListener(this);
        btn_phone= (ImageButton) v.findViewById(R.id.ib_phone_menu);
        btn_phone.setOnClickListener(this);
       // linearLayout= (LinearLayout) v.findViewById(R.id.fl_order_appraise_menu);
        tv_resName.setText(r.getName());
        tv_resDes.setText(r.getResDes());
        tv_sell.setText("已售："+r.getSell());
        tv_address.setText(r.getResAddress());
        ImageLoaderConfiguration d=ImageLoaderConfiguration.createDefault(getActivity());
        ImageLoader.getInstance().init(d);
        ImageLoader loader=ImageLoader.getInstance();
        DisplayImageOptions o=new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.loading)
                .showImageOnFail(R.drawable.loading_error)
                .cacheInMemory(true)
                .imageScaleType(ImageScaleType.NONE).build();
        loader.displayImage(r.getImageFile(),iv_photo);
        return v;
    }

    @Override
    public void onClick(View v) {
        FragmentManager fm=getFragmentManager();

        switch (v.getId()){
            case R.id.ib_phone_menu:
                Toast.makeText(getActivity(),"拨打餐厅电话",Toast.LENGTH_LONG).show();
                Uri uri = Uri.parse("tel:"+r.getResPhone());
                Intent it = new Intent(Intent.ACTION_DIAL, uri);
                startActivity(it);
                break;
            case R.id.btn_order_menu:
                if (f_menu==null)
                    f_menu=menuFragment.newInstance(r);
                fm.beginTransaction().replace(R.id.fl_order_appraise_menu,f_menu).commit();
                break;
            case R.id.btn_appraise_menu:
                if (f_appraise==null)
                    f_appraise=appraiseFragment.newInstance(r.getId());
                fm.beginTransaction().replace(R.id.fl_order_appraise_menu,f_appraise).commit();

        }
    }
}
