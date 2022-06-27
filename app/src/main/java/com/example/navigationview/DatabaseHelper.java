package com.example.navigationview;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	final static String DATABASENAME = "my.db";

	public DatabaseHelper(Context context) {
		super(context, DATABASENAME, null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "CREATE TABLE book " + "(" + "isbn  TEXT NOT NULL,"
				+ "bookname  TEXT," + "publishertime  TEXT," + "issell  TEXT," + "picture  BLOB,"
				+ "PRIMARY KEY (isbn)" + ");";

		String users = "CREATE TABLE users" + "(" +
				"username varchar(20) NOT NULL," +
				"password varchar(20) DEFAULT NULL," +
				"phone varchar(30) DEFAULT NULL," +
				"sex varchar(30) DEFAULT NULL," +
				"qianming varchar(30) DEFAULT NULL," +
				"place varchar(50) DEFAULT NULL," + "PRIMARY KEY (username)" + ")";

		String u1 = "INSERT INTO users VALUES('123','123456','45266','男','无','中国')";

		String sql1 = "INSERT INTO book VALUES ('RTX3090TI', 'Intel', '2021-01-27',  '是', NULL);";
		String sql2 = "INSERT INTO book VALUES ('RTX3080', 'Intel', '2020-11-09', '否', NULL);";
		String sql3 = "INSERT INTO book VALUES ('RTX3070', 'Intel', '2020-09-09', '否', NULL);";
		String sql4 = "INSERT INTO book VALUES ('RTX3060', 'Intel', '2020-09-17', '否', NULL);";
		String sql5 = "INSERT INTO book VALUES ('RX68O0XT', 'AMD', '2020-07-05', '否', NULL);";
		String sql6 = "INSERT INTO book VALUES ('RX6500XT', 'AMD', '2020-07-05', '否', NULL);";
		db.execSQL(sql);
		db.execSQL(sql1);
		db.execSQL(sql2);
		db.execSQL(sql3);
		db.execSQL(sql4);
		db.execSQL(sql5);
		db.execSQL(sql6);
		db.execSQL(users);
		db.execSQL(u1);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
