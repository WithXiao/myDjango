package com.example.navigationview.ui.library;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.navigationview.DatabaseHelper;
import com.example.navigationview.R;
import com.linchaolong.android.imagepicker.ImagePicker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Locale;

public class EditBookFragment extends Fragment {
    // 自动完成文本框
    AutoCompleteTextView isbn;
    String[] isbns = { "9787302288664", "9787302288665", "9787302288666" };

    // 退出按钮
    static final int EXIT_DIALOG_ID = 0;
    // 出版日期
    EditText publishtime;
    static final int DATE_DIALOG_ID = 1;
    private int mYear;
    private int mMonth;
    private int mDay;
    //picture
    private ImagePicker imagePicker = new ImagePicker();
    ImageView picture;
    byte[] photobytes;
    //save
    EditText bookname;

    String txtisbn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_editbook, container, false);
        txtisbn=getArguments().getString("isbn");
        // 自动完成文本框
        isbn = (AutoCompleteTextView) view.findViewById(R.id.isbn);
        ArrayAdapter<String> adapterauto = new ArrayAdapter<String>(getActivity(),
                R.layout.list_item, isbns);
        isbn.setAdapter(adapterauto);

        // 退出按钮
        Button exitButton = (Button) view.findViewById(R.id.exit);
        exitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                onCreateDialog(EXIT_DIALOG_ID);

            }
        });
        // 出版日期
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        publishtime = (EditText) view.findViewById(R.id.publishertime);
        publishtime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                onCreateDialog(DATE_DIALOG_ID);
            }
        });

        // picture功能
        picture = (ImageView) view.findViewById(R.id.photo);
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCameraOrGallery();
            }
        });

        //save(isbn,publishtime,picture已有)
        bookname = (EditText) view.findViewById(R.id.bookname);
        //显示数据
        showdata();

        Button save= (Button) view.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtisbn = isbn.getText().toString();
                String txtbookname = bookname.getText().toString();
                String txtpublishertime = publishtime.getText().toString();

                DatabaseHelper dbOpenHelper = new DatabaseHelper(getActivity());
                SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put("bookname", txtbookname);
                cv.put("publishertime", txtpublishertime);
                cv.put("picture", photobytes);
                long id=db.update("book", cv, "isbn=?", new String[] { txtisbn });
                if(id>0)
                {
                    Toast.makeText(getActivity(), "更新数据成功！", Toast.LENGTH_LONG).show();
                }
                db.close();

                NavController navController= Navigation.findNavController(getActivity(),R.id.nav_host_fragment);
                navController.navigate(R.id.nav_home);

            }
        });
        return view;
    }

    private void showdata() {
        DatabaseHelper dbOpenHelper = new DatabaseHelper(getActivity());
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from book where isbn=?",
                new String[] { txtisbn });
        cursor.moveToFirst();
        isbn.setText(cursor.getString(cursor.getColumnIndex("isbn")));
        bookname.setText(cursor.getString(cursor.getColumnIndex("bookname")));
        publishtime.setText(cursor.getString(cursor.getColumnIndex("publishertime")));

        photobytes = cursor.getBlob(cursor.getColumnIndex("picture"));
        if (photobytes != null && photobytes.length > 0) {
           Bitmap image = BitmapFactory.decodeByteArray(photobytes, 0, photobytes.length);
            picture.setImageBitmap(image);
        } else {
            picture.setImageResource(R.mipmap.ic_launcher);
        }
        cursor.close();
        db.close();
    }

    // 对话框创建
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case EXIT_DIALOG_ID:// 退出对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        getActivity());
                builder.setIcon(R.drawable.ic_menu_gallery);
                builder.setTitle("你确定要离开吗？");
                builder.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                // 这里添加点击确定后的逻辑
                                getActivity().finish();
                            }
                        });
                builder.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                // 这里添加点击取消后的逻辑
                                dialog.dismiss();
                            }
                        });
                builder.create().show();
                break;
            case DATE_DIALOG_ID:// 出版日期对话框
                new DatePickerDialog(getActivity(), mDateSetListener, mYear, mMonth,
                        mDay).show();
                break;

        }
        return null;
    }

    // 出版日期
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            updateDisplay();
        }
    };

    // 出版日期 updates the date in the TextView
    private void updateDisplay() {
        publishtime.setText(new StringBuilder()
                // Month is 0 based so add 1
                .append(mYear).append("-").append(mMonth + 1).append("-")
                .append(mDay));
    }
    //picture
    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePicker.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                     @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        imagePicker.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }
    private void startCameraOrGallery() {
        new AlertDialog.Builder(getActivity()).setTitle("设置图片")
                .setItems(new String[] { "从相册中选取图片", "拍照" }, new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        // 回调
                        ImagePicker.Callback callback = new ImagePicker.Callback() {
                            @Override public void onPickImage(Uri imageUri) {
                            }

                            @Override public void onCropImage(Uri imageUri) {
                                //picture.setImageURI(imageUri);
                                Glide.with(getActivity()).load(new File(imageUri.getPath())).into(picture);
                                Glide.with(getActivity()).load(new File(imageUri.getPath())).asBitmap().into(new SimpleTarget<Bitmap>(100, 100) {
                                    @Override
                                    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, stream);
                                        //savedb
                                        photobytes = stream.toByteArray();
                                    }
                                });

                            }
                        };
                        if (which == 0) {
                            // 从相册中选取图片
                            imagePicker.startGallery(EditBookFragment.this, callback);
                        } else {
                            // 拍照
                            imagePicker.startCamera(EditBookFragment.this, callback);
                        }
                    }
                })
                .show()
                .getWindow()
                .setGravity(Gravity.BOTTOM);
    }
}
