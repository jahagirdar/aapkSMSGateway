package org.aapk.donationGateway;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

public class AsyncUploadDonation extends AsyncTask<String, Void, String>{

	@Override
	protected String doInBackground(String... cmd) {
		  try {
if(cmd[0].equals("Donation")){
	uploadDonation();
}else if(cmd[0].equals("Volunteer")){
	uploadVolunteer();
}else if(cmd[0].equals("Pledge")){
	uploadPledge();
}else if(cmd[0].equals("Init")){
	upload_init();
}else if(cmd[0].equals("All")){
	uploadDonation();
	uploadVolunteer();
	uploadPledge();

}
Log.d("SMSGateway", "Upload Done");
			  
          } catch (IOException e) {
        	  Log.d("SMSGateway", "IO Exception in Upload");
              return "Unable to retrieve web page. URL may be invalid.";
          } catch (JSONException e) {
			// TODO Auto-generated catch block
        	  Log.d("SMSGateway", "JSON Exception in Upload");
			e.printStackTrace();
		}
return null;
	}
private boolean uploadDonation() throws IOException, JSONException{
	JSONObject js=null;
	  String resp=downloadUrl("http://fundraiser-aapk.rhcloud.com/donation/json/lastUpload");
	  if(resp!=null){
		  Log.d("uploadDonation","before parseResp "+resp);
		  js=new JSONObject(resp);
		  int lastUpload;
		  if(js.getString("id").equals("null")||js.getString("id")==null){lastUpload=0;}
		  else{ lastUpload=js.getInt("id");}
		  Context cxt=AppContext.getAppContext();
		  if(cxt==null){Log.d("uploadDonation"," Context is null "+cxt.toString());}
		  FundDatabase fdb=FundDatabase.getInstance(cxt);
		  lastUpload++;
		  JSONObject json;
		  Log.d("uploadDonation","before getJSONDonation "+lastUpload);
		  while((json=fdb.getJSONDonation(String.valueOf(lastUpload))) != null){
			  Log.d("uploadDonation",json.toString());
		sendJSON("http://fundraiser-aapk.rhcloud.com/donation/json/add",json);  
		  lastUpload++;
			};
			return true;
		  }
	  else{
	return false;
	  }
}
private boolean uploadVolunteer() throws IOException, JSONException{
	JSONObject js=null;
	  String resp=downloadUrl("http://fundraiser-aapk.rhcloud.com/volunteer/json/lastUpload");
	  if(resp!=null){
		  js=new JSONObject(resp);
		  int lastUpload;
		  if(js.getString("id").equals("null")||js.getString("id")==null){lastUpload=0;}
		  else{ lastUpload=js.getInt("id");}
		  VolDatabase vdb=VolDatabase.getInstance(AppContext.getAppContext());
		  lastUpload++;
		  JSONObject json;
		  while((json=vdb.getJSONVolunteer(String.valueOf(lastUpload))) != null){
		
			  sendJSON("http://fundraiser-aapk.rhcloud.com/volunteer/json/add",json);  
		  lastUpload++;
			};
			return true;
		  }
	  else{
	return false;
	  }
}	

private boolean uploadPledge() throws IOException, JSONException{
	JSONObject js=null;
	  String resp=downloadUrl("http://fundraiser-aapk.rhcloud.com/pledge/json/lastUpload");
	  if(resp!=null){
		  js=new JSONObject(resp);
		  int lastUpload;
		  if(js.getString("id").equals("null")||js.getString("id")==null){lastUpload=0;}
		  else{ lastUpload=js.getInt("id");}
		  PledgeDatabase pdb=PledgeDatabase.getInstance(AppContext.getAppContext());
		  lastUpload++;
		  JSONObject json;
		  while((json=pdb.getJSONPledge(String.valueOf(lastUpload))) != null){
		sendJSON("http://fundraiser-aapk.rhcloud.com/pledge/json/add",json);  
		  lastUpload++;
			};
			return true;
		  }
	  else{
	return false;
	  }
}	
private boolean upload_init() throws IOException, JSONException{
	
	VolDatabase vdb=VolDatabase.getInstance(AppContext.getAppContext());
	long volCount=vdb.getVolunteerCount();
	Log.d("SMSGateway","Volunteer count:"+volCount);
	long insertcount=0;
	long index=0;
	while (insertcount<volCount){
		JSONObject jsn=vdb.getJSONVolunteer(String.valueOf(index));
		if(jsn!=null){
			sendJSON("http://fundraiser-aapk.rhcloud.com/volunteer/json/add",jsn);
			insertcount++;
		}
		index++;
		if(index==200){insertcount=volCount;}
	}
//	vdb.dump();
	FundDatabase fdb=FundDatabase.getInstance(AppContext.getAppContext());
	long fundCount=fdb.getDonationCount();
	Log.d("SMSGateway","Fund Count"+fundCount);
	insertcount=0;
	 index=0;
	while (insertcount<fundCount){
		JSONObject jsn=fdb.getJSONDonation(String.valueOf(index));
		if(jsn!=null){
			sendJSON("http://fundraiser-aapk.rhcloud.com/donation/json/add",jsn);
			insertcount++;
		}
		index++;
		if(index==200){insertcount=fundCount;}
	}

	PledgeDatabase pdb=PledgeDatabase.getInstance(AppContext.getAppContext());
	long pledgeCount=pdb.getPledgeCount();
	Log.d("SMSGateway","Pledge Count"+pledgeCount);
	insertcount=0;
	 index=0;
	while (insertcount<pledgeCount){
		JSONObject jsn=pdb.getJSONPledge(String.valueOf(index));
		if(jsn!=null){
			sendJSON("http://fundraiser-aapk.rhcloud.com/pledge/json/add",jsn);
			insertcount++;
		}
		index++;
		if(index==200){insertcount=pledgeCount;}
	}
	return true;
}	
private boolean sendJSON(String myurl, JSONObject json) {
		
    try {
	
    URL url = new URL(myurl);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setReadTimeout(10000 /* milliseconds */);
    conn.setConnectTimeout(15000 /* milliseconds */);
    conn.setRequestMethod("POST");
    conn.setDoInput(true);
    conn.setDoOutput(true);
    OutputStream out = conn.getOutputStream();
    OutputStreamWriter wr = new OutputStreamWriter(out);
    wr.write(json.toString()); //ezm is my JSON object containing the api commands
    wr.flush();
    wr.close();
    int response = conn.getResponseCode();
    Log.d("SMSGateway", "SendJSON: "+myurl+" The response is: " + response);
    
if(response==200) {
	InputStream is = conn.getInputStream();

    // Convert the InputStream into a string
    String contentAsString = readIt(is, 500);
return true;
} else {
	return false;
}
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ProtocolException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return false;
}
private String downloadUrl(String myurl) throws IOException {
    InputStream is = null;
    // Only display the first 500 characters of the retrieved
    // web page content.
        
    try {
        URL url = new URL(myurl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        int response = conn.getResponseCode();
        Log.d("SMSGateway", "DownloadURL "+myurl+ " The response is: " + response);
if(response==200) {
	 is = conn.getInputStream();

     // Convert the InputStream into a string
     String contentAsString = readIt(is, 500);
     return contentAsString;

	}
return null;
    } finally {
        if (is != null) {
            is.close();
        } 
    }
}
public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
    Reader reader = null;
    reader = new InputStreamReader(stream, "UTF-8");        
    char[] buffer = new char[len];
    
    int reallen=reader.read(buffer);
    if(reallen<=0) return null;
    Log.d("readIt","reallen->"+String.valueOf(reallen));
    String nstr=String.valueOf(buffer).substring(0,reallen);
    Log.d("readIt",String.valueOf(reallen)+", nstr="+nstr);
       return new String(nstr);
}
}
