package com.roadtrippies.app.roadtrippies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText userEmail, userName, userPass;
    private Button btn_create;
    dbCon db = new dbCon();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userEmail = (EditText) findViewById(R.id.userEmailTV);
        userPass = (EditText) findViewById(R.id.userPasswordTV);
        userName = (EditText) findViewById(R.id.userNameTV);

        btn_create = (Button) findViewById(R.id.btn_create);

        btn_create.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_create:
                Log.d("Debug", "1");
                db.CONN();
                Log.d("Debug", "2");
                createAccount();
                break;
        }
    }

    public void createAccount() {
        Log.d("Debug", "Account creation started");
//#TODO send shit to db
        GlobalClass.getInstance().email = userEmail.getText().toString();
        Log.d("Debug", "3");
        GlobalClass.getInstance().pass = userPass.getText().toString();
        Log.d("Debug", "4");
        GlobalClass.getInstance().name = userName.getText().toString();
        Log.d("Debug", "5");
        GlobalClass.getInstance().LoginType = false;
        Log.d("Debug", "6");

        String query = "SELECT dbo.users.email FROM dbo.users WHERE dbo.users.email = ?";
        try {
            PreparedStatement preparedStmt = db.conn.prepareStatement(query);
            preparedStmt.setString(1, userEmail.getText().toString());
            Log.d("Debug",preparedStmt.toString());
            ResultSet rs = preparedStmt.executeQuery();
            if(rs.next()){
                Log.d("Debug", "1");
                GlobalClass.getInstance().LoggedIn = false;
                GlobalClass.getInstance().clear();
                Toast.makeText(this, "Email already used", Toast.LENGTH_LONG).show();
            }else{
                String query2 = "INSERT INTO dbo.users (email,password,name) VALUES (?,?,?)";
                Log.d("Debug", "7");
                try {
                    PreparedStatement ps = db.conn.prepareStatement(query2);
                    ps.setString(1, GlobalClass.getInstance().email);
                    ps.setString(2, GlobalClass.getInstance().pass);
                    ps.setString(3, GlobalClass.getInstance().name);
                    Log.d("Debug", "8");
                    preparedStmt.execute();
                    Toast.makeText(this, "Account created and logged in", Toast.LENGTH_LONG).show();
                    Log.d("Debug", "Success");
                    GlobalClass.getInstance().LoggedIn = true;
                    startActivity(new Intent(this, MainActivity.class));
                } catch (Exception e) {
                    Log.d("Debug", "Failed query");
                }
            }
        } catch (Exception e) {
            Log.d("Debug", "Failed query");
        }
    }
}
