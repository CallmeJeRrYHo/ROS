package com.bishe.hjh.ros;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bishe.hjh.ros.Thread.updateRestaurant;
import com.bishe.hjh.ros.bean.User;
import com.bishe.hjh.ros.net.OkHttpUtils;
import com.bishe.hjh.ros.util.ImageLoadUtils;
import com.bishe.hjh.ros.util.RestaurantAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link newHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class newHomeFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String MY_ORDER = "我的订单";
    private static final String MY_COL = "我的收藏";
    public static final String RESTAURANT = "restaurant";
    public static final int LOGIN = 10;
    private static final int REFRESH_RESTAURANT = 20;
    private static final int RECIRLE = 30;
    @BindView(R.id.vp_zixun_home)
    ViewPager vp_zixun;
    @BindView(R.id.tv_lvTitle_home)
    TextView tv_title;
    @BindView(R.id.ll_dot_container)
    LinearLayout ll_dot_conainer;
    @BindView(R.id.lv_restaurantList_home)
    ListView lv_restaurantList;
    @BindView(R.id.ptr_restaurant_home)
    PtrClassicFrameLayout ptr;
    @BindView(R.id.civ_userImage_home)
    CircleImageView userImage;
    @BindView(R.id.tv_userName_home)
    TextView tv_userName;
    @BindView(R.id.dl_home)
    DrawerLayout dl_home;
    private updateRestaurant ur;
    private ArrayList<String> items;
    private ArrayAdapter arrayAdapter;
    private List<Restaurant> restaurants = new ArrayList<>();
    private static final String MY_INFO = "我的信息";
    private ViewPagerAdapter adapter;
    private List<View> list;
    private int count = 0;
    private Message pagerRe;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RECIRLE:
                    vp_zixun.setCurrentItem(current);
                    current++;
                    Message m = Message.obtain();
                    m.what = RECIRLE;
                    sendMessageDelayed(m, 2000);
                    break;
                case REFRESH_RESTAURANT:
                    RestaurantAdapter rd = new RestaurantAdapter(ur.getRestaurants(), getActivity());
                    lv_restaurantList.setAdapter(rd);
                    Toast.makeText(getActivity(), "chenggong", Toast.LENGTH_LONG).show();
                    break;
            }

        }
    };
    private String[] titles = {"标题1", "标题2", "标题3", "标题4"};
    private int[] imageIds = {
            R.drawable.zixun1, R.drawable.zixun2, R.drawable.zixun3, R.drawable.zixun4
    };
    private int current;
    private Uri uri;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int index = 0;
    private User u = null;
    private MeFragment f_me;
    private String user;


    public newHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment newHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static newHomeFragment newInstance(String param1, String param2) {
        newHomeFragment fragment = new newHomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        SharedPreferences sh = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        user = sh.getString("user", null);

        View v = inflater.inflate(R.layout.activity_home, container, false);

        dl_home = (DrawerLayout) v.findViewById(R.id.dl_home);
        ptr = (PtrClassicFrameLayout) v.findViewById(R.id.ptr_restaurant_home);
        vp_zixun = (ViewPager) v.findViewById(R.id.vp_zixun_home);
        lv_restaurantList = (ListView) v.findViewById(R.id.lv_restaurantList_home);
        getRestaurant();
        userImage = (CircleImageView) v.findViewById(R.id.civ_userImage_home);
        tv_title = (TextView) v.findViewById(R.id.tv_lvTitle_home);
        tv_userName = (TextView) v.findViewById(R.id.tv_userName_home);
        userImage.setOnClickListener(this);
        ll_dot_conainer = (LinearLayout) v.findViewById(R.id.ll_dot_container);
        if (user != null) {
            try {
                JSONObject j = new JSONObject(user);
                u = new User(j);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ImageLoadUtils.Instance().setImage(getContext(),u.getUserImage(),userImage);
            tv_userName.setText(u.getUserName());
        }
        initViewPager();

        ptr.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getRestaurant();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });


        lv_restaurantList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Restaurant r = restaurants.get(position);
                Intent i = new Intent(getActivity(), MenuList.class);
                i.putExtra(RESTAURANT, r);
                startActivity(i);
            }
        });


        ButterKnife.bind(this, v);
        return v;
    }

    private void initViewPager() {
        current = Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2 % 4);
        Log.d("111111", "initViewPager: " + current);
        list = new ArrayList<View>();
        for (int i = 0; i < imageIds.length; i++) {
            ImageView imageView = new ImageView(getActivity());
            imageView.setImageResource(imageIds[i]);
            list.add(imageView);
            ImageView point = new ImageView(getActivity());
            point.setImageResource(R.drawable.select_point);
            if (i == index) {
                point.setEnabled(true);
                tv_title.setText(titles[i]);
            } else {
                point.setEnabled(false);
            }
            ll_dot_conainer.addView(point);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) point.getLayoutParams();
            params.leftMargin = 50;
            point.setLayoutParams(params);


        }
        adapter = new ViewPagerAdapter(getActivity(), list);
        vp_zixun.setAdapter(adapter);


        vp_zixun.setCurrentItem(current);
        vp_zixun.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                index = position % 4;
                ImageView point = (ImageView) ll_dot_conainer.getChildAt(index);
                point.setEnabled(true);
                tv_title.setText(titles[index]);
                ImageView point_old = (ImageView) ll_dot_conainer.getChildAt(count);
                point_old.setEnabled(false);
                count = index;
                current = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        pagerRe = Message.obtain();
        pagerRe.what = RECIRLE;
        handler.sendMessageDelayed(pagerRe, 2000);

    }


    private void gotoOrder() {
        Toast.makeText(getActivity(), "order", Toast.LENGTH_SHORT).show();
    }


    private void gotoInfo() {
        Toast.makeText(getActivity(), "info", Toast.LENGTH_SHORT).show();

    }

    private void gotoCol() {
        Toast.makeText(getActivity(), "col", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.civ_userImage_home:
                if (u != null) {
                    FragmentManager fm = getFragmentManager();
                    if (f_me == null) {
                        f_me = new MeFragment();
                    }
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user", u);
                    f_me.setArguments(bundle);
                    fm.beginTransaction().replace(R.id.ll_fragment_container, f_me).commit();
                } else {
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(i, LOGIN);
                }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeMessages(RECIRLE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case LOGIN:
                if (resultCode == Activity.RESULT_OK) {
                    User u = (User) data.getSerializableExtra(LoginActivity.USER);
                    ImageLoadUtils.Instance().setImage(getContext(),u.getUserImage(),userImage);
                    tv_userName.setText(u.getUserName());
                }
        }
    }

    void getRestaurant() {
        OkHttpUtils.Instance().get("http://" + getResources().getString(R.string.ip) + ":8080/web/servlet/ReadRestaurant", new OkHttpUtils.ReturnResult() {
            @Override
            public void onSuccess(String s) {
                JSONArray ja = null;
                try {
                    ja = new JSONArray(s);
                    Log.d("111111", "onRefreshBegin: " + ja.length());
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject j = (JSONObject) ja.get(i);
                        Restaurant r = null;
                        r = new Restaurant(j);
                        restaurants.add(r);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RestaurantAdapter rd = new RestaurantAdapter(restaurants, getActivity());
                lv_restaurantList.setAdapter(rd);
                ptr.refreshComplete();
            }

            @Override
            public void onFail(String s) {

            }
        });
    }

}
