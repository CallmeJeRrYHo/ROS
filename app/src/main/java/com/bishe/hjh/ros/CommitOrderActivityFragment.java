package com.bishe.hjh.ros;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bishe.hjh.ros.bean.Order;
import com.bishe.hjh.ros.bean.OrderFood;
import com.bishe.hjh.ros.util.StreamUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * A placeholder fragment containing a simple view.
 */
public class CommitOrderActivityFragment extends Fragment {

    private Order order;
    private static final String ORDER = "order";
    private TextView tv_price_commit;
    private Button btn_commit_commit;
    private RadioGroup rg_take_here;
    private EatHereFragment fragment_here;
    private TakeFragment fragment_take;
    private SharedPreferences sh;
    private boolean isHere=true;

    public static CommitOrderActivityFragment newInstance(Order o) {

        Bundle args = new Bundle();
        args.putSerializable(ORDER,o);
        CommitOrderActivityFragment fragment = new CommitOrderActivityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public CommitOrderActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sh=getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        if (getArguments()!=null){
            order= (Order) getArguments().get(ORDER);
            try {
                Log.d("111111", "onCreate: "+order.toJson());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_commit_order, container, false);
        rg_take_here= (RadioGroup) v.findViewById(R.id.rg_take_here_commit);
        btn_commit_commit= (Button) v.findViewById(R.id.btn_commit_commit);
        tv_price_commit= (TextView) v.findViewById(R.id.tv_price_commit);

        btn_commit_commit.setOnClickListener(new View.OnClickListener() {
            //判断时候输入座号
            @Override
            public void onClick(View v) {
               new AsyncTask<String, String, String>() {
                   @Override
                   protected void onPostExecute(String s) {
                       try {
                           Log.d("111111", "onPostExecute: "+s);
                           JSONObject j=new JSONObject(s);
                           String rc=j.getString("resultCode");
                           Double yue=j.getDouble("money");
                           switch (rc){
                               case "1":
                                   Toast.makeText(getActivity(),"下单成功！请耐心等候美食",Toast.LENGTH_LONG).show();
                                   sh.edit().putString(order.getRestaurant().getName(),null).commit();
                                   Log.d("tijaio111111", "onPostExecute: "+sh.getString(order.getRestaurant().getName(),null));
                                   getActivity().setResult(Activity.RESULT_OK);
                                   getActivity().finish();
                                   break;
                               case "0":
                                   Toast.makeText(getActivity(),"登录用户错误，请从新登录",Toast.LENGTH_LONG).show();
                                   break;
                               case "2":
                                   Toast.makeText(getActivity(),"账号余额不足，请充值",Toast.LENGTH_LONG).show();
                                   break;
                           }
                       } catch (JSONException e) {
                           e.printStackTrace();
                       }

                   }

                   @Override
                    protected String doInBackground(String... params) {
                        String r=null;
                        try {
                            if (isHere){
                                 order=fragment_here.getOrderInfo();
                            }else {
                                order=fragment_take.getOrderInfo();

                            }
                            String s=order.toJson().toString();
                            String a=URLEncoder.encode(s,"UTF-8");
                            URL url=new URL("http://"+getResources().getString(R.string.ip)+":8080/web/servlet/CommitOrderServlet?order="+a);
                            HttpURLConnection con= (HttpURLConnection) url.openConnection();
                            con.setRequestMethod("GET");
                            con.setConnectTimeout(5000);
                            con.setReadTimeout(5000);
                            int code=con.getResponseCode();
                            if (code==200){
                                r= StreamUtils.parserStream(con.getInputStream());
                            }
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return r;
                    }
                }.execute();
            }
        });

        Double total=0.0;
        for (OrderFood of:order.getOrderFoods()){
            total=total+of.getNum()*of.getFoodId().getPrice();
        }
        tv_price_commit.setText(total+"￥");
        fragment_here=EatHereFragment.newInstance(order);
        FragmentManager fm=getFragmentManager();
        fm.beginTransaction().add(R.id.ll_fragment_container_commit,fragment_here).commit();
        order.setHereTake(0);
        rg_take_here.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FragmentManager fm=getFragmentManager();
                switch (checkedId){
                    case R.id.rb_here:
                        isHere=true;
                        order.setHereTake(0);
                        if (fragment_here==null)
                            fragment_here=new EatHereFragment();
                        fm.beginTransaction().replace(R.id.ll_fragment_container_commit,fragment_here).commit();
                        break;
                    case R.id.rb_take:
                        isHere=false;
                        order.setHereTake(1);
                        if (fragment_take==null)
                            fragment_take=new TakeFragment();
                        Bundle b=new Bundle();
                        b.putSerializable(TakeFragment.ARG_PARAM2,order);
                        fragment_take.setArguments(b);
                        fm.beginTransaction().replace(R.id.ll_fragment_container_commit,fragment_take).commit();
                        break;

                }
            }
        });
        return v;

    }


}
