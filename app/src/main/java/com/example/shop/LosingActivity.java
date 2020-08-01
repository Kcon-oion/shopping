package com.example.shop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;

import com.mob.MobSDK;

import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

public class LosingActivity extends AppCompatActivity {
    // 填写从短信SDK应用后台注册得到的APPKEY
    private static String APPKEY = "2ffa414d2b7d3";
    // 填写从短信SDK应用后台注册得到的APPSECRET
    private static String APPSECRET = "6ab668aca95357472de32730a33b4b31";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_losing);
        MobSDK.submitPolicyGrantResult(true, null);
        sendCode(LosingActivity.this);



    }

    private void setData() {
        SQLiteOpenHelper sqLiteOpenHelper = new MySQLiteOpenHelper(LosingActivity.this);
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        String sql = "select * from user where phone = ?";
        Cursor cursor = db.rawQuery(sql,new String[]{User.getInstance().getphone()});
        while(cursor.moveToNext()){
            String name =cursor.getString(cursor.getColumnIndex("name"));
            int user_id = cursor.getInt(cursor.getColumnIndex("user_id"));
            String password = cursor.getString(cursor.getColumnIndex("password"));
            String idcard = cursor.getString(cursor.getColumnIndex("idcard"));

            User user = User.getInstance();
            user.setname(name);
            user.setuser_id(String.valueOf(user_id));
            user.setpassword(password);
            user.setidcard(idcard);
        }

    }

    public void sendCode(Context context) {
        RegisterPage page = new RegisterPage();
        //如果使用我们的ui，没有申请模板编号的情况下需传null
        page.setTempCode(null);
        page.setRegisterCallback(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    User user = User.getInstance();

                    // 处理成功的结果
                    HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
                    // 国家代码，如“86”
                    String country = (String) phoneMap.get("country");
                    Log.i("TAG",country);
                    user.setareacode(Integer.parseInt(country));
                    // 手机号码，如“13800138000”
                    String phone = (String) phoneMap.get("phone");
                    Log.i("TAG",phone);
                    user.setphone(phone);
                    // TODO 利用国家代码和手机号码进行后续的操作
                    setData();
                    Intent intent = new Intent(LosingActivity.this,Welcome.class);
                    startActivity(intent);
                    finish();
                } else{
                    // TODO 处理错误的结果
                }
            }
        });
        page.show(context);
    }
}
