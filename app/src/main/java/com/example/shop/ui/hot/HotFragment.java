package com.example.shop.ui.hot;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.shop.MySQLiteOpenHelper;
import com.example.shop.R;
import com.example.shop.ShopdetailActivity;
import com.example.shop.ui.home.HomeFragment;
import com.to.aboomy.pager2banner.Banner;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class HotFragment extends Fragment {

    RecyclerView mReView;

    List<Map<String, Object>> list;



    private HotViewModel dashboardViewModel;
    ViewPager viewPager;
    ArrayList<Integer> imgs  = new ArrayList<>();
    ArrayList<ImageView> dots  = new ArrayList<>();
    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            handler.sendEmptyMessageDelayed(1,4000);

            viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
            super.handleMessage(msg);

        }
    };




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(HotViewModel.class);
        View root = inflater.inflate(R.layout.fragment_hot, container, false);
        viewPager = root.findViewById(R.id.viewPager);
        imgs.add(R.drawable.img5);
        imgs.add(R.drawable.img1);
        imgs.add(R.drawable.img2);
        imgs.add(R.drawable.img3);
        imgs.add(R.drawable.img4);
        imgs.add(R.drawable.img5);
        imgs.add(R.drawable.img1);
        viewPager.setAdapter(new MyAdapter());
        viewPager.setCurrentItem(2);
        handler.sendEmptyMessage(1);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                if(position ==0){
                    viewPager.setCurrentItem(imgs.size()-2,false);
                    return;
                }
                if(position == (imgs.size() - 1)){
                    viewPager.setCurrentItem(1,false);
                    return;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mReView = root.findViewById(R.id.hotrecycle);
        StaggeredGridLayoutManager LayoutManager =
                new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        LayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        LayoutManager.canScrollVertically();
        mReView.setLayoutManager(LayoutManager);
        MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(getContext());
        SQLiteDatabase db=  mySQLiteOpenHelper.getReadableDatabase();
        String sql = "select * from shopping where salevolume > ? ";
        Cursor cursor = db.rawQuery(sql,new String[]{"100"});
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
        mReView.setAdapter(new Hotadapter(list,listener));



        return root;
    }

    public class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imgs.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//            super.destroyItem(container, position, object);
            container.removeView((View) object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView imageView = new ImageView(getContext());
            imageView.setBackgroundResource(imgs.get(position));
            container.addView(imageView);
            return imageView;


        }
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

    public  class Hotadapter extends RecyclerView.Adapter<Hotadapter.ViewHolder>{
        List<Map<String,Object>> data;
        View.OnClickListener listener;

        public Hotadapter(List<Map<String,Object>> data, View.OnClickListener listener){
            this.data = data;
            this.listener = listener;
        }
        @NonNull


        @Override
        public Hotadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_shop_item,parent,false);
            ViewHolder vh = new ViewHolder(view);
            view.setOnClickListener(listener);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull Hotadapter.ViewHolder holder, int position) {

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
