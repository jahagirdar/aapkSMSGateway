package org.aapk.donationGateway;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FundDatabase  extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 7;
    
    // Database Name
    private static final String DATABASE_NAME = "fundraiser";
 
    // Donation Table Columns names

    private static final String KEY_ID = "_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_DN_PH_NO = "donor_phone_number";
    private static final String KEY_RECEIPT = "receipt";
    private static final String KEY_VO_PH_NO = "volunteer_phone_number";
    
    public static final String ROLE_SFR ="S";
    public static final String ROLE_LSFR ="L";
    public static final String ROLE_ACFR ="A";
    public static final String ROLE_WFR ="W";
    

    // Donation table name
    private static final String TABLE_DON = "Donation";
    private static FundDatabase sInstance;
    public static FundDatabase getInstance(Context context) {

        // Use the application context, which will ensure that you 
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
          sInstance = new FundDatabase(context.getApplicationContext());
        }
        return sInstance;
      }
 
    private FundDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
	@Override
	public void onCreate(SQLiteDatabase db) {
		 
	        String CREATE_FUND_TABLE = "CREATE TABLE " + TABLE_DON + "("
	
	        		 +KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+ KEY_DN_PH_NO + " TEXT ," + KEY_NAME + " TEXT,"
	                + KEY_VO_PH_NO + " TEXT," + KEY_AMOUNT+" TEXT,"+KEY_RECEIPT+" TEXT"+")";
	        db.execSQL(CREATE_FUND_TABLE);
	        
		
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		Log.i("SmsReceiver","Upgrading Database:"+arg1+ " to "+ arg2);
         db.execSQL("DROP TABLE IF EXISTS " + TABLE_DON);
	        onCreate(db);
	}	public void addDonation(String amount, String phone, String receipt,
			String senderNum, String donor_fn, String donor_ln) {
		  SQLiteDatabase db = this.getWritableDatabase();
		  
	        ContentValues values = new ContentValues();
	        
	        values.put(KEY_AMOUNT, amount);
	        values.put(KEY_DN_PH_NO, phone); 
	        values.put(KEY_RECEIPT, receipt); 
	        values.put(KEY_VO_PH_NO, senderNum); 
		    values.put(KEY_NAME, donor_fn+" "+donor_ln); 

	       
		   	 // Inserting Row
		        db.insert(TABLE_DON, null, values);
		         // Closing database 
			
	}
	}
