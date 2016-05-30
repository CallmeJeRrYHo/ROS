package com.bishe.hjh.ros;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bishe.hjh.ros.bean.User;
import com.bishe.hjh.ros.util.ImagePath;
import com.bishe.hjh.ros.util.StreamUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    private static final String TAG = "uploadFile";
    private static final int TIME_OUT = 10 * 10000000; // 超时时间
    private static final String CHARSET = "utf-8"; // 设置编码
    public static final String SUCCESS = "1";
    public static final String FAILURE = "0";




    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String USER = "user";
    private static final int CODE_GALLERY_REQUEST = 1;
    private static final int CODE_RESULT_REQUEST = 2;
    private static final int EXIST = 10;
    private static final int RE_SUCCESS = 20;
    private static final int NET_ERRO = 30;
    private static final int IO_ERROR=1003;
    private static final int JSON_ERROR=1004;
    private EditText et_username;
    private EditText et_password;
    private EditText et_password2;
    private RadioButton rb_boy;
    private RadioButton rb_girl;
    private RadioGroup rg_sex;
    private Button btn_register;
    private Button btn_cancel;
    private User u;
    // TODO: Rename and change types of parameters
    private Bundle mParam1;
    private String mParam2;
    private int sex;
    private Button select;
    private ImageView imageView;
    private Uri image;
    private Handler h=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case EXIST:
                    Toast.makeText(getActivity(),"用户名已存在！",Toast.LENGTH_SHORT).show();
                    break;
                case RE_SUCCESS:
                    Toast.makeText(getActivity(),"注册成功",Toast.LENGTH_SHORT).show();
                    Intent i=new Intent();
                    i.putExtra(USER,u);
                    getActivity().setResult(Activity.RESULT_OK,i);
                    getActivity().finish();
                    break;
                case NET_ERRO:
                    Toast.makeText(getActivity(),"你的网络不给力！请稍后再试",Toast.LENGTH_SHORT).show();
                    break;
                case IO_ERROR:
                    Toast.makeText(getActivity(),"错误号："+IO_ERROR,Toast.LENGTH_SHORT).show();
                    break;
                case JSON_ERROR:
                    Toast.makeText(getActivity(),"错误号："+JSON_ERROR,Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    private String path;
    private String fileName;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(Bundle param1) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putBundle(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getBundle(ARG_PARAM1);
            if (mParam1!=null){
            Bitmap photo = mParam1.getParcelable("data");
            imageView.setImageBitmap(photo);}
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_register, container, false);
        et_password= (EditText) v.findViewById(R.id.et_password_register);
        et_password2= (EditText) v.findViewById(R.id.et_password2_register);
        et_username= (EditText) v.findViewById(R.id.et_userName_register);
        btn_cancel= (Button) v.findViewById(R.id.btn_cancel_register);
        btn_cancel.setOnClickListener(this);
        btn_register= (Button) v.findViewById(R.id.btn_register_register);
        btn_register.setOnClickListener(this);
        rg_sex= (RadioGroup) v.findViewById(R.id.rg_sex_register);
        rb_boy= (RadioButton) v.findViewById(R.id.rbtn_boy_register);
        rb_girl= (RadioButton) v.findViewById(R.id.rbtn_girl_register);
        select= (Button) v.findViewById(R.id.btn_select);
        select.setOnClickListener(this);
        imageView= (ImageView) v.findViewById(R.id.image1);
        rg_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==R.id.rbtn_boy_register){
                    sex=1;
                }else {
                    sex=0;
                }
            }
        });
        return v;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_register_register:
                if (et_username.getText().toString().length()<4||et_username.getText().toString().length()>20){
                    Toast.makeText(getActivity(),"你输入的账号格式不正确！请从新输入",Toast.LENGTH_LONG).show();
                    break;
                }
                if (et_password.getText().toString().length()<4||et_password.getText().toString().length()>20){
                    Toast.makeText(getActivity(),"你输入的密码位数不符合！",Toast.LENGTH_LONG).show();
                    break;
                }
                if(!et_password.getText().toString().equals(et_password2.getText().toString())){
                    Toast.makeText(getActivity(),"你输入2次密码不一致！",Toast.LENGTH_LONG).show();
                    break;
                }
                uploadImage();
                commitUser();
                break;
            case R.id.btn_cancel_register:
                getActivity().finish();
                break;
            case R.id.btn_select:
                Intent intentFromGallery = new Intent();
                // 设置文件类型
                intentFromGallery.setType("image/*");
                intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
                break;
        }
    }

    private void uploadImage() {
        final File file;
        if (image!=null){
            file=new File(ImagePath.getPath(getActivity(),image));
            fileName=file.getName();
        }
        else{
            fileName=null;
            return;
        }

        new Thread(){
            @Override
            public void run() {
                String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
                String PREFIX = "--", LINE_END = "\r\n";
                String CONTENT_TYPE = "multipart/form-data"; // 内容类型
                String RequestURL = "http://"+getResources().getString(R.string.ip)+":8080/web/servlet/RegisterServlet";
                try {
                    URL url = new URL(RequestURL);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(TIME_OUT);
                    conn.setConnectTimeout(TIME_OUT);
                    conn.setDoInput(true); // 允许输入流
                    conn.setDoOutput(true); // 允许输出流
                    conn.setUseCaches(false); // 不允许使用缓存
                    conn.setRequestMethod("POST"); // 请求方式
                    conn.setRequestProperty("Charset", CHARSET); // 设置编码
                    conn.setRequestProperty("connection", "keep-alive");
                    conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
                            + BOUNDARY);

                    Log.d("111111", "run: "+fileName);
                    if (file != null) {
                        /**
                         * 当文件不为空，把文件包装并且上传
                         */
                        OutputStream outputSteam = conn.getOutputStream();

                        DataOutputStream dos = new DataOutputStream(outputSteam);
                        StringBuffer sb = new StringBuffer();
                        sb.append(PREFIX);
                        sb.append(BOUNDARY);
                        sb.append(LINE_END);
                        /**
                         * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
                         * filename是文件的名字，包含后缀名的 比如:abc.png
                         */

                        sb.append("Content-Disposition: form-data; name=\"file\"; filename=\""

                                + file.getName() + "\"" + LINE_END);
                        sb.append("Content-Type: application/octet-stream; charset="
                                + CHARSET + LINE_END);
                        sb.append(LINE_END);
                        dos.write(sb.toString().getBytes());
                        InputStream is = new FileInputStream(file);
                        byte[] bytes = new byte[1024];
                        int len = 0;
                        while ((len = is.read(bytes)) != -1) {
                            dos.write(bytes, 0, len);
                        }
                        is.close();
                        dos.write(LINE_END.getBytes());
                        byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
                                .getBytes();
                        dos.write(end_data);
                        dos.flush();
                        /**
                         * 获取响应码 200=成功 当响应成功，获取响应的流
                         */
                        String s=StreamUtils.parserStream(conn.getInputStream());
                        int code=conn.getResponseCode();
                        if (code==200)
                            Log.d("222222", "run: " +s);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            /*    try {
                    URL url=new URL("http://192.168.1.119:8080/web/servlet/RegisterServlet");
                    HttpURLConnection con= (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    con.setUseCaches(false);
                    con.setRequestProperty("Connection", "Keep-Alive");
                    con.setRequestProperty("Charset", "UTF-8");
                    con.setRequestProperty("Content-Type",
                            "multipart/form-data;boundary="+boundary);
                    DataOutputStream dos=new DataOutputStream(con.getOutputStream());
                    dos.writeBytes(two+boundary+end);
                    dos.writeBytes("Content-Disposition: form-data; "+
                            "name=\"image\";filename=\"/sdcard/download/login.jpg\""+ end);
                    dos.writeBytes(end);
                    FileInputStream fs=new FileInputStream("/sdcard/download/login.jpg");
                    byte[] b= new byte[1024];
                    int length=-1;
                    while ((length=fs.read(b))!=-1){
                        dos.write(b,0,length);
                    }
                    dos.writeBytes(end);
                    dos.writeBytes(two+boundary+two+end);
                    fs.close();
                    dos.flush();

                    String s=StreamUtils.parserStream(con.getInputStream());
                    int code=con.getResponseCode();
                    if (code==200)
                    Log.d("222222", "run: " +s);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/

        }.start();
    }

    private void commitUser() {
        SharedPreferences sh=getActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
        u=new User(et_username.getText().toString(),et_password.getText().toString());
        u.setSex(sex);
        if (fileName!=null) {
            u.setUserImage("http://"+getResources().getString(R.string.ip)+":8080/web/upload/" + fileName);
        }else {
            u.setUserImage("http://"+getResources().getString(R.string.ip)+":8080/web/upload/boy.jpg");
        }
        u.setMoney(0.0);
        Log.d("111111", "commitUser: "+u.getUserName());
        new Thread(){
            @Override
            public void run() {
                Message m=Message.obtain();
                try {
                    JSONObject j=u.toJSON();
                    URL url=new URL("http://"+getResources().getString(R.string.ip)+":8080/web/servlet/RegisterServlet?user="+j.toString());
                    Log.d("111111", "run: "+j.toString());
                    HttpURLConnection con= (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setReadTimeout(5000);
                    con.setConnectTimeout(5000);
                    int code=con.getResponseCode();
                    if (code==200){
                        String s= StreamUtils.parserStream(con.getInputStream());
                        Log.d("111111", "run: "+s);
                        if (s.equals("1")){
                            m.what=EXIST;
                        }else if (s.equals("2")){
                            m.what=RE_SUCCESS;
                        }
                    }else {
                        m.what=NET_ERRO;
                    }
                } catch (JSONException e) {
                    m.what=JSON_ERROR;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    m.what=IO_ERROR;
                }finally {
                    h.sendMessage(m);
                }

            }
        }.start();
    }
    public String uri2Path(Uri uri)
    {
        int actual_image_column_index;
        String img_path;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().getContentResolver().query(uri, proj, null, null, null);
        actual_image_column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        img_path = cursor.getString(actual_image_column_index);
        return img_path;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(getActivity(), "取消", Toast.LENGTH_LONG).show();
            return;
        }

        switch (requestCode) {
            case CODE_GALLERY_REQUEST:
                //cropRawPhoto(data.getData());
                image=data.getData();
                imageView.setImageURI(image);
        }
    }

}


