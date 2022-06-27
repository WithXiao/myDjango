package com.example.navigationview.ui.login;

import androidx.lifecycle.ViewModelProvider;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.EditText;
import android.widget.Toast;

import com.example.navigationview.DatabaseHelper;
import com.example.navigationview.R;


public class registerFragment extends Fragment {
    private EditText mAccount;                        //用户名编辑
    private EditText mPwd;                            //密码编辑
    private EditText mPwdCheck;                       //密码编辑
    private Button mSureButton;                       //确定按钮
    private Button mCancelButton;                     //取消按钮
    private RegisterViewModel mViewModel;

    public static registerFragment newInstance() {
        return new registerFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.register_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        // TODO: Use the ViewModel
        mAccount = getActivity().findViewById(R.id.resetpwd_edit_name);
        mPwd = getActivity().findViewById(R.id.resetpwd_edit_pwd_new);
        mPwdCheck = getActivity().findViewById(R.id.resetpwd_edit_pwd_new);

        mSureButton = getActivity().findViewById(R.id.register_btn_sure);
        mCancelButton = getActivity().findViewById(R.id.register_btn_cancel);
        mSureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = mAccount.getText().toString().trim();
                String userPwd = mPwd.getText().toString().trim();
                String userPwdCheck = mPwdCheck.getText().toString().trim();
                if (userPwd.equals(userPwdCheck) == false) {     //两次密码输入不一样
                    Toast.makeText(getActivity(), "密码不一致，注册失败！", Toast.LENGTH_SHORT).show();
                } else {
                    NavController navController = Navigation.findNavController(getActivity(),
                            R.id.nav_host_fragment);
                    DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
                    SQLiteDatabase db = databaseHelper.getReadableDatabase();
                    //如果用户已经存在，提示注册失败
                    Cursor cursor = db.rawQuery(//Cursor为结果集对象
                            "select * from users where userName=?", new String[]{userName});
                    if (cursor.moveToNext()) {
                        Toast.makeText(getActivity(), "该用户已经存在，请换一个用户名！", Toast.LENGTH_LONG).show();
                    } else {
                        ContentValues cv = new ContentValues();
                        cv.put("username", userName);
                        cv.put("password", userPwdCheck);
                        long id = db.insert("users", null, cv);
                        if (id > 0) {
                            Toast.makeText(getActivity(), "用户注册成功！", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getActivity(), "注册失败！", Toast.LENGTH_LONG).show();
                        }
                        navController.navigate(R.id.loginFragment);
                    }
                    db.close();
                }
            }
        });
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAccount.setText("");
                mPwd.setText("");
                mPwdCheck.setText("");
            }
        });


    }

}