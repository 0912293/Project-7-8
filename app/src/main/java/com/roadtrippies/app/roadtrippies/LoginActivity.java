package com.roadtrippies.app.roadtrippies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener{

    private LinearLayout profile_section;
    private Button signOut, btn_signin, btn_create;
    private SignInButton GsignIn;
    private TextView name, email, useremail, password, google,create;
    private ImageView profile_picture;
    private GoogleApiClient googleApiClient;
    private String profileImgUrl;
    private static final int REQ_CODE = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!GlobalClass.getInstance().LoggedIn) {
            setContentView(R.layout.login_activity);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            GsignIn = (SignInButton) findViewById(R.id.g_signIn);
            btn_create = (Button)findViewById(R.id.btn_to_create);

            btn_signin = (Button)findViewById(R.id.btn_signin);
            name = (TextView) findViewById(R.id.profile_name);
            email = (TextView) findViewById(R.id.profile_email);

            useremail = (TextView) findViewById(R.id.userEmail);
            password = (TextView) findViewById(R.id.userPasword);

            GsignIn.setOnClickListener(this);
            btn_create.setOnClickListener(this);
            btn_signin.setOnClickListener(this);

            GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
            googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();
            GlobalClass.getInstance().LoggedIn = true;
        } else if(GlobalClass.getInstance().LoggedIn){
            setContentView(R.layout.profile_activity);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            profile_picture = (ImageView) findViewById(R.id.profile_pic);
            name = (TextView) findViewById(R.id.profile_name);
            email = (TextView) findViewById(R.id.profile_email);
            signOut = (Button) findViewById(R.id.btn_logout);

            name.setText(GlobalClass.getInstance().name);
            email.setText(GlobalClass.getInstance().email);

            if(GlobalClass.getInstance().profileImgUrl!=null) {
                profile_picture.setVisibility(View.VISIBLE);
                Glide.with(this).load(GlobalClass.getInstance().profileImgUrl).into(this.profile_picture); //gets profile pic
            }
            signOut.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.btn_to_create:
                startActivity(new Intent(this,CreateAccountActivity.class));
                break;

            case R.id.g_signIn:
                GlobalClass.getInstance().LoginType = true;
                signIn(GlobalClass.getInstance().LoginType);
                break;

            case R.id.btn_signin:
                GlobalClass.getInstance().LoginType = false;
                signIn(GlobalClass.getInstance().LoginType);
                break;

            case R.id.btn_logout:
                Log.d("Debug", "logout pressed");
                signOut();
                Log.d("Debug", "logout finished");
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("Debug", "ConnFailed");
    }

    private void signIn(boolean type)
    {
        if(type) {
            Intent i = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
            Log.d("Debug", "Passed GsignIn()");
            startActivityForResult(i, REQ_CODE);
        }else if (!type){
            Log.d("Debug", "normal signin");
            dblogin();
        }
    }

    private void dblogin(){
        DBCon db = new DBCon();
        db.CONN();
        Log.d("Debug","dblogin");
        String query = "SELECT * FROM dbo.users WHERE dbo.users.email = ?";
//        String query = "SELECT dbo.users.password FROM dbo.users WHERE dbo.users.email = ?";
        try {
            PreparedStatement preparedStmt = db.conn.prepareStatement(query);
            preparedStmt.setString(1, useremail.getText().toString());
            Log.d("Debug",preparedStmt.toString());
            ResultSet rs = preparedStmt.executeQuery();
            if(rs.next()){
                if (password.getText().toString().equals(rs.getString("password"))) {
                    Log.d("Debug", "1");
                    GlobalClass.getInstance().name = rs.getString("name");
                    GlobalClass.getInstance().email = useremail.getText().toString();
                    GlobalClass.getInstance().pass = password.getText().toString();
                    GlobalClass.getInstance().LoggedIn = true;
                    Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show();
                    updateUI(true);
                }else{
                    Log.d("Debug", "wrong password");
                    Toast.makeText(this, "wrong password", Toast.LENGTH_LONG).show();
                    GlobalClass.getInstance().clear();
                }
            }else{
                Log.d("Debug", "did not find things");
                Toast.makeText(this, "Failed to log in", Toast.LENGTH_LONG).show();
                GlobalClass.getInstance().clear();
            }
        } catch (Exception e) {
            Log.d("Debug", "Failed query");
        }
    }

    private void signOut() {
        Log.d("Debug", "boop");
        GlobalClass.getInstance().clear();
        Toast.makeText(this,"Signed out", Toast.LENGTH_SHORT).show();
        updateUI(true);//#TODO fix this shit...maybe
//        if(googleApiClient.isConnected()){
//            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback( //temporarily removed due to not knowing how to fix this shit
//                    new ResultCallback<Status>() {
//                        @Override
//                    public void onResult(@NonNull Status status) {
//                        Log.d("Debug", "starting ui update");
//                        updateUI(true);
//                    }
//                }
//            );
//        }

    }

    private void handleResult(GoogleSignInResult result){
        if(result.getStatus().getStatusCode() == CommonStatusCodes.SUCCESS){
            GoogleSignInAccount account = result.getSignInAccount();
            String name = account.getDisplayName();
            String email = account.getEmail();

            if(account.getPhotoUrl() != null) {
                profileImgUrl = account.getPhotoUrl().toString();
                GlobalClass.getInstance().profileImgUrl = profileImgUrl;
            }
            GlobalClass.getInstance().name = name;
            GlobalClass.getInstance().email = email;
            GlobalClass.getInstance().LoginType = true;
            Toast.makeText(this, name, Toast.LENGTH_LONG).show();

            Log.d("Debug", "Passed succesful handleResult()");
            Toast.makeText(this,"Logged in with google", Toast.LENGTH_SHORT).show();
            updateUI(true);
        }
        else{
            Log.d("Debug", "Passed failed handleResult()");
            updateUI(false);
        }
    }

    private void updateUI(boolean isLogged) {
        if(isLogged == true){
            Log.d("Debug", "Passed succesful updateUI()");
            startActivity(new Intent(this,MainActivity.class));
        }
        else {
            Log.d("Debug", "Passed failed updateUI()");
            this.profile_section.setVisibility(View.GONE);
            this.GsignIn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Debug", "Passed onActivityResult()");

        if(requestCode == REQ_CODE && data != null){
            Log.d("Debug", "Passed onActivityResult() conditional");
            googleApiClient.connect();
            GoogleSignInResult res = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.d("Debug", "Starting result handle");
            handleResult(res);
        }
    }
}
