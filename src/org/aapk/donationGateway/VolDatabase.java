package org.aapk.donationGateway;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class VolDatabase  extends SQLiteOpenHelper{
	private static final int DATABASE_VERSION = 10;

	// Database Name
	private static final String DATABASE_NAME = "volunteersDB";

	// Vol table name
	private static final String TABLE_VOL = "volunteer";

	// Vol Table Columns names
	private static final String KEY_GOVT_ID = "govt_id";
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
				+ KEY_PH_NO + " TEXT,"+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT,"
				+ KEY_GOVT_ID + " TEXT," + KEY_LIMIT+" TEXT,"+KEY_ROLE+" TEXT,"+KEY_CREATOR+" TEXT"+")";
		db.execSQL(CREATE_VOL_TABLE);
		db.execSQL("CREATE INDEX volIDX ON " + TABLE_VOL+ "(" + KEY_PH_NO+ "," +KEY_GOVT_ID+")");
		addVolunteer(db,"Vijay1", "+919980156580", "2000", "SVF231234", ROLE_SFR, null);
		addVolunteer(db,"Nina P Nayak","+919483518349","5000","test","l","+919980156580");
		addVolunteer(db,"Babu Mathew","+919448042333","5000","test","l","+919980156580");
		addVolunteer(db,"V Balakrishnan","+919845346730","5000","test","l","+919980156580");
		addVolunteer(db,"Ravikrishna Reddy","+919686080005","5000","test","l","+919980156580");
		addVolunteer(db,"Shivakumar Giriyappa Malagi","+919449089599","5000","test","l","+919980156580");
		addVolunteer(db,"Kutubuddin B Kazi","+919448009449","5000","test","l","+919980156580");
		addVolunteer(db,"Muttappa C. Angadi","+919448436897","5000","test","l","+919980156580");
		addVolunteer(db,"Chandrakant Kulkarni","+919448349543","5000","test","l","+919980156580");
		addVolunteer(db,"Sridhar Narayankar","+919844478350","5000","test","l","+919980156580");
		addVolunteer(db,"K Arkesh","+919480808000","5000","test","l","+919980156580");
		addVolunteer(db,"Mohan Dasari","+919900120071","5000","test","l","+919980156580");
		addVolunteer(db,"Ashfaq Ahmed I Madaki","+918050495295","5000","test","l","+919980156580");
		addVolunteer(db,"Sampath Kumar","+919343530052","5000","test","l","+919980156580");
		addVolunteer(db,"Basavaraj K J","+919448204754","5000","test","l","+919980156580");
		addVolunteer(db,"Hemant Kumar","+919972231707","5000","test","l","+919980156580");
		addVolunteer(db,"M R Vasudeva","+919448125412","5000","test","l","+919980156580");
		addVolunteer(db,"B T Lalitha Naik","+919900373304","5000","test","l","+919980156580");
		addVolunteer(db,"Santhosh Mohan Gowda","+919448401096","5000","test","l","+919980156580");
		addVolunteer(db,"Hassan Ali Sirquazi","+919480119257","5000","test","l","+919980156580");
		addVolunteer(db,"Kotiganahalli Ramaiah K.","+919945208735","5000","test","l","+919980156580");
		addVolunteer(db,"Shivakumar N. Tontapura","+919980067615","5000","test","l","+919980156580");
		addVolunteer(db,"K.V.Kumar","+919449833779","5000","test","l","+919980156580");
		addVolunteer(db,"Padmamma M.V.","+919731789369","5000","test","l","+919980156580");
		addVolunteer(db," Bhimaraya Raichur","+919731365154","5000","test","l","+919980156580");
		addVolunteer(db,"G. Sreedhar Kallahalla","+919448417029","5000","test","l","+919980156580");
		addVolunteer(db,"A.S.D’Silva","+917353900900","5000","test","l","+919980156580");
		addVolunteer(db,"Gurudev S H","+919620544484","5000","test","l","+919980156580");
		addVolunteer(db,"Raghavendra Thane","+919916502035","5000","test","l","+919980156580");
		addVolunteer(db , "Vijayvithal"          , "+919980156580" , "2000" , "SVF231234"        , "s" , "Manual");
		addVolunteer(db , "rohit"           , "+917829023322" , "2000" , "allpr6656b"       , "a" , "+919980156580");
		addVolunteer(db , "dilip"           , "+919845021182" , "2000" , "ka03-20010003706" , "s" , "+919980156580");
		addVolunteer(db , "aditi"           , "+919902325692" , "5000" , "qert"             , "s" , "Manual");
		addVolunteer(db , "Namami_Ghosh"    , "+919945533886" , "5000" , "test"             , "l" , "+919980156580");
		addVolunteer(db , "manik"           , "+918197996765" , "2000" , "1234"             , "a" , "+919945533886");
		addVolunteer(db , "KundanSingh"     , "+919590822271" , "5000" , "test"             , "l" , "+919980156580");
		addVolunteer(db , "Ravi_Babu"       , "+919986043315" , "5000" , "test"             , "a" , "+919590822271");
		addVolunteer(db , "Seens_kv"        , "+919341222026" , "5000" , "test"             , "a" , "+919590822271");
		addVolunteer(db , "Manjunath_Reddy" , "+918880751357" , "5000" , "test"             , "a" , "+919590822271");
		addVolunteer(db , "Shamnath_K"      , "+919060330512" , "5000" , "test"             , "a" , "+919590822271");
		addVolunteer(db , "Ramesh_Rangappa" , "+918971821422" , "5000" , "test"             , "a" , "+919590822271");
		addVolunteer(db , "Vinod_SV"        , "+917204033325" , "5000" , "test"             , "a" , "+919590822271");
		addVolunteer(db , "Sanchit_Singh"   , "+918050002002" , "5000" , "test"             , "a" , "+919590822271");
		addVolunteer(db , "Shasha_Vali"     , "+918880079683" , "5000" , "test"             , "a" , "+919590822271");
		addVolunteer(db , "Vikash_Shukla"   , "+917204508839" , "5000" , "test"             , "l" , "+919980156580");
		addVolunteer(db , "BasvanthRao"     , "+919481776326" , "5000" , "test"             , "a" , "+917204508839");
		addVolunteer(db , "Arvind_K_B"      , "+919620869322" , "5000" , "test"             , "a" , "+917204508839");
		addVolunteer(db , "Himanshu_Garg"   , "+919742328010" , "5000" , "test"             , "a" , "+917204508839");
		addVolunteer(db , "Mammadi_Sowmya"  , "+919611339159" , "5000" , "test"             , "a" , "+917204508839");
		addVolunteer(db , "Ajay_Gowda"      , "+919916098760" , "5000" , "test"             , "a" , "+917204508839");
		addVolunteer(db , "Alok_Sharma"     , "+917411719172" , "5000" , "test"             , "a" , "+917204508839");
		addVolunteer(db , "Madhusudhan"     , "+919036483630" , "5000" , "test"             , "a" , "+917204508839");
		addVolunteer(db , "V_Raju"          , "+918971466199" , "5000" , "test"             , "a" , "+917204508839");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {	 
		Log.i("SmsReceiver","Upgrading Database:"+arg1+ " to "+ arg2);

		db.execSQL("DROP TABLE IF EXISTS " + TABLE_VOL);
		onCreate(db);
		addVolunteer(db,"Nina P Nayak","+919483518349","5000","test","l","+919980156580");
		addVolunteer(db,"Babu Mathew","+919448042333","5000","test","l","+919980156580");
		addVolunteer(db,"V Balakrishnan","+919845346730","5000","test","l","+919980156580");
		addVolunteer(db,"Ravikrishna Reddy","+919686080005","5000","test","l","+919980156580");
		addVolunteer(db,"Shivakumar Giriyappa Malagi","+919449089599","5000","test","l","+919980156580");
		addVolunteer(db,"Kutubuddin B Kazi","+919448009449","5000","test","l","+919980156580");
		addVolunteer(db,"Muttappa C. Angadi","+919448436897","5000","test","l","+919980156580");
		addVolunteer(db,"Chandrakant Kulkarni","+919448349543","5000","test","l","+919980156580");
		addVolunteer(db,"Sridhar Narayankar","+919844478350","5000","test","l","+919980156580");
		addVolunteer(db,"K Arkesh","+919480808000","5000","test","l","+919980156580");
		addVolunteer(db,"Mohan Dasari","+919900120071","5000","test","l","+919980156580");
		addVolunteer(db,"Ashfaq Ahmed I Madaki","+918050495295","5000","test","l","+919980156580");
		addVolunteer(db,"Sampath Kumar","+919343530052","5000","test","l","+919980156580");
		addVolunteer(db,"Basavaraj K J","+919448204754","5000","test","l","+919980156580");
		addVolunteer(db,"Hemant Kumar","+919972231707","5000","test","l","+919980156580");
		addVolunteer(db,"M R Vasudeva","+919448125412","5000","test","l","+919980156580");
		addVolunteer(db,"B T Lalitha Naik","+919900373304","5000","test","l","+919980156580");
		addVolunteer(db,"Santhosh Mohan Gowda","+919448401096","5000","test","l","+919980156580");
		addVolunteer(db,"Hassan Ali Sirquazi","+919480119257","5000","test","l","+919980156580");
		addVolunteer(db,"Kotiganahalli Ramaiah K.","+919945208735","5000","test","l","+919980156580");
		addVolunteer(db,"Shivakumar N. Tontapura","+919980067615","5000","test","l","+919980156580");
		addVolunteer(db,"K.V.Kumar","+919449833779","5000","test","l","+919980156580");
		addVolunteer(db,"Padmamma M.V.","+919731789369","5000","test","l","+919980156580");
		addVolunteer(db," Bhimaraya Raichur","+919731365154","5000","test","l","+919980156580");
		addVolunteer(db,"G. Sreedhar Kallahalla","+919448417029","5000","test","l","+919980156580");
		addVolunteer(db,"A.S.D’Silva","+917353900900","5000","test","l","+919980156580");
		addVolunteer(db,"Gurudev S H","+919620544484","5000","test","l","+919980156580");
		addVolunteer(db,"Raghavendra Thane","+919916502035","5000","test","l","+919980156580");

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
			String govtid, String role,String parent) {
		// TODO Auto-generated method stub
		Log.i("SmsReceiver","Adding volunteer:"+ name + " Phone number:" + phone +" limit:" +limit +" govtid:"+ govtid + " role:"+ role);
		SQLiteDatabase db = this.getWritableDatabase();
		addVolunteer(db,name, phone, limit, govtid, role, parent);
	}
	private void addVolunteer(SQLiteDatabase db,String name, String phone, String limit,
			String govtid, String role,String parent)  {

		Log.i("SmsReceiver","Debug getDB");
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, name); // Contact Name
		values.put(KEY_PH_NO, phone); // Contact Phone

		values.put(KEY_GOVT_ID, govtid); 
		values.put(KEY_LIMIT, limit); 
		values.put(KEY_ROLE, role); 
		values.put(KEY_CREATOR, parent); 
		Log.i("SmsReceiver","Debug SetVal: name="+name+" phone= "+phone+" id="+govtid+" role="+role+" parent="+parent );

		// Inserting Row
		if(!isVolunteer(db,phone)){
			db.insert(TABLE_VOL, null, values);
			Log.i("SmsReceiver","Debug insert");

		}else{        db.update(TABLE_VOL, values, KEY_PH_NO+" =?", new String[] {phone});
		Log.i("SmsReceiver","Debug update");
		// 
		}
	}
	public boolean isVolunteer(SQLiteDatabase db,String phoneNumber){
		Log.i("SmsReceiver","isVolunteer: Start " +phoneNumber);
		Cursor cursor = db.query(TABLE_VOL,
				new String[] { KEY_ROLE, KEY_NAME }, KEY_PH_NO + "=?",
				new String[] { phoneNumber }, null, null, null, null);
		if(cursor==null){Log.i("SmsReceiver","Null Cursor");}
		if(cursor.getCount()==0){Log.i("SmsReceiver","Zero Cursor Count");}
		if (cursor != null &&cursor.getCount()>0) {
			Log.i("SmsReceiver","isVolunteer: NotNull, count "+cursor.getCount());
			cursor.close();
			return true;

		} else {
			Log.i("SmsReceiver","isVolunteer: No");
			return false;
		}


	}
	public boolean isFundraiser(String phoneNumber) {
		SQLiteDatabase db = this.getReadableDatabase();
		return isFundraiser(db, phoneNumber);
	}
	public boolean isFundraiser(SQLiteDatabase db,String phoneNumber) {

		Log.i("SmsReceiver","isFundraiser: Start " +phoneNumber);
		Cursor cursor = db.query(TABLE_VOL,
				new String[] { KEY_ROLE, KEY_NAME }, KEY_PH_NO + "=?",
				new String[] { phoneNumber }, null, null, null, null);
		if(cursor==null){Log.i("SmsReceiver","Null Cursor");}
		if(cursor.getCount()==0){Log.i("SmsReceiver","Zero Cursor Count");}
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


	public boolean isVolunteer(String govtid) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_VOL, new String[] { KEY_LIMIT,
		}, KEY_GOVT_ID + "=?",
				new String[] { govtid }, null, null, null, null);
		if (cursor != null && cursor.getCount()>0 ){
			cursor.close();
			return true;
		}
		else {
			cursor = db.query(TABLE_VOL, new String[] { KEY_LIMIT,
			}, KEY_PH_NO + "=?",
					new String[] { govtid }, null, null, null, null);
			if (cursor != null && cursor.getCount()>0 ){
				cursor.close();
				return true;
			}
			if (cursor != null) cursor.close();

			return false;
		}

	}
	public String getLimitByID(String govtid) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_VOL, new String[] { KEY_LIMIT
		}, KEY_GOVT_ID + "=?",
				new String[] { govtid }, null, null, null, null);
		if (cursor != null && cursor.getCount()>0){
			cursor.moveToFirst();
			String limit=cursor.getString(0);
			cursor.close();
			return limit;
		}
		else {
			cursor = db.query(TABLE_VOL, new String[] { KEY_LIMIT
			}, KEY_PH_NO + "=?",
					new String[] { govtid }, null, null, null, null);
			if (cursor != null && cursor.getCount()>0){
				cursor.moveToFirst();
				String limit=cursor.getString(0);
				cursor.close();
				return limit;
			}
			return "0";
		}

	}
	public void dump(){
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor cursor=db.rawQuery("SELECT "+KEY_PH_NO+","+ KEY_NAME+","+KEY_GOVT_ID+","+KEY_LIMIT+","+KEY_ROLE+","+KEY_CREATOR+" FROM "+TABLE_VOL,null);// new String[]{KEY_PH_NO, KEY_NAME,KEY_GOVT_ID,KEY_LIMIT,KEY_ROLE,KEY_CREATOR}, null, null, null, null, null, null);
		Log.i("dumpingVol","Row Count "+String.valueOf(cursor.getCount()));
		Log.i("DumpingVol","ColumnCount"+String.valueOf(cursor.getColumnCount()));
	cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			Log.i("DumpingVol",cursor.getString(0)+":"+cursor.getString(1)+":"+cursor.getString(2)+":"+cursor.getString(3)+":"+cursor.getString(4)+":"+cursor.getString(5));
			cursor.moveToNext();
		}
	}
	public JSONObject getJSONVolunteer(String id) {
		SQLiteDatabase db = this.getReadableDatabase();
		JSONObject jsonObject = new JSONObject();
		Cursor cursor = db.query(TABLE_VOL,
				new String[] { KEY_NAME,KEY_PH_NO,KEY_GOVT_ID,KEY_LIMIT, KEY_ROLE,KEY_CREATOR,KEY_ID }, KEY_ID + "=?",
				new String[] { id }, null, null, null, null);
		if(cursor==null){Log.i("SmsReceiver","Null Cursor for id "+id);}
		if(cursor.getCount()==0){Log.i("SmsReceiver","Zero Cursor Count for id "+id);}
		if (cursor != null &&cursor.getCount()>0) {
			Log.i("SmsReceiver","getVolunteer: NotNull, count "+cursor.getCount());
			cursor.moveToFirst();
			try {
				jsonObject.accumulate("name", cursor.getString(0));
				jsonObject.accumulate("phone", cursor.getString(1));
				jsonObject.accumulate("govtid", cursor.getString(2));
				
				jsonObject.accumulate("limit", cursor.getString(3));
				jsonObject.accumulate("role", cursor.getString(4));
				jsonObject.accumulate("parent", cursor.getString(5));
				jsonObject.accumulate("id", cursor.getString(6));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Log.i("SmsReceiver","getJSONVolunteer->"+jsonObject.toString());
			cursor.close();
			return jsonObject;
		}else {
			Log.i("SmsReceiver","getJSONVolunteer Failed");
			cursor.close();
			return null;
		}
	}
	public long getVolunteerCount() {
		// TODO Auto-generated method stub

		SQLiteDatabase db = this.getReadableDatabase();
		return DatabaseUtils.queryNumEntries(db, TABLE_VOL);
	}		

}
