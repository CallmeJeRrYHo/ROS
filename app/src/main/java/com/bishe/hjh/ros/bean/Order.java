package com.bishe.hjh.ros.bean;

import com.bishe.hjh.ros.Restaurant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HJH on 2016/4/22.
 */
public class Order  implements Serializable{
    private static final String JSON_ID = "id";
    private static final String JSON_RESTAURANT = "restaurant";
    private static final String JSON_USER = "user";
    private static final String JSON_SUM = "sum";
    private static final String JSON_ISTAKE = "istake";
    private static final String JSON_ADDRESS = "add";
    private static final String JSON_PHONE = "phone";
    private static final String JSON_FOODORDER = "food_order";
    private static final String JSON_DES = "des";
    private static final String JSON_DESKNO = "deskNo";
    private static final String JSON_ISCOMMENT = "isComment";
    private Integer orderId;
    private Restaurant restaurant;
    private User user;
    private Double sum;
    private int hereTake;
    private String address;
    private String phone;
    private boolean isComment;
    private List<OrderFood> orderFoods = new ArrayList<OrderFood>(0);

    private String des;
    private String deskNo;
    // Constructors

    /** default constructor */
    public Order() {
    }

    public Order(JSONObject j) throws JSONException {
        if (j.has(JSON_ID)){
            setOrderId(j.getInt(JSON_ID));
        }
        if (j.has(JSON_ISCOMMENT))
            setComment(j.getBoolean(JSON_ISCOMMENT));
        setRestaurant(new Restaurant((JSONObject) j.get(JSON_RESTAURANT)));
        setUser(new User((JSONObject) j.get(JSON_USER)));
        setSum(j.getDouble(JSON_SUM));
        if (j.has(JSON_ISTAKE))
            setHereTake(j.getInt(JSON_ISTAKE));
        if (j.has(JSON_ADDRESS)){
            setAddress(j.getString(JSON_ADDRESS));
        }
        if (j.has(JSON_PHONE)){
            setPhone(j.getString(JSON_PHONE));
        }
        JSONArray ja= j.getJSONArray(JSON_FOODORDER);
        for (int i=0;i<ja.length();i++){
            orderFoods.add(new OrderFood(ja.getJSONObject(i)));
        }
        if (j.has(JSON_DES)){
            setDes(j.getString(JSON_DES));
        }
        if (j.has(JSON_DESKNO)){
            setDeskNo(j.getString(JSON_DESKNO));
        }
    }
    public JSONObject toJson() throws JSONException {
        JSONObject j=new JSONObject();
        j.put(JSON_ID,orderId);
        j.put(JSON_RESTAURANT,restaurant.toJSON());
        j.put(JSON_USER,user.toJSON());
        j.put(JSON_SUM,sum);
        j.put(JSON_ISTAKE,hereTake);
        j.put(JSON_ADDRESS,address);
        j.put(JSON_PHONE,phone);
        j.put(JSON_DES,des);
        j.put(JSON_DESKNO,deskNo);
        j.put(JSON_ISCOMMENT,isComment);
        JSONArray ja=new JSONArray();
        for (OrderFood o:orderFoods){
            ja.put(o.toJson());
        }
        j.put(JSON_FOODORDER,ja);
        return j;
    }
    /** full constructor */
    public Order(Restaurant restaurant, User user, Double sum,
                 Integer hereTake, String address, String phone, List<OrderFood> orderFoods) {
        this.restaurant = restaurant;
        this.user = user;
        this.sum = sum;
        this.hereTake = hereTake;
        this.address = address;
        this.phone = phone;
        this.orderFoods = orderFoods;
    }

    // Property accessors

    public Integer getOrderId() {
        return this.orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Restaurant getRestaurant() {
        return this.restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Double getSum() {
        return this.sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    public Integer getHereTake() {
        return this.hereTake;
    }

    public void setHereTake(Integer hereTake) {
        this.hereTake = hereTake;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<OrderFood> getOrderFoods() {
        return this.orderFoods;
    }

    public void setOrderFoods(List<OrderFood> orderFoods) {
        this.orderFoods = orderFoods;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getDeskNo() {
        return deskNo;
    }

    public void setDeskNo(String deskNo) {
        this.deskNo = deskNo;
    }


    public boolean isComment() {
        return isComment;
    }

    public void setComment(boolean comment) {
        isComment = comment;
    }
}
