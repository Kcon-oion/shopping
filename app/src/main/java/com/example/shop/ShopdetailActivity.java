package com.example.shop;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ShopdetailActivity extends AppCompatActivity {
    Button buybtn,joinbtn;
    TextView etprice,etdescription,etshipadress;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopdetail);
        etprice = findViewById(R.id.price);
        etdescription = findViewById(R.id.description);
        etshipadress = findViewById(R.id.shipadress);
        imageView = findViewById(R.id.photo);
        buybtn = findViewById(R.id.buy);
        joinbtn = findViewById(R.id.join);

        Bundle bundle = getIntent().getExtras();
        int shop_id = Integer.parseInt(bundle.getString("shop_id").toString()) ;
        String description = bundle.get("description").toString();
        int salevolume = Integer.parseInt(bundle.get("salevolume").toString()) ;
        int price = Integer.parseInt(bundle.get("price").toString()) ;
        String photo = bundle.get("photo").toString();
        String name = bundle.get("name").toString();
        String shipaddress = bundle.get("shipaddress").toString();
        int resID = getResources().getIdentifier(photo.toString(), "drawable", "com.example.shop");

//        Log.i("TAG", String.valueOf(shop_id));
//        Log.i("TAG",description);
//        Log.i("TAG", String.valueOf(salevolume));
//        Log.i("TAG", String.valueOf(price));
//        Log.i("TAG",photo);
//        Log.i("TAG",name);
//        Log.i("TAG",shipaddress);

        imageView.setImageResource(resID);
        etprice.setText("￥"+price);
        etshipadress.setText("发货地 "+shipaddress);
        etdescription.setText(description);
        //Log.i("TAG",description);

        SQLiteOpenHelper sqLiteOpenHelper = new MySQLiteOpenHelper(ShopdetailActivity.this);
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();

       // contentValues.put();
        buybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dlg = new AlertDialog.Builder(ShopdetailActivity.this);
                dlg.setTitle("尊敬的用户");
                dlg.setMessage("是否支付已选中商品 价格:"+price);
                dlg.setNegativeButton("取消",null);
                dlg.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("use_id",Integer.parseInt(User.getInstance().getuser_id()));
                        contentValues.put("shop_id",shop_id);
                        contentValues.put("price",price);
                        contentValues.put("name",name);
                        contentValues.put("number",1);
                        contentValues.put("description",description);
                        contentValues.put("photo",photo);
                        contentValues.put("checking",2);
                        db.insert("shoppingtrolley",null,contentValues);
                        finish();
                    }


                });

                dlg.create();
                dlg.show();

            }
        });

        joinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("use_id",Integer.parseInt(User.getInstance().getuser_id()));
                contentValues.put("shop_id",shop_id);
                contentValues.put("price",price);
                contentValues.put("name",name);
                contentValues.put("number",1);
                contentValues.put("description",description);
                contentValues.put("photo",photo);
                contentValues.put("checking",0);
                db.insert("shoppingtrolley",null,contentValues);
                finish();
            }
        });
    }
}
