package org.aapk.donationGateway;

import org.aapk.donationGateway.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
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
	String id = idField.getText().toString();
	if (id.isEmpty()){
		Error+="ID cannot be Blank\n";
	}
	
	final EditText limitField = (EditText) findViewById(R.id.editTextLimit);
	String limit = limitField.getText().toString();
	if (limit.isEmpty()){
		Error+="Limit cannot be Blank\n";
	}
	
    final Spinner roleSpinner = (Spinner) findViewById(R.id.spinnerRole);
    String role = roleSpinner.getSelectedItem().toString();
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

    
}
