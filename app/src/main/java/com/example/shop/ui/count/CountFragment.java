package com.example.shop.ui.count;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.shop.LocationActivity;
import com.example.shop.LoginActivity;
import com.example.shop.OrderActivity;
import com.example.shop.R;
import com.example.shop.User;
import com.example.shop.Welcome;
import com.example.shop.Welcomeactivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountFragment extends Fragment  {

    private CountViewModel countViewModel;
    Button Back,Change;
    String name;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        countViewModel =
                ViewModelProviders.of(this).get(CountViewModel.class);
//        Bundle bundle = savedInstanceState;
//        name = bundle.getString("user_id");
//        Log.i("TAGcount",name);

//        Bundle bundle = getArguments();
//        name = bundle.getString("user_id");
//        Log.i("TAGcount",name);


        List<Map<String,Object>> data = getdata();

        String [] keys = {"title"};
        int [] ids = {R.id.title};
        View root = inflater.inflate(R.layout.fragment_count, container, false);
        Back =  root.findViewById(R.id.countback);
        Change = root.findViewById(R.id.countchange);


        //String s = User.getInstance().getuser_id();
        //Log.i("TAGINS",s);
        //String p =User.getInstance().getpassword();
        //Log.i("TAGINS",p);


        ListView lv = root.findViewById(R.id.countlistview);
        SimpleAdapter simpleAdapter = new SimpleAdapter(getContext(),data,R.layout.count_list_item,keys,ids);
        lv.setAdapter(simpleAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder  dlg;
                switch (position)
                 {
                     case 0:

                         dlg = new AlertDialog.Builder(getContext());
                         dlg.setTitle("尊敬的用户");
                         dlg.setCancelable(false);
                         dlg.setMessage("欢迎使用淘宝");
                         dlg.setPositiveButton("确认",null);
                         dlg.setNegativeButton("取消",null);
                         dlg.create();
                         dlg.show();
                         break;
                     case 1:
                         //dlg = new AlertDialog.Builder(getContext());
                         Intent intent = new Intent(getContext(), LocationActivity.class);
                         startActivity(intent);

                         break;

                     case 2:
                         Intent intent1 = new Intent(getContext(),OrderActivity.class);
                         startActivity(intent1);
                         break;

                 }

            }
        });
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(getContext());
                dlg.setTitle("亲爱的的用户");
                dlg.setMessage("确定退出吗？");
                dlg.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dlg.setNegativeButton("取消",null);
                dlg.create();
                dlg.show();
            }
        });

        Change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(getContext());
                dlg.setTitle("亲爱的的用户");
                dlg.setMessage("确定切换账户吗？");
                dlg.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        startActivity(intent);

                    }
                });
                dlg.setNegativeButton("取消",null);
                dlg.create();
                dlg.show();
            }
        });

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(getContext());
                dlg.setTitle("尊敬的用户");
                dlg.setMessage("是否退出？");
                dlg.setNegativeButton("取消",null);
                dlg.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                });
                dlg.create();
                dlg.show();
            }
        });

        return root;
    }

    public List<Map<String,Object>> getdata(){
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("title", User.getInstance().getname());
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("title", "我的收货地址");
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("title", "我的订单");
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("title", "账户与安全");
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("title", "支付设置");
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("title", "地区设置");
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("title", "音效与通知");
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("title", "隐私");
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("title", "问题反馈");
        list.add(map);



        return list;
    }
}
