package com.addressbook.address_app;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.sql.*;
import java.util.ArrayList;

public class AddAddressService extends Service {
    private Connection connect = null;
    private Statement statement = null;
    private ResultSet resultSet = null;
    private PreparedStatement preparedStatement = null;

    static ArrayList<Address> storage  = new ArrayList<>();
    private final IBinder mBinder = new AddressBinder();
    static AddAddressService aas = new AddAddressService();

    public class AddressBinder extends Binder {
        AddAddressService getService() {
            return AddAddressService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void addAddress(Address a) {
        storage.add(a);
        // aas.readDB(a);
        storage.sort(Address::compareTo);
    }

    public static Address getAddress(int position){
        return storage.get(position);
    }

    public static ArrayList<Address> getStorage(){
        return storage;
    }


    public static ArrayList<String> getStringStorage(){
        ArrayList<String> temp = new ArrayList<>();
        String s;
        for(int i = 0; i < storage.size(); i++){
            s = "First name: " + storage.get(i).getFirst_name() + ", Last name: " + storage.get(i).getLast_name() + ", \nPhone Number: " + storage.get(i).getPhone_number();
            temp.add(s);
        }
        return temp;
    }

    public static int size(){
        return storage.size();
    }

    public void readDB(Address a)throws Exception{
        Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/addressDB", "root", "Teajay2023!");
        statement = connect.createStatement();
        resultSet = statement.executeQuery("select * from addressDB.addressT");
        preparedStatement = connect.prepareStatement("insert into addressDB.addressT values(default, ?, ?, ?)");
        preparedStatement.setString(1, a.getFirst_name());
        preparedStatement.setString(2, a.getLast_name());
        preparedStatement.setString(3, a.getPhone_number());
        preparedStatement.executeUpdate();
           /*preparedStatement = connect.prepareStatement("DELETE FROM addressDB.addressT WHERE first_name = ? ;");
            preparedStatement.setString(1, "");*/
        preparedStatement = connect.prepareStatement("select * from addressDB.addressT");
        resultSet = preparedStatement.executeQuery();
    }



          /*   public static void addAddress(Address a) {
                storage.add(a);
        }
         public static ArrayList<String> getStorage(){
             Address address;
             ArrayList<String> stringList = new ArrayList<>();
            for(int i = 0; i < storage.size(); i++) {
                address = storage.get(i);
                String temp = address.getFirst_name() + ", " +
                        address.getLast_name() + ", " +
                        address.getPhone_number();
                 stringList.add(temp);
            }
            return stringList;
         }*/

}