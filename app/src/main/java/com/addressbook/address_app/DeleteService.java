package com.addressbook.address_app;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.ArrayList;

public class DeleteService {
    static ArrayList<Address> rem = AddAddressService.getStorage();
    private final IBinder mBinder = new AddressBinder();

    public class AddressBinder extends Binder {
        DeleteService getService(){
            return DeleteService.this;
        }
    }
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    public static void removeAddress(int index){
        rem.remove(index);
    }
}
