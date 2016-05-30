package com.bishe.hjh.ros.bean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by HJH on 2016/4/14.
 */
public class User implements Serializable{
    private static final String ORDER = "order";
    private static final String MONEY = "money";
    private static final String IMAGE = "image";
    private static final String SEX = "sex";
    private static final String NAME = "name";
    private static final String ID = "id";
    private static final String PASS = "password";
    private int userId;
    private String userName;
    private String password;
    private String userImage;
    private Double money;
    private Integer orderList;
    private int sex;
    public User() {
    }
    public User(String username, String password) {
        this.userName = username;
        this.password = password;
    }

    /** minimal constructor */
    public User(String username, String password, Integer sex) {
        this.userName = username;
        this.password = password;
        this.sex = sex;
    }

    /** full constructor */
    public User(String username, String password, Integer sex,
                String userImage, Double money, Integer orderList) {
        this.userName = username;
        this.password = password;
        this.sex = sex;
        this.userImage = userImage;
        this.money = money;
        this.orderList = orderList;
    }
    public User(int id){
        this.userId=id;
    }
    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserId() {
        return userId;
    }


    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Integer getOrderList() {
        return orderList;
    }

    public void setOrderList(Integer orderList) {
        this.orderList = orderList;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject j=new JSONObject();
        j.put(ID,getUserId());
        j.put(NAME,getUserName());
        j.put(PASS,getPassword());
        j.put(SEX,getSex());
        j.put(IMAGE,getUserImage());
        j.put(MONEY,getMoney());
        j.put(ORDER,getOrderList());
        return j;
    }
    public User(JSONObject j) throws JSONException {
        this.userId=j.getInt(ID);
        this.userName=j.getString(NAME);
        this.password=j.getString(PASS);
        this.sex=j.getInt(SEX);
        this.userImage=j.getString(IMAGE);
        this.money= j.getDouble(MONEY);
        this.orderList=j.getInt(ORDER);
    }
}
