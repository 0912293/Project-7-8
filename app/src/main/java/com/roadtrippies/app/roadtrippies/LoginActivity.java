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
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

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
            btn_create = (Button)findViewById(R.id.btn_create);

            btn_signin = (Button)findViewById(R.id.btn_signin);
            name = (TextView) findViewById(R.id.profile_name);
            email = (TextView) findViewById(R.id.profile_email);

            useremail = (TextView) findViewById(R.id.userEmail);
            password = (TextView) findViewById(R.id.userPasword);

            GsignIn.setOnClickListener(this);

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

            name.setText(GlobalClass.getInstance().user);
            email.setText(GlobalClass.getInstance().email);

            if(GlobalClass.getInstance().profileImgUrl!=null) {
                Glide.with(this).load(GlobalClass.getInstance().profileImgUrl).into(this.profile_picture); //gets profile pic
            }
            signOut.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.g_signIn:
                signIn();
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

    private void signIn()
    {
        Intent i = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        Log.d("Debug", "Passed GsignIn()");
        startActivityForResult(i, REQ_CODE);

    }

    private void signOut() {
        Log.d("Debug", "boop");
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
            new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    Log.d("Debug", "starting ui update");
                    updateUI(true);
                }
            }
        );
    }

    private void handleResult(GoogleSignInResult result){
        if(result.getStatus().getStatusCode() == CommonStatusCodes.SUCCESS){
            GoogleSignInAccount account = result.getSignInAccount();
            String name = account.getDisplayName();
            String email = account.getEmail();

            if(account.getPhotoUrl() != null) {                                   //in case we want to get and display profile pictures somewhere
                profileImgUrl = account.getPhotoUrl().toString();
                GlobalClass.getInstance().profileImgUrl = profileImgUrl;
            }
            GlobalClass.getInstance().user = name;
            GlobalClass.getInstance().email = email;
            Toast.makeText(this, name, Toast.LENGTH_LONG).show();

            Log.d("Debug", "Passed succesful handleResult()");
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
            GoogleSignInResult res = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.d("Debug", "Starting result handle");
            handleResult(res);
        }
    }
}

