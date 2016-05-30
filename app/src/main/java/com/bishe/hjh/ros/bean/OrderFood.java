package com.bishe.hjh.ros.bean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by HJH on 2016/4/22.
 */
public class OrderFood  implements Serializable{

    private static final String JSON_NUM = "num";
    private static final String JSON_ID = "id";
    private static final String JSON_FOOD = "food";
    private static final String JSON_ORDER = "order";
    private Integer id;
    private caishi foodId;
    private int orderId;
    private Integer num;

    // Constructors

    /** default constructor */
    public OrderFood() {
    }

    public OrderFood(JSONObject j) throws JSONException {
        if (j.has(JSON_ID))
            setId(j.getInt(JSON_ID));
        setFoodId(new caishi((JSONObject) j.get(JSON_FOOD)));
        setOrder(j.getInt(JSON_ORDER));
        setNum(j.getInt(JSON_NUM));

    }
    public JSONObject toJson() throws JSONException {
        JSONObject j=new JSONObject();
        j.put(JSON_ID,id);
        j.put(JSON_FOOD,foodId.toJSON());
        j.put(JSON_ORDER,orderId);
        j.put(JSON_NUM,num);
        return j;
    }
    /** full constructor */
    public OrderFood(caishi foodId, int orderId, Integer num) {
        this.foodId = foodId;
        this.orderId = orderId;
        this.num = num;
    }

    // Property accessors

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getOrder() {
        return this.orderId;
    }

    public void setOrder(int order) {
        this.orderId = order;
    }

    public Integer getNum() {
        return this.num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public caishi getFoodId() {
        return foodId;
    }

    public void setFoodId(caishi foodId) {
        this.foodId = foodId;
    }
}
