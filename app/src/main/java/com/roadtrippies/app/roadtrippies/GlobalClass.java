package com.roadtrippies.app.roadtrippies;

import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by Kevin on 23/06/2017.
 */

public class GlobalClass {
    private static GlobalClass mInstance = null;

    public boolean LoggedIn,LoginType;
    public String email,user,profileImgUrl;
    public GoogleApiClient googleApiClient;


    protected GlobalClass(){}

    public static synchronized GlobalClass getInstance(){
        if(null == mInstance){
            mInstance = new GlobalClass();
        }
        return mInstance;
    }
}
