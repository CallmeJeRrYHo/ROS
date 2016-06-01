package com.bishe.hjh.ros;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by HJH on 2016/4/10.
 */
public class Restaurant  implements Serializable{
    private static final String JSON_ID ="id" ;
    private static final String JSON_NAME = "name";
    private static final String JSON_SELL = "sell";
    private static final String JSON_STAR = "star";
    private static final String JSON_PHOTO = "photo";
    private static final String JSON_PHONE = "phone";
    private static final String JSON_ADDRE = "address";
    private static final String JSON_DES = "des";
    private String name;
    private int ratingStar;
    private int sell;
    private int id;
    private String ImageFile;
    private String resDes;
    private String resAddress;
    private String resPhone;

    public Restaurant(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public Restaurant(JSONObject j) throws JSONException {
        id=j.getInt(JSON_ID);
        name=j.getString(JSON_NAME);
        sell=j.getInt(JSON_SELL);
        ratingStar=j.getInt(JSON_STAR);
        if (j.getString(JSON_PHOTO)!=null)
            ImageFile=j.getString(JSON_PHOTO);
        if (j.has(JSON_ADDRE)) {
            resAddress = j.getString(JSON_ADDRE);
        }
        else{
            resAddress="wu";
        }
        resDes=j.getString(JSON_DES);
        resPhone=j.getString(JSON_PHONE);
    }
    public JSONObject toJSON() throws JSONException {
        JSONObject j=new JSONObject();
        j.put(JSON_ID,id);
        j.put(JSON_NAME,name);
        j.put(JSON_SELL,sell);
        j.put(JSON_STAR,ratingStar);
        j.put(JSON_PHOTO,ImageFile);
        j.put(JSON_PHONE,resPhone);
        j.put(JSON_ADDRE,resAddress);
        j.put(JSON_DES,resDes);
        return j;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRatingStar() {
        return ratingStar;
    }

    public void setRatingStar(int ratingStar) {
        this.ratingStar = ratingStar;
    }

    public int getSell() {
        return sell;
    }

    public void setSell(int sell) {
        this.sell = sell;
    }

    public int getId() {
        return id;
    }

    public String getImageFile() {
        return ImageFile;
    }

    public void setImageFile(String imageFile) {
        ImageFile = imageFile;
    }

    public String getResPhone() {
        return resPhone;
    }

    public void setResPhone(String resPhone) {
        this.resPhone = resPhone;
    }

    public String getResAddress() {
        return resAddress;
    }

    public String getResDes() {
        return resDes;
    }

    public void setResDes(String resDes) {
        this.resDes = resDes;
    }

    public void setResAddress(String resAddress) {
        this.resAddress = resAddress;
    }
}
