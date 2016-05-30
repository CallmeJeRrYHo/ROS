package com.bishe.hjh.ros;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bishe.hjh.ros.bean.User;
import com.bishe.hjh.ros.util.StreamUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_REGISTER = 1;
    private static final int LOGIN_SUCCESS=1;
    private static final int LOGIN_NOTEXIST=2;
    private static final int LOGIN_NOTMACTH=3;
    private static final int WANGLUOBUGEILI = 4;
    public static final String USER = "user";
    private EditText et_count;
    private EditText et_password;
    private Button btn_login;
    private String userName;
    private String password;
    private TextView tv_reg;
    private User u;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {

            b.dismiss();
            switch (msg.what){
                case LOGIN_SUCCESS:
                    SharedPreferences sh=getSharedPreferences("userInfo",MODE_PRIVATE);
                    try {
                        sh.edit().putString("user",back.toJSON().toString()).commit();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(),"登录成功",Toast.LENGTH_LONG).show();
                    Intent i=new Intent();
                    i.putExtra(USER,back);
                    setResult(RESULT_OK,i);
                    finish();
                    break;
                case LOGIN_NOTEXIST:
                    Toast.makeText(getApplicationContext(),"用户不存在！请检查用户名",Toast.LENGTH_LONG).show();
                    break;
                case LOGIN_NOTMACTH:
                    Toast.makeText(getApplicationContext(),"密码不正确！请输入正确密码",Toast.LENGTH_LONG).show();
                    break;
                case WANGLUOBUGEILI:
                    Toast.makeText(getApplicationContext(),"你的网络不给力！请检查网络 ",Toast.LENGTH_LONG).show();

            }
        }
    };
    private Dialog b;
    private User back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_login);
        et_count= (EditText) findViewById(R.id.et_userName_login);
        et_password= (EditText) findViewById(R.id.et_password_login);
        btn_login= (Button) findViewById(R.id.btn_login_login);
        btn_login.setOnClickListener(this);
        tv_reg= (TextView) findViewById(R.id.tv_register_login);
        tv_reg.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        tv_reg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login_login:
                b=new AlertDialog.Builder(this).setView(R.layout.login_dialog_item).setCancelable(false).create();
                b.show();
                userName=et_count.getText().toString();
                password=et_password.getText().toString();
                if (userName==null||userName.length()<=0){
                    et_count.requestFocus();
                    et_count.setError("账号不能为空");
                    break;
                }
                new Thread(){
                    @Override
                    public void run() {
                        Message m=Message.obtain();
                        try {

                            URL url=new URL("http://"+getResources().getString(R.string.ip)+":8080/web/servlet/LoginServlet?username="+userName+"&&password="+password);
                            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                            connection.setConnectTimeout(5000);
                            connection.setRequestMethod("GET");
                            connection.setReadTimeout(5000);
                            int code=connection.getResponseCode();
                            if (code==200){
                                String s= StreamUtils.parserStream(connection.getInputStream());
                                Log.d("111111",s);
                                JSONObject j= new JSONObject(s);
                                s=j.getString("result");
                                Log.d("111111",s);

                                if (s.equals("1")){
                                    m.what=LOGIN_SUCCESS;
                                    JSONObject b=j.getJSONObject("user");
                                    back=new User(b);
                                }else if (s.equals("2")){
                                    m.what=LOGIN_NOTEXIST;
                                }else if (s.equals("3")){
                                    m.what=LOGIN_NOTMACTH;
                                }
                            }else {
                                m.what=WANGLUOBUGEILI;
                            }
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            handler.sendMessage(m);
                        }
                    }
                }.start();

              /*  if (password.equals("admin")&&userName.equals("admin")){
                    SharedPreferences sh=getSharedPreferences("userInfo",MODE_PRIVATE);
                    sh.edit().putString("username",userName).commit();
                    sh.edit().putString("password",password).commit();
                    sh.edit().putString("image",u.getUserImage()).commit();
                    Toast.makeText(this,"登录成功",Toast.LENGTH_LONG).show();

                }else {
                    Toast.makeText(this,"用户名或者密码不正确",Toast.LENGTH_LONG).show();
                }*/
                break;
            case R.id.tv_register_login:
                Intent i=new Intent(this,RegisterActivity.class);
                startActivityForResult(i,REQUEST_REGISTER);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==REQUEST_REGISTER){
            if (resultCode==RESULT_OK){
                u = (User) data.getSerializableExtra(RegisterFragment.USER);
                et_count.setText(u.getUserName());
                et_password.setText(u.getPassword());
            }
        }
    }
}
