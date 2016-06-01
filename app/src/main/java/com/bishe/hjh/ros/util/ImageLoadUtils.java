package com.bishe.hjh.ros.util;

import android.content.Context;
import android.widget.ImageView;

import com.bishe.hjh.ros.R;
import com.squareup.picasso.Picasso;

/**
 * Created by HJH on 2016/6/1.
 */
public class ImageLoadUtils {
    private static ImageLoadUtils instance;
    private ImageLoadUtils(){
    }
    public  static ImageLoadUtils Instance(){
        if (instance==null) {
            instance = new ImageLoadUtils();
        }
        return instance;
    }
    public void setImage(Context context,String url, ImageView imageView ){
        Picasso.with(context)
                .load(url)
                .error(R.drawable.loading_error)
                .placeholder(R.drawable.loading)
                .into(imageView);
    }
    public void setImage(Context context ,String url, ImageView imageView,int x,int y){
        Picasso.with(context)
                .load(url)
                .error(R.drawable.loading_error)
                .placeholder(R.drawable.loading)
                .resize(x,y)
                .centerCrop()
                .into(imageView);
    }
}
