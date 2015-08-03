package org.aapk.donationGateway;

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
	UrlService urlService=new UrlService();

	public void onReceive(Context context, Intent intent) {
		  AppContext.setContext(context);
			
		// Retrieves a map of extended data from the intent.
		final Bundle bundle = intent.getExtras();

		try {

			if (bundle != null) {

				final Object[] pdusObj = (Object[]) bundle.get("pdus");
				FundDatabase fundDB = FundDatabase.getInstance(context);
				VolDatabase volDB = VolDatabase.getInstance(context); 
				for (int i = 0; i < pdusObj.length; i++) {

					SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
					String phoneNumber = currentMessage.getDisplayOriginatingAddress();

					String senderNum = phoneNumber;
					String message = currentMessage.getDisplayMessageBody().toLowerCase(Locale.US);
					String [] cmd=message.split("\\s\\s*");
					SmsManager smsManager=SmsManager.getDefault();
					urlService.upload_all(context);

					//Add New Volunteer
					if(cmd[0].equals("add") ){
						if(cmd.length<5){
							Log.i("SmsReceiver","Debug: Incorrect parameter list, Expected 5, got "+cmd.length);
						}
						else{
							String phone =fixPhone(cmd[1]);
							String id=cmd[2];
							String role=cmd[3];
							String name=cmd[4];
							
							Log.i("SmsReceiver","Debug: Add Vol Command");

							if(volDB.isLeaderOf(phoneNumber,role)){
								Log.i("SmsReceiver","Debug: Add Vol Command: Leader Detected");
								volDB.addVolunteer(name,phone,"2000",id,role,phoneNumber);
								Log.i("SmsReceiver","Debug: Add Vol Command: Sending SMS");
								smsManager.sendTextMessage(phone, null, "You are added as a " + role +" fundraiser, Please check with your Tresurer to know the per donation limits for cash and cheque", null, null);
							
							} else {
								Log.i("SmsReceiver","Debug:Not a Leader, "+senderNum+cmd[0]+ ", "+ cmd[1] + " Phone number:" + cmd[2] +" limit:" +cmd[3] +" id:"+ cmd[4]);

							}
						}

					}else

						//Donate
						if(cmd[0].equals("dn") ){
							if(cmd.length >=5){

								if(volDB.isFundraiser(senderNum)){
									Log.i("SmsReceiver","Debug Donation:" + cmd.length);
									String phone=fixPhone(cmd[1]);
									String amount=cmd[2];
									
									String receipt=cmd[3];
									String donor_fn=cmd[4];
									String donor_ln="";
									if(cmd.length>5)
										donor_ln=cmd[5];
									//if(Integer.parseInt(amount)<Integer.parseInt(volDB.getLimitByID(senderNum))){
										Log.i("SmsReceiver","Donation Received:" + amount +", from "+ donor_fn +" "+donor_ln +" "+phone+" Issued receipt#"+receipt);
										fundDB.addDonation(amount,phone,receipt,senderNum, donor_fn,donor_ln);
										smsManager.sendTextMessage(phone, null, "Thanks for donating Rs " + amount +" to AAP Karnataka. Your Receipt Number is " +receipt+"."+receipt, null, null);
										//smsManager.sendTextMessage(destinationAddress, scAddress, text, sentIntent, deliveryIntent)
										
									//}
								}else {
									String phone=cmd[1];
									smsManager.sendTextMessage(phone, null, "This volunteer is not an authorized fundraiser. Donation not accepted. Contact AAP at 9980156580", null, null);
									
								}

							} else{
								Log.i("SmsReceiver","Donation Incorrect parameters expected 5+ got "+cmd.length );
							}

						}else


							// Check
							//Donate
							if(cmd[0].equals("chk") && cmd.length>=2){
								String id=cmd[1];
								if(volDB.isVolunteer(id)){

									Log.i("SmsReceiver","Volunteer Check Passed:" + id);
									smsManager.sendTextMessage(senderNum,null,"This volunteer is an authorized fundraiser for AAM AADMI Party Karnataka. In case of any query you can contact us at donation@aapkarnataka.org", null, null);
									Log.i("SmsReceiver","sms Sent to:" + senderNum);
								}
								else {
									Log.i("SmsReceiver","Volunteer Check Failed:" + id);
									smsManager.sendTextMessage(senderNum,null,"This volunteer is not authorized to collect donations. please donate online at donate.aamaadmiparty.org " , null, null);
									Log.i("SmsReceiver","sms Sent to:" + senderNum);
								}
								
							}else if (cmd[0].equals("pledge") && cmd.length>=2){
								
									String amount=cmd[1];
									String candidate=cmd[2];
									PledgeDatabase pdb= PledgeDatabase.getInstance(context);
									pdb.addPledge(senderNum,amount,candidate);
									smsManager.sendTextMessage(senderNum,null,"Thank you for pledging Rs " +amount + " Towards funding clean politics. To know more visit aapkarnataka.org", null, null);
									
									
							}else if(cmd[0].equals("depo") && cmd.length>=2){
								String amount=cmd[1];
								String txnID=cmd[2];
								AccountDatabase adb=AccountDatabase.getInstance(context);
								adb.addDeposit(senderNum,amount,txnID);
							}else {
							
								
														int duration = Toast.LENGTH_LONG;
								Toast toast = Toast.makeText(context,
										"senderNum: "+ senderNum + ", message Did not qualify as a valid command. Please check syntax and variables.: " + message, duration);
								toast.show();
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
			int duration = Toast.LENGTH_LONG;
			Toast toast = Toast.makeText(context,
					"Exception: "+e.toString(), duration);
			toast.show();

		}
	}    
	private String fixPhone (String Phone){
		if (Phone.length()==13 && Phone.startsWith("+")) { return Phone;}
		if (Phone.length()==10 && !Phone.startsWith("+")){ return "+91"+Phone;	}
		return null;
	}
}
