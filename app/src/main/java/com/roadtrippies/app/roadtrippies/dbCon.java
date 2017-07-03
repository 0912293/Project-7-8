package com.roadtrippies.app.roadtrippies;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 * Created by Kevin on 26/06/2017.
 */

public class DBCon {
    String ip = "145.24.222.174:1433";
    String classs = "net.sourceforge.jtds.jdbc.Driver";
    String db = "Roadtrippies";
    String un = "Roadtrippies";
    String password = "Roadtrippies";
    Connection conn;

    @SuppressLint("NewApi")
    public Connection CONN() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        conn = null;
        String ConnURL = null;
        try {
            Class.forName(classs);
            ConnURL = "jdbc:jtds:sqlserver://" + ip + ";"
                    + "databaseName=" + db + ";user=" + un + ";password="
                    + password + ";";
            conn = DriverManager.getConnection(ConnURL);
            Log.d("Debug","success");
        } catch (SQLException se) {
            Log.d("Debug","SQLException");
            Log.d("Debug",se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.d("Debug","ClassNotFoundException");
        } catch (Exception e) {
            Log.d("Debug","Exception");
        }
        return conn;
    }

}
