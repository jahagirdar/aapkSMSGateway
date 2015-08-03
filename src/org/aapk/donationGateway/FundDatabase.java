package org.aapk.donationGateway;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
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
	public JSONObject getJSONDonation(String id){
		SQLiteDatabase db = this.getReadableDatabase();
		JSONObject jsonObject = new JSONObject();

		Cursor cursor = db.query(TABLE_DON,
				new String[] { KEY_AMOUNT,KEY_DN_PH_NO,KEY_RECEIPT,KEY_VO_PH_NO, KEY_NAME,KEY_ID }, KEY_ID + "=?",
				new String[] { id }, null, null, null, null);
		if(cursor==null){Log.i("SmsReceiver","Null Cursor");}
		if(cursor.getCount()==0){Log.i("SmsReceiver","Zero Cursor Count");}
		if (cursor != null &&cursor.getCount()>0) {
			Log.i("SmsReceiver","getDonation: NotNull, count "+cursor.getCount());
			cursor.moveToFirst();
			try {
				jsonObject.accumulate("amount", cursor.getString(0));
				jsonObject.accumulate("dn_phone", cursor.getString(1));
				jsonObject.accumulate("receipt", cursor.getString(2));
				jsonObject.accumulate("vol_phone", cursor.getString(3));
				jsonObject.accumulate("name", cursor.getString(4));
				jsonObject.accumulate("id", cursor.getString(5));	
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.d("getJSONDonation",jsonObject.toString());
				e.printStackTrace();
			}

			Log.i("SmsReceiver","getJSONDonation->"+jsonObject.toString());
			cursor.close();
			return jsonObject;
		}else {
			Log.i("SmsReceiver","getJSONDonation Failed");
			cursor.close();
			return null;
		}
	}

	public long getDonationCount() {
		SQLiteDatabase db = this.getReadableDatabase();
		return DatabaseUtils.queryNumEntries(db, TABLE_DON);
			}
}
