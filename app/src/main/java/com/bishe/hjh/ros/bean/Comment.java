package com.bishe.hjh.ros.bean;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by HJH on 2016/5/4.
 */
public class Comment implements java.io.Serializable {
    private static final String ID = "id";
    private static final String USERID ="userid" ;
    private static final String RESID = "resid";
    private static final String DATE = "date";
    private static final String STAR = "star";
    private static final String COMMENT = "comment";
    private static final String ORDERID = "orderid";

    // Fields

    private Integer id;
    private Integer userId;
    private Integer resId;
    private String comment;
    private String date;
    private Double star;
    private int orderId;

    // Constructors

    /** default constructor */
    public Comment() {
    }

    /** minimal constructor */
    public Comment(Integer id, Integer userId, Integer resId) {
        this.id = id;
        this.userId = userId;
        this.resId = resId;
    }

    public  Comment(JSONObject j) throws JSONException {
        if (j.has(ID))
            setId(j.getInt(ID));
        if (j.has(DATE)){
            setDate(j.getString(DATE));
        }
        setUserId(j.getInt(USERID));
        setResId(j.getInt(RESID));
        if (j.has(STAR))
            setStar( j.getDouble(STAR));
        if (j.has(COMMENT))
            setComment(j.getString(COMMENT));
        if(j.has(ORDERID))
            setOrderId(j.getInt(ORDERID));
    }
    public JSONObject toJson() throws JSONException {
        JSONObject j=new JSONObject();
        j.put(ID,id);
        j.put(USERID,userId);
        j.put(RESID,resId);
        j.put(DATE,date);
        j.put(STAR,star);
        j.put(COMMENT,comment);
        j.put(ORDERID, orderId);

        return j;
    }
    /** full constructor */
    public Comment(Integer id, Integer userId, Integer resId, String comment,
                   String date) {
        this.id = id;
        this.userId = userId;
        this.resId = resId;
        this.comment = comment;
        this.date = date;
    }

    // Property accessors

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getResId() {
        return this.resId;
    }

    public void setResId(Integer resId) {
        this.resId = resId;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getStar() {
        return star;
    }

    public void setStar(Double star) {
        this.star = star;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}