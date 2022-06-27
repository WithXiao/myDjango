package com.example.navigationview.ui.login;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.example.navigationview.DatabaseHelper;
import com.example.navigationview.R;

public class loginFragment extends Fragment {
    private EditText mAccount;                        //用户名编辑
    private EditText mPwd;                            //密码编辑
    private Button mRegisterButton;                   //注册按钮
    private Button mLoginButton;                      //登录按钮


    private LoginViewModel mViewModel;

    public static loginFragment newInstance() {
        return new loginFragment();
    }
        //public void main();
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.i("test","wqewq");
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        // TODO: Use the ViewModel
        //图片显示为圆形
        ImageView logo = getActivity().findViewById(R.id.logo);
        DrawableTypeRequest<Integer> load = Glide.with(getActivity()).load(R.drawable.logo);
        //通过id找到相应的控件
        mAccount = getActivity().findViewById(R.id.login_edit_account);
        mPwd = getActivity().findViewById(R.id.login_edit_pwd);
        mRegisterButton = getActivity().findViewById(R.id.login_btn_register);
        mLoginButton = getActivity().findViewById(R.id.login_btn_login);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = mAccount.getText().toString().trim();    //获取当前输入的用户名和密码信息
                String userPwd = mPwd.getText().toString().trim();
                //读取数据库
                DatabaseHelper dbOpenHelper = new DatabaseHelper(getActivity());
                SQLiteDatabase db = dbOpenHelper.getWritableDatabase();//打开数据库
                //table->select
                Cursor cursor = db.rawQuery(//Cursor为结果集对象
                        "select username,password from users where username=?", new String[]{userName});
                String rname = null;
                String rpwd = null;
                if (cursor.moveToNext()) {
                    rname = cursor.getString(cursor.getColumnIndex("username"));
                    rpwd = cursor.getString(cursor.getColumnIndex("password"));
                    //判断输入是否正确
                    if (userName.equals(rname) && userPwd.equals(rpwd)) {
                        Toast.makeText(getActivity(), "登录成功！", Toast.LENGTH_LONG).show();
                        TextView u = getActivity().findViewById(R.id.user_name);
                        u.setText(userName);
                        Bundle bundle=new Bundle();
                        bundle.putString("name",userName);
                        NavController navController = Navigation.findNavController(getActivity(),
                                R.id.nav_host_fragment);
                        navController.navigate(R.id.nav_home,bundle);
                    } else {
                        Toast.makeText(getActivity(), "用户名或密码有错误，登录失败!", Toast.LENGTH_LONG).show();
                    }

                }else {
                    Toast.makeText(getActivity(), "用户不存在，登录失败!", Toast.LENGTH_LONG).show();
                }
            }
        });
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(getActivity(),
                        R.id.nav_host_fragment);
                navController.navigate(R.id.registerFragment);
            }
        });


    }
}
