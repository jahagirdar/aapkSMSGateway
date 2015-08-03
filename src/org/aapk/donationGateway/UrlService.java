package org.aapk.donationGateway;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class UrlService {
	public boolean upload_volunteers(Context context){
		if(isNetworkConnected(context)){
			new AsyncUploadDonation().execute("Donation");
		}
		return false;

	}
	public boolean upload_donations(Context context){
		if(isNetworkConnected(context)){
			new AsyncUploadDonation().execute("Volunteer");
		return true;
		}
		return false;
	}
	public boolean upload_pledges(Context context){
		if(isNetworkConnected(context)){
			new AsyncUploadDonation().execute("Pledge");
		return true;
		}
		return false;
	}
	public boolean upload_all(Context context){
		if(isNetworkConnected(context)){
			new AsyncUploadDonation().execute("All");
		return true;
		}
		return false;
	}
	public boolean isNetworkConnected(Context context){
		ConnectivityManager connMgr = (ConnectivityManager)    context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			Log.d("SMSGateway", "Network Available");
			return true;
		} else {
			Log.d("SMSGateway", "No Network");
			return false;
		}

	}
}
