package com.example.shop;

import android.app.Application;

public class User extends Application {

    public  String user_id;
    public String password;
    public String name;
    public int areacode;
    public String phone;
    public String idcard;
    private static User instance;


    public static User getInstance(){
        return instance;
    }

    public String getuser_id(){
        return user_id;
    }
    public void setuser_id(String s){
        user_id = s;
    }

    public String getpassword(){
        return password;
    }
    public void setpassword(String s){
        password = s;
    }



    public String getname(){
        return name;
    }
    public void setname(String s){
        name = s;
    }

    public int getareacode(){
        return areacode;
    }
    public void setareacode(int i){
        areacode = i;
    }

    public String getphone(){
        return phone;
    }
    public void setphone(String s){
        phone = s;
    }

    public String getidcard(){
        return idcard;
    }
    public void setidcard(String s){
        idcard = s;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }


}
