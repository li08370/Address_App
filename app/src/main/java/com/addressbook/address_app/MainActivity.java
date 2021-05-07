package com.addressbook.address_app;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.*;
import static com.addressbook.address_app.AddAddressService.addAddress;
import static java.sql.DriverManager.getConnection;


public class MainActivity extends AppCompatActivity {
    private static ArrayAdapter<String> arrayAdapter;

    private static boolean isBound;
    EditText inputAddress;
    DeleteService deleteService;
    ListView addressList;
    Button addAddress;
    ToggleButton status;
    //DeleteService deleteService;

    private Connection connect = null;
    private Statement statement = null;
    private ResultSet resultSet = null;
    private PreparedStatement preparedStatement = null;

    private static Address address;
    private static boolean adding = false;
    private static boolean loading = true;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity ma = new MainActivity();
        inputAddress = (EditText) findViewById(R.id.inputText);
        addAddress = (Button) findViewById(R.id.addButton);
        addressList = (ListView) findViewById(R.id.addressList);
        status = (ToggleButton) findViewById(R.id.toggleButton);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        addressList.setAdapter(arrayAdapter);
        if(loading){
            try {
                ma.getData();
                loading = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //if their is information to draw on from addPage
        if (adding || AddingPage.getEditing()) {
            Bundle extras = getIntent().getExtras();
            address = extras.getParcelable("Address");
            try {
                onButtonClick();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        addressList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            String value;
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(status.isChecked()) {
                    //deleting
                    remove(position);
                }else {
                    //editing
                    AddingPage.setEditing(true);
                    int ps = position;
                    Intent i = new Intent(getApplicationContext(), AddingPage.class);
                    i.putExtra("position", ps);
                    startActivity(i);
                }
            }
        } );


        //opens AddingPage
        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AddingPage.class);
                startActivity(i);
            }
        });
        //constant reading field and search engine
        inputAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                arrayAdapter.getFilter().filter(s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DeleteService.AddressBinder binder = (DeleteService.AddressBinder) service;
            deleteService = binder.getService();
            isBound = true;
        }
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };
    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(MainActivity.this, DeleteService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }
    @Override
    protected void onRestart(){
        super.onRestart();
        Intent intent = new Intent(MainActivity.this, DeleteService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (isBound) {
            unbindService(mConnection);
            isBound = false;
        }
    }
    public static void setAdding(boolean b){
        adding = b;
    }
    //updates the listView addressList
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onButtonClick() throws Exception {
        addAddress(address);
        arrayAdapter.clear();
        arrayAdapter.addAll(AddAddressService.getStringStorage());
    }
    public void remove(int position){
        DeleteService.removeAddress(position);
        arrayAdapter.clear();
        arrayAdapter.addAll(AddAddressService.getStringStorage());
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getData(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connect = getConnection("jdbc:mysql://localhost:3306/addressDB?autoReconnect=true&useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","root", "Teajay2023!");
            // connect = getConnection("jdbc:mysql://localhost:3306/addressDB?autoReconnect=true&useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "Teajay2023!");
            statement = connect.createStatement();
            resultSet = statement.executeQuery("select * from addressDB.addressT");
            Address iAddress = new Address("", "", "");
            while (resultSet.next()) {
                String first_name = resultSet.getString("first_name");
                String last_name = resultSet.getString("last_name");
                String phone_number = resultSet.getString("phone_number");
                iAddress.setFirst_name(first_name);
                iAddress.setLast_name(last_name);
                iAddress.setPhone_number(phone_number);
                addAddress(iAddress);
            }
            preparedStatement = connect.prepareStatement("truncate table addressT");
            preparedStatement.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}