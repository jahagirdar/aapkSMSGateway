package com.example.aapksmsgateway;

import java.util.Locale;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class incommingSMS extends BroadcastReceiver {
	 // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();
     
    public void onReceive(Context context, Intent intent) {
     
        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();
 
        try {
             
            if (bundle != null) {
                 
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                FundDatabase fundDB =new FundDatabase(context);
                VolDatabase volDB =new VolDatabase(context); 
                for (int i = 0; i < pdusObj.length; i++) {
                     
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                     
                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody().toLowerCase(Locale.US);
                    String [] cmd=message.split("\\s");
                    SmsManager smsManager=SmsManager.getDefault();
                    
                    
                    //Add New Volunteer
                    if(cmd[0].equals("add")){
                    	String name=cmd[1];
                    	String phone =cmd[2];
                    	String limit=cmd[3];
                    	String id=cmd[4];
                    	String role=cmd[5];
                    	Log.i("SmsReceiver","Debug: Add Vol Command");
                        
                    	if(volDB.isLeader(phoneNumber)){
                    		Log.i("SmsReceiver","Debug: Add Vol Command: Leader Detected");
                    		volDB.addVolunteer(name,phone,limit,id,role,phoneNumber);
                    		Log.i("SmsReceiver","Debug: Add Vol Command: Sending SMS");
                    		smsManager.sendTextMessage(phone, null, "You are added as a " + role +" fundraiser, Your per Donor max limit is  Rs"+ limit, null, null);
                    } else {
                    	Log.i("SmsReceiver","Debug:"+cmd[0]+ ", "+ cmd[1] + " Phone number:" + cmd[2] +" limit:" +cmd[3] +" id:"+ cmd[4]);
                        	
                    }
                    }
                    
                    //Donate
                    if(cmd[0].equals("dn")){
                    	if(volDB.isFundraiser(senderNum)){
                    		String amount=cmd[1];
                    		String phone=cmd[2];
                    		String receipt=cmd[3];
                    		String donor_fn=cmd[4];
                    		String donor_ln=cmd[5];
                    		if(Integer.parseInt(amount)<Integer.parseInt(volDB.getLimit(senderNum))){
                    		Log.i("SmsReceiver","Donation Received:" + amount +", from "+ donor_fn +" "+donor_ln +" "+phone+" Issued receipt#"+receipt);
                    		fundDB.addDonation(amount,phone,receipt,senderNum, donor_fn,donor_ln);
                    	 	smsManager.sendTextMessage(phone, null, "Thanks for donation " + amount +" to AAP", null, null);
                    		}
                    	}
                    	
                    }
                    
                    
                    // Check
                  //Donate
                    if(cmd[0].equals("chk")){
                    	String id=cmd[1];

                		Log.i("SmsReceiver","Volunteer Check:" + id);
                		if(volDB.isVolunteer(id)){
                    		smsManager.sendTextMessage(senderNum,null,"This volunteer is authorized to collect One time cash donation upto Rs " + volDB.getLimitByID(id), null, null);
                    		}
                    	}
                    	
                    
                    // Show Alert
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context,
                                 "senderNum: "+ senderNum + ", message: " + message, duration);
                    toast.show();
                     
                } // end for loop
              } // bundle is null
 
        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);
             
        }
    }    
}
