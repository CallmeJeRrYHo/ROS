package com.bishe.hjh.ros;

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
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bishe.hjh.ros.bean.Order;
import com.bishe.hjh.ros.bean.User;
import com.bishe.hjh.ros.util.ImageLoadUtils;
import com.bishe.hjh.ros.util.StreamUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link MyOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyOrderFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String RESTAURANT = "restaurant";
    public static final String ORDER ="order" ;
    private static final String TAG ="3333333" ;
    private static final String NET_ERROR = "netError";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private PtrClassicFrameLayout ptr;
    private ListView lv;
    private List<Order> orders=new ArrayList<>();
    private String username=null;
    private OrderAdapter od;

    public MyOrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyOrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyOrderFragment newInstance(String param1, String param2) {
        MyOrderFragment fragment = new MyOrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public void onResume() {
        new OrderAsyncTask().execute(username);
        Log.d(TAG, "onResume: ");
        super.onResume();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView: ");
        View v=inflater.inflate(R.layout.fragment_my_order, container, false);
        ptr= (PtrClassicFrameLayout) v.findViewById(R.id.ptr_order_my_order);
        lv= (ListView) v.findViewById(R.id.lv_my_order);
        SharedPreferences sh=getActivity().getSharedPreferences("userInfo",Context.MODE_PRIVATE);
        try {
            if (sh.getString("user",null)!=null) {
                JSONObject us = new JSONObject(sh.getString("user", null));
                if (us != null) {
                    User u = new User(us);
                    username = u.getUserName();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ptr.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);

            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                new OrderAsyncTask().execute(username);
            }
        });
        new OrderAsyncTask().execute(username);
        return v;
    }
    class OrderAdapter extends BaseAdapter{

        private Context c;
        private List<Order> list=new ArrayList<>();
        public OrderAdapter(Context c,List<Order> l){
            this.c=c;
            this.list=l;
        }
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            View v;
            if (convertView==null) {
                v = LayoutInflater.from(c).inflate(R.layout.my_order_item, parent, false);
                holder=new ViewHolder();
                holder.tv_resName= (TextView) v.findViewById(R.id.tv_resName_item);
                holder.civ_image= (CircleImageView) v.findViewById(R.id.civ_resImage_item);
                holder.ll_res= (LinearLayout) v.findViewById(R.id.ll_res_item);
                holder.ll_order= (LinearLayout) v.findViewById(R.id.ll_order_item);
                holder.tv_order= (TextView) v.findViewById(R.id.tv_order_item);
                holder.tv_isComment= (TextView) v.findViewById(R.id.tv_is_comment_item);
                holder.tv_sum= (TextView) v.findViewById(R.id.tv_sum_item);
                v.setTag(holder);
            }else {
                v=convertView;
                holder= (ViewHolder) v.getTag();
            }

            holder.tv_resName.setText(list.get(position).getRestaurant().getName());
            if (list.get(position).isComment()){
                holder.tv_isComment.setVisibility(View.VISIBLE);
            }else {
                holder.tv_isComment.setVisibility(View.INVISIBLE);
                holder.ll_order.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i=new Intent(getActivity(),CommentActivity.class);
                        i.putExtra(ORDER,list.get(position));
                        startActivity(i);
                    }
                });
            }
            ImageLoadUtils.Instance().setImage(getContext(),list.get(position).getRestaurant().getImageFile(),holder.civ_image);
            holder.ll_res.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), MenuList.class);
                    i.putExtra(RESTAURANT, list.get(position).getRestaurant());
                    startActivity(i);
                }
            });

            holder.tv_order.setText(list.get(position).getOrderFoods().get(0).getFoodId().getName()+"等"+list.get(position).getOrderFoods().size()+"件");
            holder.tv_sum.setText("合计：￥"+list.get(position).getSum());
            return v;
        }



        class ViewHolder{
            TextView tv_resName;
            TextView tv_isComment;
            CircleImageView civ_image;
            TextView tv_order;
            TextView tv_sum;
            LinearLayout ll_res;
            LinearLayout ll_order;
        }
    }


    class OrderAsyncTask extends AsyncTask<String,String ,String >{


        @Override
        protected String doInBackground(String... params) {
            String s=null;
            if (params[0]==null)
                return s;
            try {
                URL url=new URL("http://"+getResources().getString(R.string.ip)+":8080/web/servlet/ReadMyOrder?username="+params[0]);
                HttpURLConnection con= (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setReadTimeout(5000);
                con.setConnectTimeout(5000);
                int code=con.getResponseCode();
                if (code==200){
                    s= StreamUtils.parserStream(con.getInputStream());
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                s=NET_ERROR;
            }finally {
                return s;

            }
        }

        @Override
        protected void onPostExecute(String s) {
            try {
               Log.d("333333", "onPostExecute: "+s);
                orders=new ArrayList<>();
                if(s!=null) {

                    if (s.equals(NET_ERROR))
                        Toast.makeText(getActivity(),"您的网络不给力请稍后再试",Toast.LENGTH_SHORT).show();
                    else {
                        JSONArray ja = new JSONArray(s);
                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject j = ja.getJSONObject(i);
                            Order o = new Order(j);
                            Log.d("333333", "onPostExecute: " + o.getRestaurant().getName());

                            orders.add(o);
                        }
                        od = new OrderAdapter(getActivity(), orders);
                        lv.setAdapter(od);
                    }
                }
                ptr.refreshComplete();
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("333333", "onPostExecute: "+e);

            }
        }
    }

}
