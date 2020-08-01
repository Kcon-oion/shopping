package com.example.shop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.shop.ui.shop.ShopFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.example.shop.R.layout.shoppingtrolley_item;

public class OrderActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    OrderAdapter orderAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        recyclerView =findViewById(R.id.orderRecyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(OrderActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        String id = User.getInstance().getuser_id();
        String sql = "select * from shoppingtrolley where use_id = ? and checking = 2";

        MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(OrderActivity.this);
        SQLiteDatabase db=  mySQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,new String[]{id});
        orderAdapter = new OrderAdapter(getdata(cursor));
        recyclerView.setAdapter(orderAdapter);

    }
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
    class OrderAdapter extends  RecyclerView.Adapter<OrderAdapter.ViewHolder>{
        List<Map<String,Object>> data;

        public OrderAdapter(List<Map<String,Object>> data) {
            this.data = data;
        }

        @NonNull
        @Override
        public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.order_item,parent,false);
            ViewHolder vh = new ViewHolder(view);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {
            Map<String,Object> item = data.get(position);
            holder.etdescription.setText(item.get("description").toString());
            holder.etprice.setText("￥"+item.get("price").toString());

            int resID =  getResources().getIdentifier(item.get("photo").toString(), "drawable", "com.example.shop");
            holder.imageView.setImageResource(resID);
            holder.itemView.setTag(item);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView imageView;
            TextView etdescription,etprice;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.photo);
                etdescription = itemView.findViewById(R.id.description);
                etprice = itemView.findViewById(R.id.price);
            }
        }
    }
}

