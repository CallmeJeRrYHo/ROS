package com.bishe.hjh.ros;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bishe.hjh.ros.bean.User;
import com.bishe.hjh.ros.util.StreamUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link MeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String NET_ERROR = "netError";
    public static final int ME = 5000;
    public static final int LOGIN = 1002;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private User user;
    private CircleImageView userImage;
    private TextView userName;
    private LinearLayout money;
    private LinearLayout order;
    private TextView tv_money;
    private ImageView setting;
    private LinearLayout logout;
    private boolean isLogin=false;
    private ImageLoader loader;
    private String u;
    private String un=null;
    public MeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MeFragment newInstance(String param1, String param2) {
        MeFragment fragment = new MeFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
          user= (User) getArguments().getSerializable("user");
            isLogin=true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_me, container, false);
        final SharedPreferences sh=getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        u=sh.getString("user",null);

        //init
        userImage= (CircleImageView) v.findViewById(R.id.civ_userImage_me);
        userName= (TextView) v.findViewById(R.id.tv_userName_me);
        setting= (ImageView) v.findViewById(R.id.ib_setting_me);
        money= (LinearLayout) v.findViewById(R.id.ll_money_me);
        order= (LinearLayout) v.findViewById(R.id.ll_order_me);
        tv_money= (TextView) v.findViewById(R.id.tv_money_me);
        logout= (LinearLayout) v.findViewById(R.id.ll_logout_me);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (u!=null){
                    AlertDialog.Builder b=new AlertDialog.Builder(getActivity());
                    b.setTitle("退出登录");
                    b.setMessage("确定退出登录吗？");
                    b.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sh.edit().clear().commit();
                            un=null;
                        }
                    });
                    b.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    b.create().show();

                }else {
                    Toast.makeText(getActivity(),"请先登录！",Toast.LENGTH_SHORT).show();
                }

            }
        });

        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(getActivity());
        ImageLoader.getInstance().init(configuration);
        loader = ImageLoader.getInstance();
        if (isLogin){
            loader.displayImage(user.getUserImage(),userImage);
            userName.setText(user.getUserName());
        }else if (u!=null){
            try {
                user=new User(new JSONObject(u));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            loader.displayImage(user.getUserImage(),userImage);
            userName.setText(user.getUserName());
            un=user.getUserName();
        }
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (u==null){
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(i,LOGIN);
                }
            }
        });
        new AsyncTask<String ,String ,String>(){

            @Override
            protected String doInBackground(String... params) {
                String r=null;
                if (un==null)
                    return r;
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
                    r=NET_ERROR;
                }finally {
                    return r;

                }
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s!=null){
                    if (s.equals(NET_ERROR))
                        Toast.makeText(getActivity(),"您的网络不给力请稍后再试",Toast.LENGTH_SHORT).show();
                    else
                        tv_money.setText("余 额："+s+"￥");
                }

            }
        }.execute(un);


        return v;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode== Activity.RESULT_OK) {
            User user1 = (User) data.getSerializableExtra(LoginActivity.USER);
            loader.displayImage(user1.getUserImage(),userImage);
            userName.setText(user1.getUserName());
            tv_money.setText("余 额："+user1.getMoney()+"￥");
            Log.d("111111", "onActivityResult: " + user1.getUserName());
        }
    }
}
