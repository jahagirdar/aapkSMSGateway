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

public class AccountDatabase extends SQLiteOpenHelper{
	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_NAME = "accountDB.sqlite";


	private static final String KEY_ID = "_id";
	private static final String KEY_TXN_ID = "TXN_ID";
	private static final String KEY_AMOUNT = "amount";
	private static final String KEY_DEPOSITER_PHONE = "depositer_phone_number";
	private static final String TABLE_ACCOUNT = "Account_table";


	private static AccountDatabase sInstance;
	public static AccountDatabase getInstance(Context context) {

		// Use the application context, which will ensure that you 
		// don't accidentally leak an Activity's context.
		// See this article for more information: http://bit.ly/6LRzfx
		if (sInstance == null) {
			sInstance = new AccountDatabase(context.getApplicationContext());
		}
		return sInstance;
	}

	private AccountDatabase (Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String CREATE_PLEDGE_TABLE = "CREATE TABLE " + TABLE_ACCOUNT + "("

    		 +KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+ KEY_DEPOSITER_PHONE + " TEXT ," + KEY_AMOUNT + " TEXT ,"+ KEY_TXN_ID + " TEXT"+")";

		db.execSQL(CREATE_PLEDGE_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		Log.i("SmsReceiver","Upgrading Database:"+arg1+ " to "+ arg2);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT);
		onCreate(db);
	}


	public void addDeposit(String senderNum, String amount, String txnID) {
		SQLiteDatabase db = this.getWritableDatabase();
		Log.i("SmsReceiver","Account Debug getDB");
		ContentValues values = new ContentValues();
		values.put(KEY_DEPOSITER_PHONE, senderNum); // Contact Name
		values.put(KEY_AMOUNT, amount); // Contact Phone

		values.put(KEY_TXN_ID, txnID); 
		Log.i("SmsReceiver","Account Debug SetVal");
		db.insert(TABLE_ACCOUNT, null, values);
		Log.i("SmsReceiver","Account Debug insert");	
	}
	public JSONObject getJSONAccount(String id) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = this.getReadableDatabase();
		JSONObject jsonObject = new JSONObject();
		Cursor cursor = db.query(TABLE_ACCOUNT,
				new String[] { KEY_ID,KEY_DEPOSITER_PHONE,KEY_AMOUNT, KEY_TXN_ID }, KEY_ID + "=?",
				new String[] { id }, null, null, null, null);
		if(cursor==null){Log.i("SmsReceiver","Null Cursor");}
		if(cursor.getCount()==0){Log.i("SmsReceiver","Zero Cursor Count");}
		if (cursor != null &&cursor.getCount()>0) {
			Log.i("SmsReceiver","getVolunteer: NotNull, count "+cursor.getCount());
			cursor.moveToFirst();
			try {
				jsonObject.accumulate("id", cursor.getString(0));
				jsonObject.accumulate("phone", cursor.getString(1));
				jsonObject.accumulate("amount", cursor.getString(2));
				jsonObject.accumulate("txnid", cursor.getString(3));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Log.i("SmsReceiver","getJSONPledge->"+jsonObject.toString());
			cursor.close();
			return jsonObject;
		}else {
			Log.i("SmsReceiver","getJSONPledge Failed");
			cursor.close();
			return null;
		}

	}

	public long getAccountCount() {
		SQLiteDatabase db = this.getReadableDatabase();
		return DatabaseUtils.queryNumEntries(db, TABLE_ACCOUNT);
			}
	}
