package com.example.navigationview.ui.user;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import com.example.navigationview.DatabaseHelper;
import com.example.navigationview.R;

public class userFragment extends Fragment {

    private UserViewModel mViewModel;

    public static userFragment newInstance() {
        return new userFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        // TODO: Use the ViewModel
        TextView user_name = getActivity().findViewById(R.id.user_name);
        final String name=user_name.getText().toString();

        if (user_name.getText().equals("")){
            new  AlertDialog.Builder(getActivity()).setTitle("消息提醒" ).setMessage("您还未登录，请先登录" )
                    .setPositiveButton("确定" ,  null )
                    .show();
        }else {
             final EditText people_name=getActivity().findViewById(R.id.people_name);
             final EditText  people_phone=getActivity().findViewById(R.id.people_phone);
             final EditText  people_sex=getActivity().findViewById(R.id.people_sex);
             final EditText  people_sign=getActivity().findViewById(R.id.people_sign);
             Button people_save=getActivity().findViewById(R.id.people_save);

            //读取该用户的信息，并显示
            DatabaseHelper dbOpenHelper = new DatabaseHelper(getActivity());
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();//打开数据库
            Cursor cursor = db.rawQuery(//Cursor为结果集对象
                    "select * from users where username=?", new String[]{name});
            if (cursor.moveToNext()) {
                people_name.setText(cursor.getString(cursor.getColumnIndex("username")));
                people_sex.setText(cursor.getString(cursor.getColumnIndex("sex")));
                people_phone.setText(cursor.getString(cursor.getColumnIndex("phone")));
                people_sign.setText(cursor.getString(cursor.getColumnIndex("qianming")));
                db.close();
            }
            //点击保存更改后，将数据更新到数据库
            people_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseHelper dbOpenHelper = new DatabaseHelper(getActivity());
                    SQLiteDatabase db = dbOpenHelper.getWritableDatabase();//打开数据库
                    ContentValues cv = new ContentValues();
                    String p=people_phone.getText().toString();
                    String sign=people_sign.getText().toString();
                    String sex=people_sex.getText().toString();
                    cv.put("phone", p);
                    cv.put("sex",sex);
                    cv.put("qianming", sign);
                    long id=db.update("users", cv, "username=?", new String[] {name});
                    if(id>0)
                    {
                        Toast.makeText(getActivity(), "信息完善成功！", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getActivity(), "信息完善成功！", Toast.LENGTH_LONG).show();
                    }
                    db.close();
                }
            });

        }
    }

}