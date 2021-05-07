package com.addressbook.address_app;

import android.os.Parcel;
import android.os.Parcelable;

public class Address implements Parcelable {
    String first_name, last_name, phone_number;
    //int phone_number;
    boolean editStatus;
    boolean fName = false, lName = false, phoneNumber = false;

    public String getFirst_name() {
        return first_name;
    }
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }
    public String getLast_name() {
        return last_name;
    }
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }
    public String getPhone_number() {
        return phone_number;
    }
    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
    public boolean getEditStatus(){
        return editStatus;
    }
    public void setEditStatus(boolean b){
        this.editStatus = b;
    }

    public String toString(){
        String s = "First name: " + first_name + ", ";
        s += "Last name: " + last_name + ", ";
        s += "\nPhone number: " + phone_number + ",";
        return s;
    }

    public boolean equals(Object obj){
        if(obj instanceof Address){
            Address otherAddress = (Address) obj;
            if(this.getFirst_name().equalsIgnoreCase(otherAddress.getFirst_name())){
                fName = true;
            }
            if (this.getLast_name().equalsIgnoreCase(otherAddress.getLast_name())){
                lName = true;
            }
            if(this.getPhone_number() == otherAddress.getPhone_number()){
                phoneNumber = true;
            }
        }
        return fName || lName || phoneNumber;
    }

    public boolean equal(Object obj){
        if(obj instanceof Address){
            Address otherAddress = (Address) obj;
            if (this.getLast_name().equalsIgnoreCase(otherAddress.getLast_name())){
                lName = true;
            }
            if(this.getPhone_number() == otherAddress.getPhone_number()){
                phoneNumber = true;
            }
        }
        return lName || phoneNumber;
    }

    public int compareTo(Address a){
        return (first_name.compareTo(a.getFirst_name()));
    }

    Address(String first_name, String last_name){
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone_number = "-1";
    }
    Address(String first_name){
        this.first_name = first_name;
        this.last_name = "empty";
        this.phone_number = "-1";
        editStatus = false;
    }
    Address(String first_name, String last_name, String phone_number){
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone_number = phone_number;
        editStatus = false;
    }

    public Address(Parcel in){
        String[] data = new String[3];

        in.readStringArray(data);
        // the order needs to be the same as in writeToParcel() method
        this.first_name = data[0];
        this.last_name = data[1];
        this.phone_number = data[2];
    }

    @Override
    public int describeContents() {
        return 0;
    }
    public static final Creator CREATOR = new Creator() {
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        public Address[] newArray(int size) {
            return new Address[size];
        }
    };
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.first_name,
                this.last_name,
                this.phone_number});
    }
}