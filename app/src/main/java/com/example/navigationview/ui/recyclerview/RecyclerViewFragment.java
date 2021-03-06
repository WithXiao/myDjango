package com.example.navigationview.ui.recyclerview;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.navigationview.DatabaseHelper;
import com.example.navigationview.R;
import com.example.navigationview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecyclerViewFragment extends Fragment {

    private RecyclerViewViewModel mViewModel;
    private List<HashMap<String, Object>> mData=new ArrayList<HashMap<String, Object>>();;
    private SwipeRecyclerView recyclerView;
    private MyRecycleViewAdapter adapter;
    private int pageSize = 5;
    Cursor cursor;
    public static RecyclerViewFragment newInstance() {
        return new RecyclerViewFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        mData =getdata(0,pageSize);
        recyclerView = (SwipeRecyclerView) view.findViewById(R.id.swipeRecyclerView);
        //set color
        recyclerView.getSwipeRefreshLayout()
                .setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

        recyclerView.getRecyclerView().setLayoutManager(new GridLayoutManager(getActivity(), 1));
        //recyclerView.getRecyclerView().setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        adapter=new MyRecycleViewAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setOnLoadListener(new SwipeRecyclerView.OnLoadListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mData.clear();
                        mData =getdata(0,pageSize);
                        recyclerView.complete();
                        adapter.notifyDataSetChanged();

                    }
                }, 1000);

            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mData =getdata(mData.size(),pageSize);
                        if(cursor.getCount() < pageSize){
                            recyclerView.onNoMore("?????????????????????");
                        }else {
                            recyclerView.stopLoadingMore();
                            adapter.notifyDataSetChanged();
                        }

                    }
                }, 1000);
            }
        });

        //???????????????????????????????????????recyclerView.setOnLoadListener()????????????
        //????????????????????????????????????????????????setRefreshing(true),????????????OnLoadListener
        recyclerView.setRefreshing(true);
        return view;
        //????????????
        //??????????????????
        // recyclerView.setRefreshEnable(false);
        //??????????????????
        //recyclerView.setLoadMoreEnable(false);
        //??????emptyView
        /*TextView textView = new TextView(this);
        textView.setText("empty view");
        recyclerView.setEmptyView(textView);*/
        //??????footerView
        //recyclerView.setFooterView(new SimpleFooterView(this));
        //??????SwipeRecyclerView??????GridLayoutManager???SpanSizeLookup????????????????????????????????????
        //GridLayoutManager????????????SpanSizeLookup?????????????????????????????????
        /*recyclerView.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 3;
            }
        });*/
        //????????????footerView ????????????
       /* recyclerView.getRecyclerView().addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setColor(0xFFEECCCC);
                Rect rect = new Rect();
                int left = parent.getPaddingLeft();
                int right = parent.getWidth() - parent.getPaddingRight();
                final int childCount = parent.getChildCount() - 1;
                for (int i = 0; i < childCount; i++) {
                    final View child = parent.getChildAt(i);

                    //??????child???????????????
                    final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                    final int top = child.getBottom() + params.bottomMargin;
                    final int itemDividerHeight = 1;//px
                    rect.set(left + 50, top, right - 50, top + itemDividerHeight);
                    c.drawRect(rect, paint);
                }
            }
        });*/
        //??????noMore
        // recyclerView.onNoMore("-- end --");
        //??????????????????
        //recyclerView.onNetChange(true);
        //??????????????????
        //recyclerView.onError("error");


    }
    private List<HashMap<String, Object>> getdata(int offset, int pageSize)
    {
        byte[] b = null;
        Bitmap image = null;
        DatabaseHelper dbOpenHelper = new DatabaseHelper(getActivity());
        SQLiteDatabase db=dbOpenHelper.getWritableDatabase();
        cursor=db.rawQuery("select * from book limit ?,?",new String[]{String.valueOf(offset),String.valueOf(pageSize)});

        while (cursor.moveToNext()) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("isbn", cursor.getString(cursor.getColumnIndex("isbn")));
            map.put("publishertime", cursor.getString(cursor.getColumnIndex("publishertime")));
            map.put("bookname",cursor.getString(cursor.getColumnIndex("bookname")));

            b = cursor.getBlob(cursor.getColumnIndex("picture"));
            if (b != null && b.length > 0) {
                image = BitmapFactory.decodeByteArray(b, 0, b.length);
                map.put("picture", image);
            } else {
                map.put("picture", R.drawable.a1);
            }
            mData.add(map);
        }

        cursor.close();
        return mData;
    }
    class  MyRecycleViewAdapter extends RecyclerView.Adapter<MyRecycleViewAdapter.ViewHolder>
    {


        public  class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView picture;
            public TextView isbn;
            public TextView bookname;
            public TextView publishertime;

            public ViewHolder(View convertView) {
                super(convertView);
                picture = (ImageView)convertView.findViewById(R.id.picture);
                isbn = (TextView)convertView.findViewById(R.id.isbn);
                bookname = (TextView)convertView.findViewById(R.id.bookname);
                publishertime = (TextView)convertView.findViewById(R.id.publishertime);
            }
        }
        @NonNull
        @Override
        public MyRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(getActivity()).inflate(R.layout.item,parent, false);

            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull MyRecycleViewAdapter.ViewHolder holder, final int position) {

            if(mData.get(position).get("picture") instanceof Bitmap)
                holder.picture.setImageBitmap((Bitmap) mData.get(position).get("picture"));
            else
                holder.picture.setBackgroundResource((Integer)mData.get(position).get("picture"));
            holder.isbn.setText((String)mData.get(position).get("isbn"));
            holder.bookname.setText((String)mData.get(position).get("bookname"));
            holder.publishertime.setText((String)mData.get(position).get("publishertime"));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavController navController= Navigation.findNavController(getActivity(),R.id.nav_host_fragment);
                    Bundle bundle=new Bundle();
                    bundle.putString("isbn",(String)mData.get(position).get("isbn"));
                    navController.navigate(R.id.detailFragment,bundle);

                }
            });
        }

        @Override
        public int getItemCount() {
            return mData == null ? 0 : mData.size();
        }
    }
}
