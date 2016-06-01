package com.bishe.hjh.ros;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.snappingstepper.SnappingStepper;
import com.bigkoo.snappingstepper.listener.SnappingStepperValueChangeListener;
import com.bishe.hjh.ros.bean.Order;
import com.bishe.hjh.ros.bean.OrderFood;
import com.bishe.hjh.ros.bean.User;
import com.bishe.hjh.ros.bean.caishi;
import com.bishe.hjh.ros.util.ImageLoadUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class caishiDetailActivityFragment extends Fragment {

    private static final String CAISHI = "caishi";
    private static final String OREDER = "order";
    private static final String RESTAURANT = "res";
    private ImageView iv_photo;
    private TextView tv_name;
    private TextView tv_sell;
    private TextView tv_price;
    private RatingBar rb_star;
    private SnappingStepper stp_add;
    private TextView tv_des;
    private caishi c;
    private Order order=null;

    private Restaurant r;
    private User user=null;

    public caishiDetailActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            c= (caishi) getArguments().get(CAISHI);
            order= (Order) getArguments().get(OREDER);
            r= (Restaurant) getArguments().get(RESTAURANT);
        }
    }

    public static caishiDetailActivityFragment newInstance(caishi c , Order o,Restaurant r) {

        Bundle args = new Bundle();
        args.putSerializable(RESTAURANT,r);
        args.putSerializable(CAISHI,c);
        args.putSerializable(OREDER,o);
        caishiDetailActivityFragment fragment = new caishiDetailActivityFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_caishi_detail, container, false);
        tv_des= (TextView) v.findViewById(R.id.tv_des_detail);
        tv_des.setText(c.getDes());
        tv_name= (TextView) v.findViewById(R.id.tv_name_detail);
        tv_name.setText(c.getName());
        tv_price= (TextView) v.findViewById(R.id.tv_price_detail);
        tv_price.setText("￥"+c.getPrice());
        tv_sell= (TextView) v.findViewById(R.id.tv_sell_detail);
        tv_sell.setText("已售："+c.getSell());
        iv_photo= (ImageView) v.findViewById(R.id.iv_caishi_detail);
        ImageLoadUtils.Instance().setImage(getContext(),c.getImageFile(),iv_photo);
        stp_add= (SnappingStepper) v.findViewById(R.id.stp_add_detail);

        SharedPreferences sh=getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String u=sh.getString("user",null);
        if (u!=null) {
            try {
                JSONObject j = new JSONObject(u);
                user = new User(j);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (order!=null) {
            for (OrderFood of : order.getOrderFoods()) {
                if (of.getFoodId().getId() == c.getId()) {
                    stp_add.setValue(of.getNum());
                }
            }
        }
        stp_add.setOnValueChangeListener(new SnappingStepperValueChangeListener() {
            @Override
            public void onValueChange(View view, int value) {

                //判断是否登录
                if (user!=null){

                    //判断订单是否存在
                    if (order==null) {
                        order = new Order();
                        Log.d("111111", "onValueChange: is null");
                        order.setRestaurant(r);
                        order.setUser(user);
                        order.setHereTake(0);
                        order.setSum(0.0);
                        order.setOrderId(111);
                        OrderFood of = new OrderFood( c, order.getOrderId(), value);
                        List<OrderFood> os = order.getOrderFoods();
                        os.add(of);

                        try {
                            order.toJson();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else {

                        //判断数量是否为0
                        if (value == 0) {
                            Iterator<OrderFood> it=order.getOrderFoods().iterator();
                            while (it.hasNext()){
                                if (it.next().getFoodId().getId()==c.getId()){
                                    it.remove();
                                }
                            }
                        } else {
                            boolean isExist = false;
                            for (OrderFood of : order.getOrderFoods()) {
                                if (of.getFoodId().getId() == c.getId()) {
                                    of.setNum(value);
                                    isExist = true;
                                    break;
                                }
                            }
                            if (!isExist) {
                                OrderFood of = new OrderFood(c, order.getOrderId(), value);
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
                    Intent i=new Intent();
                    i.putExtra(OREDER,order);
                    getActivity().setResult(Activity.RESULT_OK,i);
                }else {
                    Toast.makeText(getActivity(),"请先登录！",Toast.LENGTH_SHORT).show();
                }
            }
        });
        rb_star= (RatingBar) v.findViewById(R.id.rb_star_detail);
        rb_star.setRating(c.getPraise());
        return v;
    }


}
