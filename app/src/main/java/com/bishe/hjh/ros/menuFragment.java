package com.bishe.hjh.ros;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.snappingstepper.SnappingStepper;
import com.bigkoo.snappingstepper.listener.SnappingStepperValueChangeListener;
import com.bishe.hjh.ros.bean.Order;
import com.bishe.hjh.ros.bean.OrderFood;
import com.bishe.hjh.ros.bean.User;
import com.bishe.hjh.ros.bean.caishi;
import com.bishe.hjh.ros.util.StreamUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link menuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class menuFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    public static final String CAISHI = "caishi";
    private static final String TAG = "menulist";
    public static final int COMMITORDER = 1;
    private static final int CAISHIDETAIL = 1005;
    public static final String ORDER ="order" ;
    private static final String RESTARUANT = "res";
    // TODO: Rename and change types of parameters
    private Restaurant mParam1;
    private ListView lv_caishi;
    private List<caishi> list=new ArrayList<caishi>();
    private PtrFrameLayout ptr;
    private Order order=null;
    private User user=null;
    private String save;
    private ImageButton ib_shoppingcar;

    private SharedPreferences sh;
    private caishiAdapter r=null;

    public menuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment menuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static menuFragment newInstance(Restaurant param1) {
        menuFragment fragment = new menuFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = (Restaurant) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        r=new caishiAdapter(getActivity(),list);
        lv_caishi.setAdapter(r);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_menu, container, false);
        lv_caishi= (ListView) v.findViewById(R.id.lv_caishiList_menuList);
        sh=getActivity().getSharedPreferences("userInfo",Context.MODE_PRIVATE);
        Log.d("crev111111", "onCreateView: "+sh.getString(mParam1.getName(),null));
        Log.d("crev111111", "onCreateView: "+(order==null));
        try {
            String u=sh.getString("user",null);
            if (u!=null) {
                JSONObject j = new JSONObject(u);
                user = new User(j);
            }
            String s=sh.getString(mParam1.getName(),null);
            if (s!=null){
                JSONObject j=new JSONObject(s);
                order=new Order(j);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ib_shoppingcar= (ImageButton) v.findViewById(R.id.ib_shoppingcar_menu);
        ib_shoppingcar.setOnClickListener(this);
        new myAsyncTask().execute(String.valueOf(mParam1.getId()));
        ptr= (PtrFrameLayout) v.findViewById(R.id.ptr_caishi_menu);
        ptr.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                new myAsyncTask().execute(String.valueOf(mParam1.getId()));


            }
        });
        lv_caishi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                caishi c=list.get(position);
                Intent i=new Intent(getActivity(),caishiDetailActivity.class);
                i.putExtra(CAISHI,c);
                i.putExtra(RESTARUANT,mParam1);
                i.putExtra(ORDER,order);
                startActivityForResult(i,CAISHIDETAIL);
            }
        });
        return v;
    }

    @Override
    public void onClick(View v) {
        if (order!=null) {
            Intent i = new Intent(getActivity(), CommitOrderActivity.class);
            i.putExtra("order", order);
            startActivityForResult(i,COMMITORDER);
        }else {
            Toast.makeText(getActivity(),"请先选择菜品",Toast.LENGTH_SHORT).show();
        }
    }

    class myAsyncTask extends AsyncTask<String,Void,List<caishi>>{

        @Override
        protected List<caishi> doInBackground(String... params) {

            try {
                list = new ArrayList<caishi>();
                URL u = new URL("http://" + getResources().getString(R.string.ip) + ":8080/web/servlet/ReadCaishi?restaurantId=" + params[0]);
                HttpURLConnection con = (HttpURLConnection) u.openConnection();
                con.setReadTimeout(5000);
                con.setConnectTimeout(5000);
                con.setRequestMethod("GET");
                int code = con.getResponseCode();
                if (code == 200) {
                    String result = StreamUtils.parserStream(con.getInputStream());
                    JSONArray ja = new JSONArray(result);
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject j = (JSONObject) ja.get(i);
                        caishi r = new caishi(j);

                        list.add(r);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<caishi> list) {
            r=new caishiAdapter(getActivity(),list);
            lv_caishi.setAdapter(r);
            ptr.refreshComplete();
        }
    }
    class caishiAdapter extends BaseAdapter {
        private Context context;
        private List<caishi> list;
        public caishiAdapter(Context c, List<caishi> r){
            this.context=c;
            this.list=r;
        }
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LinearLayout ll=null;
            ViewHolder v;
            caishi c=list.get(position);
            if (convertView!=null){
                ll= (LinearLayout) convertView;
                v= (ViewHolder) ll.getTag();
            }else {
                ll= (LinearLayout) LayoutInflater.from(context).inflate(R.layout.menu_list_item,null);
                v = new ViewHolder();
                v.image= (ImageView) ll.findViewById(R.id.iv_caishi_item);
                v.name= (TextView) ll.findViewById(R.id.tv_caiming_item);
                v.price= (TextView) ll.findViewById(R.id.tv_price_item);
                v.sell= (TextView) ll.findViewById(R.id.tv_sell_item);
                v.star= (RatingBar) ll.findViewById(R.id.rb_star_item);
                v.stp_add= (SnappingStepper) ll.findViewById(R.id.stp_add_item);
                ImageLoaderConfiguration d=ImageLoaderConfiguration.createDefault(context);
                ImageLoader.getInstance().init(d);
                v.loader=ImageLoader.getInstance();
                v.o=new DisplayImageOptions.Builder()
                        .showImageOnLoading(R.drawable.loading)
                        .showImageOnFail(R.drawable.loading_error)
                        .cacheInMemory(true)
                        .imageScaleType(ImageScaleType.NONE).build();
                ll.setTag(v);
            }

            v.loader.displayImage(c.getImageFile(),v.image,v.o);
            v.name.setText(c.getName());
            v.price.setText("￥"+c.getPrice());
            v.sell.setText("已售："+c.getSell());
            v.star.setRating(c.getPraise());
            if (order!=null) {
                for (OrderFood of : order.getOrderFoods()) {
                    if (of.getFoodId().getId() == list.get(position).getId()) {
                        v.stp_add.setValue(of.getNum());
                    }
                }
            }

            //增减菜式订单修改
            v.stp_add.setOnValueChangeListener(new SnappingStepperValueChangeListener() {
                @Override
                public void onValueChange(View view, int value) {

                    //判断是否登录
                    if (user!=null){

                        //判断订单是否存在
                        if (order==null) {
                            order = new Order();
                            Log.d("111111", "onValueChange: is null");
                            order.setRestaurant(mParam1);
                            order.setUser(user);
                            order.setHereTake(0);
                            order.setSum(0.0);
                            order.setOrderId(111);
                            OrderFood of = new OrderFood(list.get(position), order.getOrderId(), value);
                            List<OrderFood> os = order.getOrderFoods();
                            os.add(of);
                            Log.d(TAG, "onClick: " + os.size() + "       " + order.getSum());

                            try {
                                order.toJson();
                                Log.d(TAG, "onClick: " + order.toJson());
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d(TAG, "onClick: " + e);
                            }
                        }else {

                            //判断数量是否为0
                            if (value == 0) {
                                Iterator<OrderFood> it=order.getOrderFoods().iterator();
                                while (it.hasNext()){
                                    if (it.next().getFoodId().getId()==list.get(position).getId()){
                                        it.remove();
                                    }
                                }
                            } else {
                                boolean isExist = false;
                                for (OrderFood of : order.getOrderFoods()) {
                                    if (of.getFoodId().getId() == list.get(position).getId()) {
                                        of.setNum(value);
                                        Log.d(TAG, "onValueChange: "+order.getSum());
                                        isExist = true;
                                        break;
                                    }
                                }
                                if (!isExist) {
                                    OrderFood of = new OrderFood(list.get(position), order.getOrderId(), value);
                                    List<OrderFood> os = order.getOrderFoods();
                                    os.add(of);
                                }
                            }
                        }

                        //判断订单 里面有无菜品
                        if (order.getOrderFoods().size()==0){
                            order=null;
                        }else {
                            order.setSum(0.0);
                            for (OrderFood of : order.getOrderFoods()) {
                                order.setSum(order.getSum() + of.getNum() * of.getFoodId().getPrice());
                            }
                        }
                    }else {
                        Toast.makeText(getActivity(),"请先登录！",Toast.LENGTH_SHORT).show();
                    }

                }
            });

            return ll;
        }

    }
    static class ViewHolder{
        TextView name;
        ImageView image;
        TextView sell;
        RatingBar star;
        TextView price;
        SnappingStepper stp_add;
        ImageLoader loader;
        DisplayImageOptions o;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("111111", "onPause: pppppppppppp");
        if (user!=null&&order!=null) {
            try {
                String us=sh.getString("user",null);
                JSONObject j=new JSONObject(us);
                user=new User(j);
                order.setUser(user);
                sh.edit().putString(mParam1.getName(), order.toJson().toString()).commit();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case COMMITORDER:

            if (resultCode == Activity.RESULT_OK)
                order = null;
            r.notifyDataSetChanged();
                break;
            case CAISHIDETAIL:
                if (resultCode==Activity.RESULT_OK)
                    order= (Order) data.getSerializableExtra("order");

                r.notifyDataSetChanged();
                break;
        }
    }
}
