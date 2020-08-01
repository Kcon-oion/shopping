package com.example.shop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    Button loginbtn,forgetbtn,registerbtn;
    EditText etusername,etpassword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginbtn = findViewById(R.id.login);
        initView();
        registertbtn();
        initViewlistener();
        initforgetbtn();


    }

    private void initforgetbtn() {
        forgetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,LosingActivity.class);
                startActivity(intent);
                finish();

//                Intent intent = new Intent(LoginActivity.this,testActivity.class);
//                startActivity(intent);
//                finish();
            }
        });
    }

    private void registertbtn() {
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initViewlistener() {
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etusername.getText().toString();
                String password = etpassword.getText().toString();
                if(TextUtils.isEmpty(username)){
                    etusername.setError("账号不能为空");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    etpassword.setError("密码不能为空");
                    return;
                }


                MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(LoginActivity.this);
                SQLiteDatabase db = mySQLiteOpenHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery("select * from user where name = ? and password = ?",new String[]{username,password});
                while (cursor.moveToNext()){
                    String user_id = cursor.getString(cursor.getColumnIndex("user_id"));
                    String user_password = cursor.getString(cursor.getColumnIndex("password"));
                    int areacode = cursor.getInt(cursor.getColumnIndex("areacode"));
                    String phone = cursor.getString(cursor.getColumnIndex("phone"));
                    String idcard = cursor.getString(cursor.getColumnIndex("idcard"));
 //                   Log.i("TAG",user_id);

                    User user =User.getInstance();
                    user.setuser_id(user_id);
                    user.setpassword(user_password);
                    user.setname(username);
                    user.setareacode(areacode);
                    user.setphone(phone);
                    user.setidcard(idcard);
                    Intent intent = new Intent(LoginActivity.this,Welcome.class);
                    startActivity(intent);
                    finish();
                }


//                Intent intent = new Intent(LoginActivity.this,Welcome.class);
//                startActivity(intent);
//                finish();

            }

        });
    }

    private void initView() {
        etusername = findViewById(R.id.username);
        etpassword = findViewById(R.id.password);
        loginbtn = findViewById(R.id.login);
        forgetbtn = findViewById(R.id.forget);
        registerbtn = findViewById(R.id.register);
    }
}
