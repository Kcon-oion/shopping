package com.example.shop.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.shop.MySQLiteOpenHelper;
import com.example.shop.R;
import com.example.shop.ShopdetailActivity;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    RecyclerView mReView;
    ImageView imageView;
    EditText shop;
    List<Map<String, Object>> list;
    RecycleviewAdpater adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        imageView = root.findViewById(R.id.search);
        shop = root.findViewById(R.id.shop);

        mReView = root.findViewById(R.id.homeshoprecycleview);
        StaggeredGridLayoutManager LayoutManager =new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        LayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mReView.setLayoutManager(LayoutManager);

        MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(getContext());
        SQLiteDatabase db=  mySQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = db.query("shopping",null,null,null,null,null,null);
        list = getdata(cursor);


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> item = (TreeMap<String,Object>)v.getTag();
//                String description = item.get("description").toString();
                Intent intent = new Intent(getContext(), ShopdetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("description",item.get("description").toString());
                bundle.putString("salevolume",item.get("salevolume").toString());
                bundle.putString("price",item.get("price").toString());
                bundle.putString("photo",item.get("photo").toString());
                bundle.putString("name",item.get("name").toString());
                bundle.putString("shipaddress",item.get("shipaddress").toString());
                bundle.putString("shop_id",item.get("shop_id").toString());
                intent.putExtras(bundle);
                startActivity(intent);


            }
        };

        adapter = new RecycleviewAdpater(list,listener);
        mReView.setAdapter(adapter);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase  db = mySQLiteOpenHelper.getReadableDatabase();
                String description = "%"+shop.getText().toString()+"%";
                String sql = "select * from shopping where description like ?";
                Cursor cursor1 = db.rawQuery(sql,new String[]{description});
                list = getdata(cursor1);
                StaggeredGridLayoutManager LayoutManager =new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
                LayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
                mReView.setLayoutManager(LayoutManager);
                RecycleviewAdpater adapter = new RecycleviewAdpater(list,listener);
                adapter.notifyDataSetChanged();
                mReView.setAdapter(adapter);

            }
        });

        return root;
    }

    private List<Map<String, Object>> getdata(Cursor cursor) {
        List<Map<String,Object>> list = new ArrayList<>();
        while(cursor.moveToNext()){
            Map<String,Object> map = new TreeMap<>();
            String shopsalevolume = cursor.getString(cursor.getColumnIndex("salevolume"));
            map.put("salevolume",shopsalevolume);
            //Log.i("TAg",shopsalevolume);
            String shopprice = cursor.getString(cursor.getColumnIndex("price"));
            map.put("price",shopprice);
            //Log.i("TAG",shopprice);
            String shopdescrition = cursor.getString(cursor.getColumnIndex("description"));
            map.put("description",shopdescrition);
            //Log.i("TAG",shopdescrition);
            String shopphoto = cursor.getString(cursor.getColumnIndex("photo"));
            map.put("photo",shopphoto);
            //Log.i("TAG",shopphoto);
            String shopname = cursor.getString(cursor.getColumnIndex("name"));
            map.put("name",shopname);
            String shopshipaddress = cursor.getString(cursor.getColumnIndex("shipaddress"));
            map.put("shipaddress",shopshipaddress);
            String shopid = cursor.getString(cursor.getColumnIndex("shop_id"));
            map.put("shop_id",shopid);
            //Log.i("TAGshopid",shopid);

            list.add(map);

        }
        return list;
    }


    public class  RecycleviewAdpater extends RecyclerView.Adapter<RecycleviewAdpater.ViewHolder> {

       // String [] favorite = getResources().getStringArray(R.array.shop_photo);
        List<Map<String,Object>> data;
        View.OnClickListener listener;
        public RecycleviewAdpater(List<Map<String,Object>> data, View.OnClickListener listener) {
            this.data = data;
            this.listener = listener;
        };

        @NonNull
        @Override
        public RecycleviewAdpater.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_shop_item,parent,false);
            ViewHolder vh = new ViewHolder(view);

            view.setOnClickListener(listener);
            return vh;

        }


        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            Map<String,Object> item= data.get(position);
            holder.tvdescription.setText(item.get("description").toString());
            //Log.i("tvdescription",item.get("description").toString());
            holder.tvsalevolume.setText("销量："+item.get("salevolume").toString());
            //Log.i("tvsalevolume",item.get("salevolume").toString());
            holder.tvprice.setText("￥"+item.get("price").toString());
            //Log.i("tvprice",item.get("price").toString());
            int resID = getResources().getIdentifier(item.get("photo").toString(), "drawable", "com.example.shop");

            InputStream is = getResources().openRawResource(resID);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inSampleSize = 5;
            Bitmap btp = BitmapFactory.decodeStream(is, null, options);
            holder.img.setImageBitmap(btp);


            //holder.img.setImageResource(resID);
            holder.itemView.setTag(item);//保存对象，不然点击事件找不到数据


        }

        @Override
        public int getItemCount() {
            return  data.size();
            //return favorite.length;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView img;
            public TextView tvdescription;
            public TextView tvprice;
            public TextView tvsalevolume;

            public ViewHolder(@NonNull View itemView) {

                super(itemView);
                img = itemView.findViewById(R.id.itemview);
                tvdescription = itemView.findViewById(R.id.description);
                tvprice = itemView.findViewById(R.id.price);
                tvsalevolume = itemView.findViewById(R.id.salevolume);
            }
        }
    }
}
