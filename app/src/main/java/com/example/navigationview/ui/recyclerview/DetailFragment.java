package com.example.navigationview.ui.recyclerview;

import androidx.lifecycle.ViewModelProviders;

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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.navigationview.DatabaseHelper;
import com.example.navigationview.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class DetailFragment extends Fragment {

    private DetailViewModel mViewModel;
    String isbn;

    public static DetailFragment newInstance() {
        return new DetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_detail, container, false);
        isbn=getArguments().getString("isbn");
        DatabaseHelper dbOpenHelper = new DatabaseHelper(getActivity());
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from book where isbn=?",
                new String[] { isbn });
        cursor.moveToFirst();
        String publishertime = cursor.getString(cursor.getColumnIndex("publishertime"));
        String bookname= cursor.getString(cursor.getColumnIndex("bookname"));
        byte[] b = cursor.getBlob(cursor.getColumnIndex("picture"));
        ImageView picture = (ImageView)view.findViewById(R.id.picture);
        if (b != null && b.length > 0) {
            Bitmap image = BitmapFactory.decodeByteArray(b, 0, b.length);
            picture.setImageBitmap(image);
        } else {
            picture.setImageResource(R.mipmap.ic_launcher);
        }

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.toolbar_layout);
        collapsingToolbar.setTitle(bookname);
        TextView tvbookname=(TextView)view.findViewById(R.id.bookname);
        tvbookname.setText(isbn);
        TextView tvpublishertime= view.findViewById(R.id.publishertime);
        tvpublishertime.setText(publishertime);
        Button del_book= (Button) view.findViewById(R.id.del_book);
        del_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper dbOpenHelper = new DatabaseHelper(getActivity());
                SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
                db.execSQL("delete from book where isbn=?", new String[] { isbn });
                db.close();
                Toast.makeText(getActivity(), "删除数据成功！", Toast.LENGTH_LONG).show();
                NavController navController= Navigation.findNavController(getActivity(),R.id.nav_host_fragment);
                navController.navigate(R.id.nav_home);
            }
        });
        Button edit_book= (Button) view.findViewById(R.id.edit_book);
        edit_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController= Navigation.findNavController(getActivity(),R.id.nav_host_fragment);
                Bundle bundle=new Bundle();
                bundle.putString("isbn",isbn);
                navController.navigate(R.id.nav_editbook,bundle);
            }
        });
        return  view;
    }
}
