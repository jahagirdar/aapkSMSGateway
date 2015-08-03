package org.aapk.donationGateway;

import java.util.Locale;

import org.aapk.donationGateway.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


public void addVolunteer(View button) { 
	  AppContext.setContext(this);

    // Do click handling here
	String Error="";
	final EditText nameField = (EditText) findViewById(R.id.editTextName);
	String name = nameField.getText().toString();
	if (name.isEmpty()){
		Error+="Name cannot be Blank\n";
	}
	
	final EditText phoneField = (EditText) findViewById(R.id.editTextPhone);
	String phone = phoneField.getText().toString();
	if (phone.isEmpty()){
		Error+="Phone Number cannot be Blank\n";
	}
	
	final EditText idField = (EditText) findViewById(R.id.editTextID);
	String id = idField.getText().toString().toLowerCase(Locale.ENGLISH);
	if (id.isEmpty()){
		Error+="ID cannot be Blank\n";
	}
	
	final EditText limitField = (EditText) findViewById(R.id.editTextLimit);
	String limit = limitField.getText().toString();
	if (limit.isEmpty()){
		Error+="Limit cannot be Blank\n";
	}
	
    final Spinner roleSpinner = (Spinner) findViewById(R.id.spinnerRole);
    String role = roleSpinner.getSelectedItem().toString().toLowerCase(Locale.ENGLISH);
    if (role.isEmpty()){
		Error+="Role must be selected\n";
	}
	
    if(!Error.isEmpty()){
    	new AlertDialog.Builder(this)
        .setTitle("Error All items should be specified")
        .setMessage(Error)
        .show();
    } else {
VolDatabase db = VolDatabase.getInstance(this);
db.addVolunteer(name, phone, limit, id, role, "Manual");
    }


}

public void dump_vol(View button){
	  AppContext.setContext(this);

UrlService u=new UrlService();
Log.d("SMSGateway", "Starting Upload Volunteers");
u.upload_donations(this);
}
public void dump_don(View button){
	  AppContext.setContext(this);

	UrlService u=new UrlService();
	Log.d("SMSGateway", "Starting Upload Donation");
	u.upload_volunteers(this);
}
public void dump_pledge(View button){
	  AppContext.setContext(this);

	UrlService u=new UrlService();
	Log.d("SMSGateway", "Starting Upload Pledge");
	u.upload_pledges(this);
}
public void dump_all(View button){
	  AppContext.setContext(this);

	UrlService u=new UrlService();
	Log.d("SMSGateway", "Starting Dump All Pledge");
	u.upload_all(this);
}    
}
