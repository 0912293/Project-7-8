package com.roadtrippies.app.roadtrippies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText userEmail, userPass;
    private Button btn_create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userEmail = (EditText) findViewById(R.id.userEmailTV);
        userPass = (EditText) findViewById(R.id.userPasswordTV);

        btn_create = (Button) findViewById(R.id.btn_create);

        btn_create.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_create:
                createAccount();
                break;
        }
    }

    public void createAccount(){
        Log.d("Debug", "Account creation started");
//#TODO send shit to db
        GlobalClass.getInstance().user = userEmail.getText().toString();
        GlobalClass.getInstance().pass = userPass.getText().toString();
        GlobalClass.getInstance().LoggedIn = true;
        GlobalClass.getInstance().LoginType = false;
        Toast.makeText(this,"Account created and logged in", Toast.LENGTH_LONG).show();
        Log.d("Debug", "Success");
        startActivity(new Intent(this,MainActivity.class));
    }
}
