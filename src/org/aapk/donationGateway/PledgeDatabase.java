package org.aapk.donationGateway;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PledgeDatabase  extends SQLiteOpenHelper{

private static final int DATABASE_VERSION = 0;

private static final String DATABASE_NAME = "pledgeTable";


private static final String KEY_ID = "_id";
private static final String KEY_CANDIDATE = "name";
private static final String KEY_AMOUNT = "amount";
private static final String KEY_DN_PH_NO = "donor_phone_number";

public static final String ROLE_SFR ="S";
public static final String ROLE_LSFR ="L";
public static final String ROLE_ACFR ="A";
public static final String ROLE_WFR ="W";


// Donation table name
private static final String TABLE_PLEDGE = "Donation";
private static PledgeDatabase sInstance;
public static PledgeDatabase getInstance(Context context) {

    // Use the application context, which will ensure that you 
    // don't accidentally leak an Activity's context.
    // See this article for more information: http://bit.ly/6LRzfx
    if (sInstance == null) {
      sInstance = new PledgeDatabase(context.getApplicationContext());
    }
    return sInstance;
  }

private PledgeDatabase(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
}

@Override
public void onCreate(SQLiteDatabase db) {
	 
    String CREATE_PLEDGE_TABLE = "CREATE TABLE " + TABLE_PLEDGE + "("

    		 +KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+ KEY_DN_PH_NO + " TEXT ," + KEY_AMOUNT + " TEXT ,"+ KEY_CANDIDATE + " TEXT"+")";
    
    db.execSQL(CREATE_PLEDGE_TABLE);
    
}

@Override
public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
	Log.i("SmsReceiver","Upgrading Database:"+arg1+ " to "+ arg2);
     db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLEDGE);
        onCreate(db);
}

public void addPledge(String senderNum, String amount, String candidate) {
	
	// TODO Auto-generated method stub
	  SQLiteDatabase db = this.getWritableDatabase();
	  Log.i("SmsReceiver","Debug getDB");
      ContentValues values = new ContentValues();
      values.put(KEY_DN_PH_NO, senderNum); // Contact Name
      values.put(KEY_AMOUNT, amount); // Contact Phone

      values.put(KEY_CANDIDATE, candidate); 
      Log.i("SmsReceiver","Debug SetVal");
 	  db.insert(TABLE_PLEDGE, null, values);
      Log.i("SmsReceiver","Debug insert");
     }	
}
