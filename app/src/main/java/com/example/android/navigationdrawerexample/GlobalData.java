package com.example.android.navigationdrawerexample;

/**
 * Created by gabdulla on 12/30/2014.
 */
import android.app.Application;
import android.nfc.Tag;

/**
 * Created by gabdulla on 12/29/2014.
 */
public class GlobalData extends Application {
    private String email;
    Tag mytag;

    public void setEmail(String dat){
        email=dat;
    }

    public String getEmail(){
        return email;
    }

    public void setMytag(Tag dat){
        mytag=dat;
    }

    public Tag getMytag(){
        return mytag;
    }

}
