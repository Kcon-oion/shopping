package com.example.shop;

import android.app.Application;

public class Mycountuser extends Application {
    public  String user_id;
    public String password;
    public String getuser_id(){
        return user_id;
    }
    public void setuser_id(String s){
        password = s;
    }
}
