package com.roadtrippies.app.roadtrippies;

/**
 * Created by Kevin on 23/06/2017.
 */

public class GlobalClass {
    private static GlobalClass mInstance = null;

    public boolean LoggedIn,LoginType;
    public String email,user,profileImgUrl;


    protected GlobalClass(){}

    public static synchronized GlobalClass getInstance(){
        if(null == mInstance){
            mInstance = new GlobalClass();
        }
        return mInstance;
    }
}
