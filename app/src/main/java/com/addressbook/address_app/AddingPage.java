package com.addressbook.address_app;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class AddingPage extends AppCompatActivity {
    private static boolean editing = false;
    int position;
    Address eAddress;
    Button done;
    EditText finput, linput, pinput;
    String fname, lname, pNumber;
    private static boolean isBound;
    AddAddressService AddressService;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adding_page);
        done = (Button) findViewById(R.id.doneButton);
        finput = (EditText) findViewById(R.id.fnameinput);
        linput = (EditText) findViewById(R.id.lnameinput);
        pinput = (EditText) findViewById(R.id.pNumber);
        MainActivity.setAdding(true);
        if(editing){
            Bundle extras = getIntent().getExtras();
            position = extras.getInt("position");
            eAddress = AddAddressService.getAddress(position);
            DeleteService.removeAddress(position);
            finput.setText(eAddress.getFirst_name());
            linput.setText(eAddress.getLast_name());
            pinput.setText(eAddress.getPhone_number());
                /*fname = extras.getString("name");
                lname = extras.getString("lname");
                pNumber = extras.getString("pNumber");
                finput.setText(fname);
                linput.setText(lname);
                pinput.setText(pNumber);*/
        }

        done.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                Address add = new Address(finput.getText().toString(), linput.getText().toString(), pinput.getText().toString());
                int size = AddAddressService.size();
                i.putExtra("Address", add);
                        /* i.putExtra("fname", finput.getText().toString());
                        i.putExtra("lname", linput.getText().toString());
                        i.putExtra("pNumber", pinput.getText().toString());*/
                startActivity(i);
            }
        });
    /*
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    i.putExtra("Address", new Address(finput.getText().toString(), linput.getText().toString(), pinput.getText().toString()));
                    startActivity(i);
                }
            });*/
    }

    //Binding service
    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            AddAddressService.AddressBinder binder = (AddAddressService.AddressBinder) service;
            AddressService = binder.getService();
            isBound = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };
    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(AddingPage.this, AddAddressService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }
    @Override
    protected void onRestart(){
        Toast.makeText(getApplicationContext(), "Restarting service bind " + isBound, Toast.LENGTH_LONG).show();
        super.onRestart();
        Intent intent = new Intent(AddingPage.this, AddAddressService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }
    @Override
    protected  void onStop() {
        super.onStop();
        if(isBound) {
            unbindService(mConnection);
            isBound = false;
        }
    }

    public static void setEditing(boolean b){
        editing = b;
    }
    public static boolean getEditing(){
        return editing;
    }

}
