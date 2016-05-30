package com.bishe.hjh.ros.bean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by HJH on 2016/4/12.
 */
public class caishi implements Serializable{
    private static final String JSON_COMMENT = "comment";
    private static final String JSON_RESID = "resid";
    private static final String JSON_DES = "des";
    private static final String JSON_IMAGE = "image";
    private static final String JSON_SELL ="sell" ;
    private static final String JSON_STAR = "star";
    private static final String JSON_PRICE = "price";
    private static final String JSON_ID = "id";
    private static final String JSON_NAME = "name";
    private int id;
    private Double price;
    private String name;
    private int praise;
    private String des;
    private int sell;
    private String imageFile;
    private int resId;
    private int commentId;

    public caishi(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPraise() {
        return praise;
    }

    public void setPraise(int praise) {
        this.praise = praise;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public int getSell() {
        return sell;
    }

    public void setSell(int sell) {
        this.sell = sell;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
    public JSONObject toJSON() throws JSONException {
        JSONObject j=new JSONObject();
        j.put(JSON_NAME,name);
        j.put(JSON_ID,id);
        j.put(JSON_PRICE,price);
        j.put(JSON_STAR,praise);
        j.put(JSON_SELL,sell);
        j.put(JSON_IMAGE,imageFile);
        j.put(JSON_DES,des);
        j.put(JSON_RESID,resId);
        j.put(JSON_COMMENT,commentId);
        return j;
    }
    public caishi(JSONObject j) throws JSONException {
        id=j.getInt(JSON_ID);
        setName(j.getString(JSON_NAME));
        setSell(j.getInt(JSON_SELL));
        setPrice(j.getDouble(JSON_PRICE));
        setPraise(j.getInt(JSON_STAR));
        setImageFile(j.getString(JSON_IMAGE));
        setCommentId(j.getInt(JSON_COMMENT));
        setDes(j.getString(JSON_DES));
        setResId(j.getInt(JSON_RESID));
    }
}
