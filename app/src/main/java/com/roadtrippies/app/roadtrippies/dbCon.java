package com.roadtrippies.app.roadtrippies;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
/**
 * Created by Kevin on 26/06/2017.
 */

public class dbCon {
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
            Log.d("Debug","failed db conn");
        } catch (ClassNotFoundException e) {
            Log.d("Debug","failed db conn");
        } catch (Exception e) {
            Log.d("Debug","failed db conn");
        }
        return conn;
    }

}
