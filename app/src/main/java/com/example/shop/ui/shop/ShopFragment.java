package com.example.shop.ui.shop;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shop.MySQLiteOpenHelper;
import com.example.shop.R;
import com.example.shop.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ShopFragment extends Fragment {

    private ShopViewModel notificationsViewModel;
    Shoppingtrolleyadapter shoppingtrolleyadapter;
    RecyclerView recyclerView;
    Button Balance;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(ShopViewModel.class);
        View root = inflater.inflate(R.layout.fragment_shop, container, false);

        recyclerView = root.findViewById(R.id.shoppingtrolley);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

        String id = User.getInstance().getuser_id();
        String sql = "select * from shoppingtrolley where use_id = ? and checking !=2";

        MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(getContext());
        SQLiteDatabase db=  mySQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,new String[]{id});
        shoppingtrolleyadapter = new Shoppingtrolleyadapter(getdata(cursor),listener);
        recyclerView.setAdapter(shoppingtrolleyadapter);

        Balance = root.findViewById(R.id.settlement);
        Balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView view;
                view = getView().findViewById(R.id.allprice);



                AlertDialog.Builder dlg = new AlertDialog.Builder(getContext());
                dlg.setTitle("尊敬的用户");
                dlg.setMessage("是否支付已选中商品 价格:"+view.getText());
                dlg.setNegativeButton("取消",null);
                dlg.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //结算
                        String sql2 = "update  shoppingtrolley  set checking = 2  where use_id =? and checking = ?";
                        db.execSQL(sql2,new String[]{User.getInstance().getuser_id(),"1"});
//                shoppingtrolleyadapter.notifyDataSetChanged();

                        String sql = "select * from shoppingtrolley where use_id = ? and checking !=2";
                        Cursor cursor = db.rawQuery(sql,new String[]{id});
                        shoppingtrolleyadapter = new Shoppingtrolleyadapter(getdata(cursor),listener);
                        recyclerView.setAdapter(shoppingtrolleyadapter);

                        view.setText("0");

                    }
                });
                dlg.create();
                dlg.show();
            }
        });

        return root;
    }
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Map<String, Object> item = (TreeMap<String,Object>)v.getTag();
            AlertDialog.Builder dlg = new AlertDialog.Builder(getContext());
            dlg.setTitle("亲爱的用户");
            dlg.setMessage("确定要将此商品移除购物车?");
            dlg.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String user_id =  item.get("user_id").toString();
                    String shop_id =  item.get("shop_id").toString();
                    Log.i("TAG",user_id);
                    Log.i("TAG",shop_id);
                    MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(getContext());
                    SQLiteDatabase db=  mySQLiteOpenHelper.getReadableDatabase();
                    String sql ="delete from shoppingtrolley where use_id =? and shop_id = ?";
                    db.execSQL(sql,new String[]{user_id,shop_id});
                    String sql2 = "select * from shoppingtrolley where use_id = ? and checking !=2";
                    Cursor cursor2 = db.rawQuery(sql2,new String[]{User.getInstance().getuser_id()});
                    String id = User.getInstance().getuser_id();
                    Cursor cursor = db.rawQuery(sql,new String[]{id});

//
//                    shoppingtrolleyadapter.notifyDataSetChanged();
                    shoppingtrolleyadapter = new Shoppingtrolleyadapter(getdata(cursor2),listener);
                    recyclerView.setAdapter(shoppingtrolleyadapter);




                }
            });
            dlg.setNegativeButton("取消",null);
            dlg.create();
            dlg.show();

            Log.i("TAG","GETTHIS");
        }
    };

    private List<Map<String, Object>> getdata(Cursor cursor) {
        List<Map<String,Object>> list = new ArrayList<>();
        while(cursor.moveToNext()){
            Map<String,Object> map = new TreeMap<>();

            String shopprice = cursor.getString(cursor.getColumnIndex("price"));
            map.put("price",shopprice);
            //Log.i("TAG",shopprice);
            String shopdescrition = cursor.getString(cursor.getColumnIndex("description"));
            map.put("description",shopdescrition);
            //Log.i("TAG",shopdescrition);
            String shopphoto = cursor.getString(cursor.getColumnIndex("photo"));
            map.put("photo",shopphoto);
            //Log.i("TAG",shopphoto);
            int user_id = cursor.getInt(cursor.getColumnIndex("use_id"));
            map.put("user_id",user_id);
            int shop_id = cursor.getInt(cursor.getColumnIndex("shop_id"));
            Log.i("TAGSHOPID", String.valueOf(shop_id));
            map.put("shop_id",shop_id);

            list.add(map);

        }
        return list;
    }

    public  class Shoppingtrolleyadapter extends RecyclerView.Adapter<Shoppingtrolleyadapter.ViewHolder>{

        MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(getContext());
        SQLiteDatabase db=  mySQLiteOpenHelper.getReadableDatabase();

        List<Map<String,Object>> data;
        View.OnClickListener listener;
        public Shoppingtrolleyadapter(List<Map<String,Object>> data, View.OnClickListener listener) {
                this.data = data;
                this.listener =listener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.shoppingtrolley_item,parent,false);
            ViewHolder vh = new ViewHolder(view);
            view.setOnClickListener(listener);

            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Map<String,Object> item = data.get(position);
            holder.etdescription.setText(item.get("description").toString());
            holder.etprice.setText("￥"+item.get("price").toString());
            int resID = getResources().getIdentifier(item.get("photo").toString(), "drawable", "com.example.shop");
            holder.imageView.setImageResource(resID);
            holder.itemView.setTag(item);
            holder.buy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                   holder.all.setText(item.get("price").toString());
                    TextView view = getView().findViewById(R.id.allprice);
                    if(isChecked){

                        int num=  Integer.parseInt(view.getText().toString());
                        int add =Integer.parseInt(item.get("price").toString());
                        int end = num+add;
                        String price = String.valueOf(end);
                        view.setText(price);
                        String sql = "update shoppingtrolley set checking =? where use_id =? and shop_id =?";
                        db.execSQL(sql,new String[]{"1",  item.get("user_id").toString(),item.get("shop_id").toString()});
                        Log.i("TAG","getthis");
                        Log.i("TAG",item.get("user_id").toString());
                        Log.i("TAG",item.get("shop_id").toString());
                    }
                    else{
                        int num=  Integer.parseInt(view.getText().toString());
                        int add =Integer.parseInt(item.get("price").toString());
                        int end = num-add;
                        String price = String.valueOf(end);
                        view.setText(price);
                        String sql = "update shoppingtrolley set checking =? where use_id =? and shop_id =?";
                        db.execSQL(sql,new String[]{"0", item.get("user_id").toString(),item.get("shop_id").toString()});
                        Log.i("TAG","getthis");
                        Log.i("TAG",item.get("user_id").toString());
                        Log.i("TAG",item.get("shop_id").toString());
                    }


                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public  ImageView imageView;
            TextView etdescription,etprice;
            CheckBox buy;


            public ViewHolder(@NonNull View itemView) {

                super(itemView);
                imageView = itemView.findViewById(R.id.photo);
                etdescription = itemView.findViewById(R.id.description);
                etprice = itemView.findViewById(R.id.price);
                buy = itemView.findViewById(R.id.check);

            }
        }
    }
}
