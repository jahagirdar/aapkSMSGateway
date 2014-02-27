package com.example.aapksmsgateway;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class VolDatabase  extends SQLiteOpenHelper{
	 private static final int DATABASE_VERSION = 2;
	    
	    // Database Name
	    private static final String DATABASE_NAME = "fundraisers";
	 
	    // Vol table name
	    private static final String TABLE_VOL = "volunteer";
	 
	    // Vol Table Columns names
	    private static final String KEY_ID = "id";
	    private static final String KEY_NAME = "name";
	    private static final String KEY_PH_NO = "phone_number";
	    private static final String KEY_LIMIT = "amount";
	    private static final String KEY_ROLE = "role";
	    private static final String KEY_CREATOR = "creator";
	    
	    public static final String ROLE_SFR ="S";
	    public static final String ROLE_LSFR ="L";
	    public static final String ROLE_ACFR ="A";
	    public static final String ROLE_WFR ="W";
	    public VolDatabase(Context context) {
	        super(context, DATABASE_NAME, null, DATABASE_VERSION);
	    }
		@Override
		public void onCreate(SQLiteDatabase db) {
			 String CREATE_VOL_TABLE = "CREATE TABLE " + TABLE_VOL + "("
		                + KEY_PH_NO + " TEXT PRIMARY KEY," + KEY_NAME + " TEXT,"
		                + KEY_ID + " TEXT," + KEY_LIMIT+" TEXT,"+KEY_ROLE+" TEXT,"+KEY_CREATOR+" TEXT"+")";
		        db.execSQL(CREATE_VOL_TABLE);
		        db.execSQL("CREATE INDEX volIDX ON " + TABLE_VOL+ "(" + KEY_PH_NO+ "," +KEY_ID+")");
		        addVolunteer(db,"Vijay", "9980000000", "2000", "SVF231234", ROLE_SFR, null);
		        
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {	 
			Log.i("SmsReceiver","Upgrading Database:"+arg1+ " to "+ arg2);
	        
			 db.execSQL("DROP TABLE IF EXISTS " + TABLE_VOL);
		        onCreate(db);
		}

		public boolean isLeader(String phoneNumber) {
			SQLiteDatabase db = this.getReadableDatabase();

			Cursor cursor = db.query(TABLE_VOL, new String[] { KEY_ROLE,
					KEY_NAME  }, KEY_PH_NO + "=?",
					new String[] { phoneNumber }, null, null, null, null);
			if (cursor != null ){
				cursor.moveToFirst();
				String role=cursor.getString(0);
				if(role.equals(ROLE_SFR)||role.equals(ROLE_LSFR)||role.equals(ROLE_ACFR)){
					 
					cursor.close();
					return true;
				} else {
					 
					cursor.close();
					return false;
				}
			}
			 
			return false;
		}
		       
		public void addVolunteer(String name, String phone, String limit,
				String id, String role,String parent) {
			// TODO Auto-generated method stub
			 Log.i("SmsReceiver","Adding volunteer:"+ name + " Phone number:" + phone +" limit:" +limit +" id:"+ id + " role:"+ role);
			  SQLiteDatabase db = this.getWritableDatabase();
			  addVolunteer(db,name, phone, limit, id, role, parent);
		}
		private void addVolunteer(SQLiteDatabase db,String name, String phone, String limit,
				String id, String role,String parent) {
			
			  Log.i("SmsReceiver","Debug getDB");
		        ContentValues values = new ContentValues();
		        values.put(KEY_NAME, name); // Contact Name
		        values.put(KEY_PH_NO, phone); // Contact Phone
		 
		        values.put(KEY_ID, id); 
		        values.put(KEY_LIMIT, limit); 
		        values.put(KEY_ROLE, role); 
		        values.put(KEY_CREATOR, parent); 
		        Log.i("SmsReceiver","Debug SetVal");
		   	 
		   	 // Inserting Row
		        db.insert(TABLE_VOL, null, values);
		        Log.i("SmsReceiver","Debug insert");
		         // Closing database c
		        Log.i("SmsReceiver","Debug close");
		
			
		}
		public boolean isFundraiser(String phoneNumber) {
			SQLiteDatabase db = this.getReadableDatabase();
			 
	        Cursor cursor = db.query(TABLE_VOL, new String[] { KEY_ROLE,
	                KEY_NAME  }, KEY_PH_NO + "=?",
	                new String[] { phoneNumber }, null, null, null, null);
	        if (cursor != null )
	            cursor.moveToFirst();
	 String role=cursor.getString(0);
	 if(role.equals(ROLE_LSFR)||role.equals(ROLE_ACFR)||role.equals(ROLE_SFR)||role.equals(ROLE_WFR)){
		  cursor.close();
	 return true;
	 } else { cursor.close(); return false;}
	       
			
		}
		public String getLimit(String senderNum) {
			 SQLiteDatabase db = this.getReadableDatabase();
			 
		        Cursor cursor = db.query(TABLE_VOL, new String[] { KEY_LIMIT,
		                }, KEY_PH_NO + "=?",
		                new String[] { senderNum }, null, null, null, null);
		        if (cursor != null ){
		            cursor.moveToFirst();
		 String limit=cursor.getString(0);
		  cursor.close();
		 return limit;
		        } else {
		        	  
		        	 return "0";
		        }
		       
			
		}
		public boolean isVolunteer(String id) {
			 SQLiteDatabase db = this.getReadableDatabase();
			 
		        Cursor cursor = db.query(TABLE_VOL, new String[] { KEY_LIMIT,
		                }, KEY_ID + "=?",
		                new String[] { id }, null, null, null, null);
		        if (cursor != null ){
		        	  cursor.close();
		            return true;
		        }
		        else {
		        	  
		        	return false;
		        }
		    
		}
		public String getLimitByID(String id) {
			 SQLiteDatabase db = this.getReadableDatabase();
			 
		        Cursor cursor = db.query(TABLE_VOL, new String[] { KEY_LIMIT
		                }, KEY_ID + "=?",
		                new String[] { id }, null, null, null, null);
		        if (cursor != null ){
		        	cursor.moveToFirst();
		       	 String limit=cursor.getString(0);
		       	  cursor.close();
		       	 return limit;
		        }
		           else {
		        	    
		        	return "0";
		           }
		    
		}

		}
