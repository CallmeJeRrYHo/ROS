package com.bishe.hjh.ros;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bishe.hjh.ros.util.StreamUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Splash extends AppCompatActivity {

    private static final int GO_GUID = 1000;
    private static final int GO_HOME = 1001;
    private static final String CONFIG = "config";
    private static final int TIME = 3000;
    private boolean isFirst;
    private TextView tv_version;
    private String newVersion;
    private String apkurl;
    private String des;
    private TextView tv_download;
    protected Handler handler=new Handler(){
       @Override
       public void handleMessage(Message msg) {
            switch (msg.what){
                case GO_HOME:
                    goHome();
                    break;
                case SHOW_DIALOG:
                    showDia();
                    break;
                case IO_ERROR:
                    Toast.makeText(getApplicationContext(),"错误号："+IO_ERROR,Toast.LENGTH_SHORT).show();
                    goHome();
                    break;
                case JSON_ERROR:
                    Toast.makeText(getApplicationContext(),"错误号："+JSON_ERROR,Toast.LENGTH_SHORT).show();
                    goHome();
                    break;
                case SERVER_ERROR:
                    Toast.makeText(getApplicationContext(),"你的网络不太给力！",Toast.LENGTH_SHORT).show();
                    goHome();

            }
       }
   };
    private static final int SHOW_DIALOG=1002;
    private static final String TAG="hjh";
    private static final int IO_ERROR=1003;
    private static final int JSON_ERROR=1004;
    private static final int SERVER_ERROR=1005;
    private void showDia() {
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("检查到有新版本");
        builder.setMessage(des);
        builder.setCancelable(false);
        builder.setPositiveButton("立即升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                download();
            }
        });
        builder.setNegativeButton("暂不升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                goHome();
            }
        });
        builder.show();

    }
    private void download() {
        HttpUtils http=new HttpUtils();
        http.download(apkurl, "/mnt/sdcard/ros2.0.apk", new RequestCallBack<File>() {
            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
                Toast.makeText(getApplicationContext(),"下载已完成",Toast.LENGTH_SHORT).show();
                installAPK();
                finish();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(getApplicationContext(),"下载失败"+s,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                tv_download.setVisibility(View.VISIBLE);
                tv_download.setText(current+"/"+total);
            }
        });
    }

    private void installAPK() {
        Intent i=new Intent("android.intent.action.VIEW");
        i.addCategory("android.intent.category.DEFAULT");
        i.setDataAndType(Uri.fromFile(new File("/mnt/sdcard/ros2.0.apk")),"application/vnd.android.package-archive");
        startActivity(i);
    }

    private void goHome() {
        SharedPreferences sh=getSharedPreferences(CONFIG,MODE_PRIVATE);
        isFirst=sh.getBoolean("isFirst",true);
        if (!isFirst){
            Intent i=new Intent(Splash.this,newHome.class);
            startActivity(i);

        }else{
            sh.edit().putBoolean("isFirst",false).commit();
            Intent i=new Intent(Splash.this,Guid.class);
            startActivity(i);
        }
        finish();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        tv_version= (TextView) findViewById(R.id.tv_version_splash);
        tv_version.setText("版本号："+getVersion());
        tv_download= (TextView) findViewById(R.id.tv_download_splash);
        update();
      /*  SharedPreferences sh=getSharedPreferences(CONFIG,MODE_PRIVATE);
        isFirst=sh.getBoolean("isFirst",true);
        if (isFirst){
            handler.sendEmptyMessageDelayed(GO_GUID,TIME);
            sh.edit().putBoolean("isFirst",false).commit();
        }else{
            handler.sendEmptyMessageDelayed(GO_HOME,TIME);
        }*/

    }
/*
* 获取版本号
*
* */
    public String getVersion(){
        PackageManager manager=getPackageManager();
        try {
            PackageInfo packageInfo=manager.getPackageInfo(getPackageName(),0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }
    /*
    * 检查更新
    * */
    public void update(){
        new Thread(){
            @Override
            public void run() {
                Message m=Message.obtain();
                long start=System.currentTimeMillis();
                try {
                    URL url=new URL("http://"+getResources().getString(R.string.ip)+":8080/updateinfo%20.html");
                    HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    connection.setRequestMethod("GET");
                    int code=connection.getResponseCode();
                    Log.d(TAG,"" +code);

                    if (code==200){
                        String s=StreamUtils.parserStream(connection.getInputStream());
                        Log.d(TAG,s);

                        JSONObject j=new JSONObject(s);
                        newVersion=j.getString("code");
                        des=j.getString("des");
                        apkurl=j.getString("apkurl");

                        if (newVersion.equals(getVersion())){
                            m.what=GO_HOME;
                        }else {
                            m.what=SHOW_DIALOG;

                        }
                        System.out.print(s);
                    }else {
                       m.what=SERVER_ERROR;
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    m.what=IO_ERROR;
                } catch (JSONException e) {
                    m.what=JSON_ERROR;
                    e.printStackTrace();
                }finally {
                    long dTime=System.currentTimeMillis()-start;
                    if (dTime>2000){
                        handler.sendMessage(m);
                    }else{
                        try {
                            sleep(2000-dTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        handler.sendMessage(m);
                    }
                }
            }
        }.start();

    }

}
