package com.example.shop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.util.ArrayList;
import java.util.List;

public class LocationActivity extends AppCompatActivity {
    //TextView locationInfo;
    TextView name;
    EditText etadress;
    Button getAddress,confirm;
    LocationClient mlocationClient;
    StringBuilder currentPosition;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        //locationInfo = findViewById(R.id.location);
        etadress = findViewById(R.id.edit);
        getAddress = findViewById(R.id.getaddress);
        confirm = findViewById(R.id.confirm);
        name = findViewById(R.id.name);

        name.setText(name.getText()+User.getInstance().getname());

        getAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etadress.setText(currentPosition);
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteOpenHelper sqLiteOpenHelper = new MySQLiteOpenHelper(LocationActivity.this);
                SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
                String sql = "update user set address = ? where user_id = ?";
                db.execSQL(sql,new String[]{etadress.getText().toString(),User.getInstance().getuser_id()});

                finish();

            }
        });

        mlocationClient = new LocationClient(getApplicationContext());
        mlocationClient.registerLocationListener(new MLocationListener());



        List<String> primissionList = new ArrayList<String>();
        if(ContextCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            primissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED){
            primissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ContextCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            primissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(!primissionList.isEmpty()){
            String [] permissions = primissionList.toArray(new String[primissionList.size()]);
            ActivityCompat.requestPermissions(LocationActivity.this,permissions,1);
            requestLocation();
        }
        else {
            requestLocation();
        }
        //requestLocation();

    }
    private void requestLocation() {
        initLaction();
        mlocationClient.start();
    }

    private void initLaction() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);

        option.setCoorType("bd0911");
        option.setScanSpan(1000);
        option.setOpenGps(true);
        option.setLocationNotify(true);
        option.setIgnoreKillProcess(false);
        option.setWifiCacheTimeOut(5*60*1000);
        option.setEnableSimulateGps(false);
        option.setIsNeedAddress(true);
        mlocationClient.setLocOption(option);
    }
    class MLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            Log.i("TAG","Getthis");
             currentPosition = new StringBuilder();
             currentPosition.append(bdLocation.getAddrStr());

            Log.i("TAG",currentPosition.toString());

        }
    }
}
