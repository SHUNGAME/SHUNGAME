package com.extra.crazyguess.sqlitehelper;

import android.content.Context;  
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;  
import android.database.sqlite.SQLiteOpenHelper;  
import android.database.sqlite.SQLiteDatabase.CursorFactory;  

/** 
 * SQLiteOpenHelper是一个辅助类，用来管理数据库的创建和版本他，它提供两个方面的功能 
 * 第一，getReadableDatabase()、getWritableDatabase()可以获得SQLiteDatabase对象，通过该对象可以对数据库进行操作 
 * 第二，提供了onCreate()、onUpgrade()两个回调函数，允许我们再创建和升级数据库时，进行自己的操作 
 */  
public class DatabaseHelper extends SQLiteOpenHelper {  
    private static final int VERSION = 1;  
  
    /** 
     * 在SQLiteOpenHelper的子类当中，必须有该构造函数 
     * @param context   上下文对象 
     * @param name      数据库名称 
     * @param factory 
     * @param version   当前数据库的版本，值必须是整数并且是递增的状态 
     */  
    public DatabaseHelper(Context context, String name, CursorFactory factory,  
            int version) {  
        //必须通过super调用父类当中的构造函数  
        super(context, name, factory, version);  
    }  
      
    public DatabaseHelper(Context context, String name, int version){  
        this(context,name,null,version);  
    }  
  
    public DatabaseHelper(Context context, String name){  
        this(context,name,VERSION);  
    }  
  
    //该函数是在第一次创建的时候执行，实际上是第一次得到SQLiteDatabase对象的时候才会调用这个方法  
    @Override  
    public void onCreate(SQLiteDatabase db) {  
    	// TODO Auto-generated method stub  
        System.out.println("create a database");  
        try{
//        	//execSQL用于执行SQL语句  
//        	db.execSQL("create table user(id int,name varchar(20))");
//        	//创建用户信息表
//        	db.execSQL("create table userinfo(id VARCHAR2(20), userid VARCHAR2(20), " +
//        			"username VARCHAR2(40), datetime VARCHAR2(20), scores VARCHAR2(20), " +
//        			"otherinfo VARCHAR2(40))");
//        	//创建答题状态表
//        	db.execSQL("create table questionstate(userid VARCHAR2(20), module VARCHR2(20), " +
//        			"selquenum int, blaquenum int, linequenum int, challengelevel int)");
        	//用户分数、状态综合表
        	db.execSQL("create table userscore(id VARCHAR2(40), userid VARCHAR2(20), " +
        			"username VARCHAR2(200), datetime VARCHAR2(40), scores VARCHAR2(40), " +
        			"selquenum VARCHAR2(40), blaquenum VARCHAR2(40), linequenum VARCHAR2(40), " +
        			"module VARCHAR2(40), challengelevel VARCHAR2(40))");
        }catch(SQLException s){
			s.printStackTrace();
		}
    }  
  
    @Override  
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {  
        // TODO Auto-generated method stub  
        System.out.println("upgrade a database");  
    }  
}
