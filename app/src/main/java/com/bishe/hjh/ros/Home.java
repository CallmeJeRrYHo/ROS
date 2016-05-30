package com.bishe.hjh.ros;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bishe.hjh.ros.Thread.updateRestaurant;
import com.bishe.hjh.ros.bean.User;
import com.bishe.hjh.ros.util.RestaurantAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class Home extends AppCompatActivity implements View.OnClickListener {

    private static final String MY_ORDER = "我的订单";
    private static final String MY_COL = "我的收藏";
    public static final String RESTAURANT = "restaurant";
    public static final int LOGIN = 10;
    private static final int REFRESH_RESTAURANT = 20;
    private static final int RECIRLE = 30;
    private ArrayList<String> items;
    private ArrayAdapter arrayAdapter;
    private ListView listView;
    private DrawerLayout dl_home;
    private static final String MY_INFO = "我的信息";
    private ViewPager vp_zixun;
    private ViewPagerAdapter adapter;
    private List<View> list;
    private LinearLayout ll_dot_conainer;
    private ListView lv_restaurantList;
    private int count = 0;
    private CircleImageView userImage;
    private PtrClassicFrameLayout ptr;
    private TextView tv_title;
    private TextView tv_useName;
    private updateRestaurant ur;
    private Message pagerRe = Message.obtain();
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
                    RestaurantAdapter rd = new RestaurantAdapter(ur.getRestaurants(), getApplication());
                    lv_restaurantList.setAdapter(rd);
                    Toast.makeText(getApplicationContext(), "chenggong", Toast.LENGTH_LONG).show();
                    break;
            }

        }
    };
    private String[] titles = {"标题1", "标题2", "标题3", "标题4"};
    private int[] imageIds = {
            R.drawable.zixun1, R.drawable.zixun2, R.drawable.zixun3, R.drawable.zixun4
    };
    private int current = Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2 % 4);
    private Uri uri;
    private ImageLoader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        pagerRe.what = RECIRLE;
        dl_home = (DrawerLayout) findViewById(R.id.dl_home);
        ur=new updateRestaurant();
        ur.start();
        ptr = (PtrClassicFrameLayout) findViewById(R.id.ptr_restaurant_home);
        vp_zixun = (ViewPager) findViewById(R.id.vp_zixun_home);
        lv_restaurantList = (ListView) findViewById(R.id.lv_restaurantList_home);
        RestaurantAdapter rd = new RestaurantAdapter(ur.getRestaurants(), getApplication());
        lv_restaurantList.setAdapter(rd);
        userImage = (CircleImageView) findViewById(R.id.civ_userImage_home);
        tv_title = (TextView) findViewById(R.id.tv_lvTitle_home);
        tv_useName = (TextView) findViewById(R.id.tv_userName_home);
        userImage.setOnClickListener(this);
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(configuration);
        loader = ImageLoader.getInstance();

        ptr.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                Message m = Message.obtain();
                ur=new updateRestaurant();
                ur.start();
                m.what = REFRESH_RESTAURANT;
                handler.sendMessage(m);
                ptr.refreshComplete();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
        /*
        * 模拟接受到数据
        * */

        ll_dot_conainer = (LinearLayout) findViewById(R.id.ll_dot_container);
        lv_restaurantList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Restaurant r = ur.getRestaurants().get(position);
                Intent i = new Intent(Home.this, MenuList.class);
                i.putExtra(RESTAURANT, r);
                startActivity(i);
            }
        });
        initViewPager();

        handler.sendMessageDelayed(pagerRe, 2000);

    }


    private void initViewPager() {
        list = new ArrayList<View>();
        for (int i = 0; i < imageIds.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(imageIds[i]);
            list.add(imageView);
            ImageView point = new ImageView(this);
            point.setImageResource(R.drawable.select_point);
            if (i == 0) {
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
        adapter = new ViewPagerAdapter(this, list);
        vp_zixun.setAdapter(adapter);
        vp_zixun.setCurrentItem(current);
        vp_zixun.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int index = position % 4;
                ImageView point = (ImageView) ll_dot_conainer.getChildAt(index);
                point.setEnabled(true);
                tv_title.setText(titles[index]);
                ImageView point_old = (ImageView) ll_dot_conainer.getChildAt(count % 4);
                point_old.setEnabled(false);
                count = index;
                current = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private void gotoOrder() {
        Toast.makeText(this, "order", Toast.LENGTH_SHORT).show();
    }


    private void gotoInfo() {
        Toast.makeText(this, "info", Toast.LENGTH_SHORT).show();

    }

    private void gotoCol() {
        Toast.makeText(this, "col", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(this, LoginActivity.class);
        startActivityForResult(i, LOGIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case LOGIN:
                if (resultCode == RESULT_OK) {
                    User u = (User) data.getSerializableExtra(LoginActivity.USER);
                    uri = Uri.parse(u.getUserImage());
                    // Bitmap b= BitmapFactory.decodeFile(u.getUserImage());
                    Log.d("111111", "onActivityResult: " + uri + "   ");
                    loader.displayImage(u.getUserImage(), userImage);
                    // userImage.setImageURI(uri);
                    tv_useName.setText(u.getUserName());
                    Log.d("111111", "onActivityResult: " + u.getUserName());
                }
        }
    }
}
