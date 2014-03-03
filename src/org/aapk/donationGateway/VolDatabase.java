package org.aapk.donationGateway;

import android.app.Notification;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

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
	    
	    public static final String ROLE_SFR ="s";
	    public static final String ROLE_LSFR ="l";
	    public static final String ROLE_ACFR ="a";
	    public static final String ROLE_WFR ="w";
	    private static VolDatabase sInstance;
	    public static VolDatabase getInstance(Context context) {

	        // Use the application context, which will ensure that you 
	        // don't accidentally leak an Activity's context.
	        // See this article for more information: http://bit.ly/6LRzfx
	        if (sInstance == null) {
	          sInstance = new VolDatabase(context.getApplicationContext());
	        }
	        return sInstance;
	      }
	    private VolDatabase(Context context) {
	        super(context, DATABASE_NAME, null, DATABASE_VERSION);
	    }
		@Override
		public void onCreate(SQLiteDatabase db) {
			 String CREATE_VOL_TABLE = "CREATE TABLE " + TABLE_VOL + "("
		                + KEY_PH_NO + " TEXT PRIMARY KEY," + KEY_NAME + " TEXT,"
		                + KEY_ID + " TEXT," + KEY_LIMIT+" TEXT,"+KEY_ROLE+" TEXT,"+KEY_CREATOR+" TEXT"+")";
		        db.execSQL(CREATE_VOL_TABLE);
		        db.execSQL("CREATE INDEX volIDX ON " + TABLE_VOL+ "(" + KEY_PH_NO+ "," +KEY_ID+")");
		        addVolunteer(db,"Vijay", "9980156580", "2000", "SVF231234", ROLE_SFR, null);
		        addVolunteer(db,"Vijay1", "+919980156580", "2000", "SVF231234", ROLE_SFR, null);
		        addVolunteer(db,"Vijay2", "919980156580", "2000", "SVF231234", ROLE_SFR, null);
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {	 
			Log.i("SmsReceiver","Upgrading Database:"+arg1+ " to "+ arg2);
	        
			 db.execSQL("DROP TABLE IF EXISTS " + TABLE_VOL);
		        onCreate(db);
		}

		public boolean isLeaderOf(String phoneNumber,String deputyrole) {
			SQLiteDatabase db = this.getReadableDatabase();

			Cursor cursor = db.query(TABLE_VOL, new String[] { KEY_ROLE,
					KEY_NAME  }, KEY_PH_NO + "=?",
					new String[] { phoneNumber }, null, null, null, null);
			if (cursor != null && cursor.getCount()>0){
				cursor.moveToFirst();
				String role=cursor.getString(0);
				if(role.equals(ROLE_SFR)
						||(role.equals(ROLE_LSFR)&& (deputyrole.equals(ROLE_ACFR)||deputyrole.equals(ROLE_WFR)))
						||(role.equals(ROLE_ACFR) && deputyrole.equals(ROLE_WFR))){
					 
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
				String id, String role,String parent)  {
				
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
		        if(!isFundraiser(db,phone)){
					    db.insert(TABLE_VOL, null, values);
		        Log.i("SmsReceiver","Debug insert");
		        
			}else{        db.update(TABLE_VOL, values, KEY_PH_NO+" =?", new String[] {phone});
			Log.i("SmsReceiver","Debug update");
	         // 

			}
		}
		public boolean isFundraiser(String phoneNumber) {
			SQLiteDatabase db = this.getReadableDatabase();
			return isFundraiser(db, phoneNumber);
		}
		public boolean isFundraiser(SQLiteDatabase db,String phoneNumber) {
			
			Log.i("SmsReceiver","isFundraiser: Start");
			Cursor cursor = db.query(TABLE_VOL,
					new String[] { KEY_ROLE, KEY_NAME }, KEY_PH_NO + "=?",
					new String[] { phoneNumber }, null, null, null, null);
			
			if (cursor != null &&cursor.getCount()>0) {
				Log.i("SmsReceiver","isFundraiser: NotNull, count "+cursor.getCount());
				cursor.moveToFirst();
				Log.i("SmsReceiver","isFundraiser: at First Row");
				String role = cursor.getString(0);
				if (role.equals(ROLE_LSFR) || 
						role.equals(ROLE_ACFR)||
						role.equals(ROLE_SFR) 
						|| role.equals(ROLE_WFR)) {
					Log.i("SmsReceiver","isFundraiser: Role Match");
					cursor.close();
					return true;
				}else {
					Log.i("SmsReceiver","isFundraiser: Role No Match:"+role+":");
					cursor.close();
					return false;
				}
			} else {
				Log.i("SmsReceiver","isFundraiser: No");
				return false;
			}

		}
		
		
		public boolean isVolunteer(String id) {
			 SQLiteDatabase db = this.getReadableDatabase();
			 
		        Cursor cursor = db.query(TABLE_VOL, new String[] { KEY_LIMIT,
		                }, KEY_ID + "=?",
		                new String[] { id }, null, null, null, null);
		        if (cursor != null && cursor.getCount()>0 ){
		        	  cursor.close();
		            return true;
		        }
		        else {
		        	cursor = db.query(TABLE_VOL, new String[] { KEY_LIMIT,
                }, KEY_PH_NO + "=?",
                new String[] { id }, null, null, null, null);
        if (cursor != null && cursor.getCount()>0 ){
        	  cursor.close();
            return true;
        }
        if (cursor != null) cursor.close();
		        	  
		        	return false;
		        }
		    
		}
		public String getLimitByID(String id) {
			 SQLiteDatabase db = this.getReadableDatabase();
			 
		        Cursor cursor = db.query(TABLE_VOL, new String[] { KEY_LIMIT
		                }, KEY_ID + "=?",
		                new String[] { id }, null, null, null, null);
		        if (cursor != null && cursor.getCount()>0){
		        	cursor.moveToFirst();
		       	 String limit=cursor.getString(0);
		       	  cursor.close();
		       	 return limit;
		        }
		           else {
		        	   cursor = db.query(TABLE_VOL, new String[] { KEY_LIMIT
		                }, KEY_PH_NO + "=?",
		                new String[] { id }, null, null, null, null);
		        if (cursor != null && cursor.getCount()>0){
		        	cursor.moveToFirst();
		       	 String limit=cursor.getString(0);
		       	  cursor.close();
		       	 return limit;
		        }
		        	return "0";
		           }
		    
		}

		}
