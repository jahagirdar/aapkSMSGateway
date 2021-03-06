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

public class PledgeDatabase  extends SQLiteOpenHelper{

	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_NAME = "pledgeDB";


	private static final String KEY_ID = "_id";
	private static final String KEY_CANDIDATE = "name";
	private static final String KEY_AMOUNT = "amount";
	private static final String KEY_DN_PH_NO = "donor_phone_number";

	public static final String ROLE_SFR ="S";
	public static final String ROLE_LSFR ="L";
	public static final String ROLE_ACFR ="A";
	public static final String ROLE_WFR ="W";


	// Donation table name
	private static final String TABLE_PLEDGE = "Pledge_table";
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

	public JSONObject getJSONPledge(String id) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = this.getReadableDatabase();
		JSONObject jsonObject = new JSONObject();
		Cursor cursor = db.query(TABLE_PLEDGE,
				new String[] { KEY_ID,KEY_DN_PH_NO,KEY_AMOUNT, KEY_CANDIDATE }, KEY_ID + "=?",
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
				jsonObject.accumulate("candidate", cursor.getString(3));
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

	public long getPledgeCount() {
		SQLiteDatabase db = this.getReadableDatabase();
		return DatabaseUtils.queryNumEntries(db, TABLE_PLEDGE);
			}
}
