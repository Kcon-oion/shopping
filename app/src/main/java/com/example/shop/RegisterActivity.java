package com.example.shop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {
    Button registerbtn;
    EditText etusername,etpassword,etphone,etidcard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etusername = findViewById(R.id.username);
        etpassword = findViewById(R.id.password);
        etphone = findViewById(R.id.phone);
        registerbtn =findViewById(R.id.register);
        etidcard = findViewById(R.id.idcard);

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etusername.getText().toString();
                String password = etpassword.getText().toString();
                String phone =   etphone.getText().toString() ;
                String idcard = etidcard.getText().toString();
                Log.i("TAG",name);
                Log.i("TAG",password);
                Log.i("TAG",phone);
                Log.i("TAG",idcard);
                MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(RegisterActivity.this);
                SQLiteDatabase  db = mySQLiteOpenHelper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put("name",name);
                values.put("password",password);
                values.put("phone",phone);
                values.put("idcard",idcard);
                values.put("areacode",86);
                db.insert("user",null,values);

                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
