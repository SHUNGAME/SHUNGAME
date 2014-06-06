package com.extra.crazyguess;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import com.extra.crazyguess.sqlitehelper.DatabaseHelper;

import android.annotation.SuppressLint;
import android.app.Activity;  
import android.content.ContentValues;  
import android.database.Cursor;  
import android.database.sqlite.SQLiteDatabase;  
import android.os.Bundle;  
import android.view.View;  
import android.view.View.OnClickListener;  
import android.widget.Button;
import android.widget.TextView;

public class SQLiteActivity extends Activity {  
    /** Called when the activity is first created. */  
    private Button createDatabaseButton = null;  
    private Button updateDatabaseButton = null;  
    private Button insertButton = null;  
    private Button updateButton = null;  
    private Button selectButton = null;  
    private Button deleteButton = null;
    private Button addtest = null;
    private TextView texttest = null;
  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.main);
        texttest = (TextView)findViewById(R.id.texttest);
        createDatabaseButton = (Button) findViewById(R.id.createDatabase);  
        updateDatabaseButton = (Button) findViewById(R.id.updateDatabase);  
        insertButton = (Button) findViewById(R.id.insert);  
        updateButton = (Button) findViewById(R.id.update);  
        selectButton = (Button) findViewById(R.id.select);  
        deleteButton = (Button) findViewById(R.id.delete);
        addtest = (Button)findViewById(R.id.addtest);
        addtest.setOnClickListener(new addtestOnClickListener());
        createDatabaseButton  
                .setOnClickListener(new CreateDatabaseOnClickListener());  
        updateDatabaseButton  
                .setOnClickListener(new UpdateDatabaseOnClickListener());  
        insertButton.setOnClickListener(new InsertOnClickListener());  
        updateButton.setOnClickListener(new UpdateOnClickListener());  
        selectButton.setOnClickListener(new SelectOnClickListener());  
        deleteButton.setOnClickListener(new DeleteOnClickListener());  
    }  
  
    // createDatabaseButton点击事件监听器  
    class CreateDatabaseOnClickListener implements OnClickListener {  
        public void onClick(View v) {  
            // 创建了一个DatabaseHelper对象，只执行这句话是不会创建或打开连接的  
            DatabaseHelper dbHelper = new DatabaseHelper(SQLiteActivity.this,  
                    "test_yangyz_db");  
            // 只有调用了DatabaseHelper的getWritableDatabase()方法或者getReadableDatabase()方法之后，才会创建或打开一个连接  
            SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();
            creatDatabase();
        }
    }
  
    // updateDatabaseButton点击事件监听器  
    class UpdateDatabaseOnClickListener implements OnClickListener {  
        public void onClick(View v) {  
            // TODO Auto-generated method stub  
            DatabaseHelper dbHelper = new DatabaseHelper(SQLiteActivity.this,
                    "test_yangyz_db", 2);  
            // 得到一个只读的SQLiteDatabase对象  
            SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();
            updateDatabase();
        }
    }
    
    
    
  
    // insertButton点击事件监听器  
    class InsertOnClickListener implements OnClickListener {  
        public void onClick(View v) {  
            // 创建ContentValues对象  
            ContentValues values = new ContentValues();  
            // 向该对象中插入键值对，其中键是列名，值是希望插入到这一列的值，值必须和数据库当中的数据类型一致  
            values.put("id", 1);  
            values.put("name", "yangyz");  
            // 创建DatabaseHelper对象  
            DatabaseHelper dbHelper = new DatabaseHelper(SQLiteActivity.this,  
                    "test_yangyz_db", 2);  
            // 得到一个可写的SQLiteDatabase对象  
            SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();  
            // 调用insert方法，就可以将数据插入到数据库当中  
            // 第一个参数:表名称  
            // 第二个参数：SQl不允许一个空列，如果ContentValues是空的，那么这一列被明确的指明为NULL值  
            // 第三个参数：ContentValues对象  
            sqliteDatabase.insert("user", null, values);
            insertUser();
        }  
    }
    
    
  
    // updateButton点击事件监听器  
    class UpdateOnClickListener implements OnClickListener {  
        public void onClick(View v) {  
            // 创建一个DatabaseHelper对象  
            DatabaseHelper dbHelper = new DatabaseHelper(SQLiteActivity.this,  
                    "test_yangyz_db", 2);  
            // 得到一个可写的SQLiteDatabase对象  
            SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();  
            // 创建一个ContentValues对象  
            ContentValues values = new ContentValues();  
            values.put("name", "zhangsan");  
            // 调用update方法  
            // 第一个参数String：表名  
            // 第二个参数ContentValues：ContentValues对象  
            // 第三个参数String：where字句，相当于sql语句where后面的语句，？号是占位符  
            // 第四个参数String[]：占位符的值  
            sqliteDatabase.update("user", values, "id=?", new String[] { "1" });  
            System.out.println("-----------update------------");  
        }  
    }
    
    class addtestOnClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Map<String,String> map = queryForUser("1");
			String scores = map.get("scores");
			if("0".equals(scores) || "".equals(scores)){
				scores = "1";
			}else if("1".equals(scores)){
				scores = "2";
			}else if("2".equals(scores)){
				scores = "3";
			}else if("3".equals(scores)){
				scores = "0";
			}
			updateUser(scores, "1","20140605");
			map = queryForUser("1");
			String ssss = map.get("scores");
			texttest.setText(ssss);
		}
    	
    }
    
    
  
    // selectButton点击事件监听器  
    class SelectOnClickListener implements OnClickListener {  
        public void onClick(View v) {  
            String id = null;  
            String name = null;  
            //创建DatabaseHelper对象  
            DatabaseHelper dbHelper = new DatabaseHelper(SQLiteActivity.this,  
                    "test_yangyz_db", 2);  
            // 得到一个只读的SQLiteDatabase对象  
            SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();  
            // 调用SQLiteDatabase对象的query方法进行查询，返回一个Cursor对象：由数据库查询返回的结果集对象  
            // 第一个参数String：表名  
            // 第二个参数String[]:要查询的列名  
            // 第三个参数String：查询条件  
            // 第四个参数String[]：查询条件的参数  
            // 第五个参数String:对查询的结果进行分组  
            // 第六个参数String：对分组的结果进行限制  
            // 第七个参数String：对查询的结果进行排序  
            Cursor cursor = sqliteDatabase.query("user", new String[] { "id",  
                    "name" }, "id=?", new String[] { "1" }, null, null, null);  
            // 将光标移动到下一行，从而判断该结果集是否还有下一条数据，如果有则返回true，没有则返回false  
            while (cursor.moveToNext()) {  
                id = cursor.getString(cursor.getColumnIndex("id"));  
                name = cursor.getString(cursor.getColumnIndex("name"));
//                .e("tabledata","id="+id+"\n"+"name="+name);
            }
            System.out.println("-------------select------------");  
            System.out.println("id: "+id);  
            System.out.println("name: "+name);
            queryForUser("1");
        }  
    }
    
  
    // deleteButton点击事件监听器  
    class DeleteOnClickListener implements OnClickListener {  
        public void onClick(View v) {  
            //创建DatabaseHelper对象  
            DatabaseHelper dbHelper = new DatabaseHelper(SQLiteActivity.this,"test_yangyz_db",2);  
            //获得可写的SQLiteDatabase对象  
            SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();  
            //调用SQLiteDatabase对象的delete方法进行删除操作  
            //第一个参数String：表名  
            //第二个参数String：条件语句  
            //第三个参数String[]：条件值  
            sqliteDatabase.delete("user", "id=?", new String[]{"1"});  
            System.out.println("----------delete----------");  
        }  
    }
    
    
    public void creatDatabase(){
    	DatabaseHelper dbHelper = new DatabaseHelper(SQLiteActivity.this, "userAndQuestion");
    	SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();
    }
    
    public void updateDatabase(){
    	DatabaseHelper dbHelper = new DatabaseHelper(SQLiteActivity.this, "userAndQuestion", 2);  
        // 得到一个只读的SQLiteDatabase对象  
        SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();
    }
    
    public void insertUser(){
    	// 创建ContentValues对象  
        ContentValues values = new ContentValues();
        for(int i = 0; i < 3; i++){
        	values.put("id", i);
        	values.put("userid", "1");
        	values.put("username", "");
        	values.put("datetime", "2014060"+(4+i));
        	values.put("scores", "0");
        	values.put("otherinfo", "");
        	// 创建DatabaseHelper对象  
        	DatabaseHelper dbHelper = new DatabaseHelper(SQLiteActivity.this, "userAndQuestion", 2);  
        	// 得到一个可写的SQLiteDatabase对象  
        	SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();  
        	// 调用insert方法，就可以将数据插入到数据库当中  
        	sqliteDatabase.insert("userinfo", null, values);  
        }
    }
    
    public void updateUser(String score, String userid, String datetime){
    	// 创建一个DatabaseHelper对象  
        DatabaseHelper dbHelper = new DatabaseHelper(SQLiteActivity.this, "userAndQuestion", 2);  
        // 得到一个可写的SQLiteDatabase对象  
        SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();  
        // 创建一个ContentValues对象  
        ContentValues values = new ContentValues();  
        values.put("scores", score);  
        // 调用update方法  
        // 第一个参数String：表名  
        // 第二个参数ContentValues：ContentValues对象  
        // 第三个参数String：where字句，相当于sql语句where后面的语句，？号是占位符  
        // 第四个参数String[]：占位符的值  
        sqliteDatabase.update("userinfo", values, "userid=? and datetime=?", 
        		new String[] {userid, datetime});
    }
    
	@SuppressLint("SimpleDateFormat")
	public Map<String, String> queryForUser(String userid){
		Map<String, String> map = new HashMap<String, String>();
		//获取当前系统时间
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String currentdate = sDateFormat.format(new java.util.Date());
		//创建DatabaseHelper对象  
		DatabaseHelper dbHelper = new DatabaseHelper(SQLiteActivity.this, "userAndQuestion", 2);  
		// 得到一个只读的SQLiteDatabase对象  
		SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor cursor = sqliteDatabase.rawQuery(
				"select * from userinfo where userid=? and datetime=?", 
				new String[] {userid,"20140605"});
		int colums = cursor.getColumnCount();
		while (cursor.moveToNext()) {
			for (int i = 0; i < colums; i++) {
				String columname = cursor.getColumnName(i);
				String columvalue = cursor.getString(cursor
						.getColumnIndex(columname));
				if (columvalue == null) {
					columvalue = "";
				}
				map.put(columname, columvalue);
			}
		}
		return map;
		
	}
	
	public void delete(){
    	//创建DatabaseHelper对象  
        DatabaseHelper dbHelper = new DatabaseHelper(SQLiteActivity.this,"userAndQuestion",2);  
        //获得可写的SQLiteDatabase对象  
        SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();  
        //调用SQLiteDatabase对象的delete方法进行删除操作  
        sqliteDatabase.delete("userinfo", "id=?", new String[]{"1"}); 
    }
}
