package com.bishe.hjh.ros;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bishe.hjh.ros.bean.Order;
import com.bishe.hjh.ros.bean.OrderFood;
import com.bishe.hjh.ros.util.StreamUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link TakeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TakeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private Order mParam2;
    private EditText et_address;
    private EditText et_phone;
    private TextView tv_money;
    private LinearLayout ll_note;
    private TextView tv_restaurant;
    private TextView tv_note;
    private LinearLayout ll_main;
    public TakeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param2 Parameter 2.
     * @return A new instance of fragment TakeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TakeFragment newInstance(Order param2) {
        TakeFragment fragment = new TakeFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam2 = (Order) getArguments().get(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_take, container, false);
        et_address= (EditText) v.findViewById(R.id.et_address_take);
        et_phone= (EditText) v.findViewById(R.id.et_phone_take);
        tv_money= (TextView) v.findViewById(R.id.tv_money_take);
        ll_note= (LinearLayout) v.findViewById(R.id.ll_note_take);
        tv_restaurant= (TextView) v.findViewById(R.id.tv_restaurant_take);
        ll_main= (LinearLayout) v.findViewById(R.id.ll_main_tak);
        tv_note= (TextView) v.findViewById(R.id.tv_note_take);

        new AsyncTask<String ,String ,String>(){

            @Override
            protected String doInBackground(String... params) {
                String r=null;
                try {
                    URL url=new URL("http://"+getResources().getString(R.string.ip)+":8080/web/servlet/getUserMoney?username="+params[0]);
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
                }
                return r;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                tv_money.setText("余额："+s+"￥");

            }
        }.execute(mParam2.getUser().getUserName());
        tv_restaurant.setText(mParam2.getRestaurant().getName());


        mParam2.setDes(tv_note.getText().toString());
        for (OrderFood of:mParam2.getOrderFoods()){
            View it=inflater.inflate(R.layout.order_list_item,container,false);
            TextView name= (TextView) it.findViewById(R.id.tv_order_list_name_item);
            TextView num= (TextView) it.findViewById(R.id.tv_order_num_item);
            TextView price= (TextView) it.findViewById(R.id.tv_order_price_item);
            name.setText(of.getFoodId().getName());
            num.setText("x"+of.getNum());
            price.setText(of.getFoodId().getPrice()+"￥");
            ll_main.addView(it);
        }
        return v;
    }

    public Order getOrderInfo(){
        if (et_address.getText().toString()!=null){
            mParam2.setAddress(et_address.getText().toString());
        }else {
            Toast.makeText(getActivity(),"请输入地址",Toast.LENGTH_SHORT).show();
        }
        if (et_phone.getText().toString()!=null){
            mParam2.setPhone(et_phone.getText().toString());
        }else {
            Toast.makeText(getActivity(),"请输入电话号码",Toast.LENGTH_SHORT).show();
        }

        return mParam2;
    }

}
