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
  
    // createDatabaseButton����¼�������  
    class CreateDatabaseOnClickListener implements OnClickListener {  
        public void onClick(View v) {  
            // ������һ��DatabaseHelper����ִֻ����仰�ǲ��ᴴ��������ӵ�  
            DatabaseHelper dbHelper = new DatabaseHelper(SQLiteActivity.this,  
                    "test_yangyz_db");  
            // ֻ�е�����DatabaseHelper��getWritableDatabase()��������getReadableDatabase()����֮�󣬲Żᴴ�����һ������  
            SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();
            creatDatabase();
        }
    }
  
    // updateDatabaseButton����¼�������  
    class UpdateDatabaseOnClickListener implements OnClickListener {  
        public void onClick(View v) {  
            // TODO Auto-generated method stub  
            DatabaseHelper dbHelper = new DatabaseHelper(SQLiteActivity.this,
                    "test_yangyz_db", 2);  
            // �õ�һ��ֻ����SQLiteDatabase����  
            SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();
            updateDatabase();
        }
    }
    
    
    
  
    // insertButton����¼�������  
    class InsertOnClickListener implements OnClickListener {  
        public void onClick(View v) {  
            // ����ContentValues����  
            ContentValues values = new ContentValues();  
            // ��ö����в����ֵ�ԣ����м���������ֵ��ϣ�����뵽��һ�е�ֵ��ֵ��������ݿ⵱�е���������һ��  
            values.put("id", 1);  
            values.put("name", "yangyz");  
            // ����DatabaseHelper����  
            DatabaseHelper dbHelper = new DatabaseHelper(SQLiteActivity.this,  
                    "test_yangyz_db", 2);  
            // �õ�һ����д��SQLiteDatabase����  
            SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();  
            // ����insert�������Ϳ��Խ����ݲ��뵽���ݿ⵱��  
            // ��һ������:������  
            // �ڶ���������SQl������һ�����У����ContentValues�ǿյģ���ô��һ�б���ȷ��ָ��ΪNULLֵ  
            // ������������ContentValues����  
            sqliteDatabase.insert("user", null, values);
            insertUser();
        }  
    }
    
    
  
    // updateButton����¼�������  
    class UpdateOnClickListener implements OnClickListener {  
        public void onClick(View v) {  
            // ����һ��DatabaseHelper����  
            DatabaseHelper dbHelper = new DatabaseHelper(SQLiteActivity.this,  
                    "test_yangyz_db", 2);  
            // �õ�һ����д��SQLiteDatabase����  
            SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();  
            // ����һ��ContentValues����  
            ContentValues values = new ContentValues();  
            values.put("name", "zhangsan");  
            // ����update����  
            // ��һ������String������  
            // �ڶ�������ContentValues��ContentValues����  
            // ����������String��where�־䣬�൱��sql���where�������䣬������ռλ��  
            // ���ĸ�����String[]��ռλ����ֵ  
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
    
    
  
    // selectButton����¼�������  
    class SelectOnClickListener implements OnClickListener {  
        public void onClick(View v) {  
            String id = null;  
            String name = null;  
            //����DatabaseHelper����  
            DatabaseHelper dbHelper = new DatabaseHelper(SQLiteActivity.this,  
                    "test_yangyz_db", 2);  
            // �õ�һ��ֻ����SQLiteDatabase����  
            SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();  
            // ����SQLiteDatabase�����query�������в�ѯ������һ��Cursor���������ݿ��ѯ���صĽ��������  
            // ��һ������String������  
            // �ڶ�������String[]:Ҫ��ѯ������  
            // ����������String����ѯ����  
            // ���ĸ�����String[]����ѯ�����Ĳ���  
            // ���������String:�Բ�ѯ�Ľ�����з���  
            // ����������String���Է���Ľ����������  
            // ���߸�����String���Բ�ѯ�Ľ����������  
            Cursor cursor = sqliteDatabase.query("user", new String[] { "id",  
                    "name" }, "id=?", new String[] { "1" }, null, null, null);  
            // ������ƶ�����һ�У��Ӷ��жϸý�����Ƿ�����һ�����ݣ�������򷵻�true��û���򷵻�false  
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
    
  
    // deleteButton����¼�������  
    class DeleteOnClickListener implements OnClickListener {  
        public void onClick(View v) {  
            //����DatabaseHelper����  
            DatabaseHelper dbHelper = new DatabaseHelper(SQLiteActivity.this,"test_yangyz_db",2);  
            //��ÿ�д��SQLiteDatabase����  
            SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();  
            //����SQLiteDatabase�����delete��������ɾ������  
            //��һ������String������  
            //�ڶ�������String���������  
            //����������String[]������ֵ  
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
        // �õ�һ��ֻ����SQLiteDatabase����  
        SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();
    }
    
    public void insertUser(){
    	// ����ContentValues����  
        ContentValues values = new ContentValues();
        for(int i = 0; i < 3; i++){
        	values.put("id", i);
        	values.put("userid", "1");
        	values.put("username", "");
        	values.put("datetime", "2014060"+(4+i));
        	values.put("scores", "0");
        	values.put("otherinfo", "");
        	// ����DatabaseHelper����  
        	DatabaseHelper dbHelper = new DatabaseHelper(SQLiteActivity.this, "userAndQuestion", 2);  
        	// �õ�һ����д��SQLiteDatabase����  
        	SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();  
        	// ����insert�������Ϳ��Խ����ݲ��뵽���ݿ⵱��  
        	sqliteDatabase.insert("userinfo", null, values);  
        }
    }
    
    public void updateUser(String score, String userid, String datetime){
    	// ����һ��DatabaseHelper����  
        DatabaseHelper dbHelper = new DatabaseHelper(SQLiteActivity.this, "userAndQuestion", 2);  
        // �õ�һ����д��SQLiteDatabase����  
        SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();  
        // ����һ��ContentValues����  
        ContentValues values = new ContentValues();  
        values.put("scores", score);  
        // ����update����  
        // ��һ������String������  
        // �ڶ�������ContentValues��ContentValues����  
        // ����������String��where�־䣬�൱��sql���where�������䣬������ռλ��  
        // ���ĸ�����String[]��ռλ����ֵ  
        sqliteDatabase.update("userinfo", values, "userid=? and datetime=?", 
        		new String[] {userid, datetime});
    }
    
	@SuppressLint("SimpleDateFormat")
	public Map<String, String> queryForUser(String userid){
		Map<String, String> map = new HashMap<String, String>();
		//��ȡ��ǰϵͳʱ��
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String currentdate = sDateFormat.format(new java.util.Date());
		//����DatabaseHelper����  
		DatabaseHelper dbHelper = new DatabaseHelper(SQLiteActivity.this, "userAndQuestion", 2);  
		// �õ�һ��ֻ����SQLiteDatabase����  
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
    	//����DatabaseHelper����  
        DatabaseHelper dbHelper = new DatabaseHelper(SQLiteActivity.this,"userAndQuestion",2);  
        //��ÿ�д��SQLiteDatabase����  
        SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();  
        //����SQLiteDatabase�����delete��������ɾ������  
        sqliteDatabase.delete("userinfo", "id=?", new String[]{"1"}); 
    }
}
