package org.aapk.donationGateway;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class AppContext extends Application{
	 private static Context context;

	    public void onCreate(){
	        super.onCreate();
	       // AppContext.context = getApplicationContext();
	        Log.d("AapContext",context.toString());
	    }
public static void setContext(Context c){
	if(context==null) context=c;
}
	    public static Context getAppContext() {
	    	Log.d("AapContext",context.toString());
	    	return AppContext.context;
	    }
}
