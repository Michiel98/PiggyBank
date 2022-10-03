package com.piggebank;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.piggebank.android.R;
import com.piggebank.registrationPackage.Registration;

public class mainLogIn extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    CallbackManager callbackManager;

    private Button logBut;
    private LoginButton facebookBut;
    private TextView create;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private ImageView logo;
    Animation animation;
    private ProgressDialog progressDialog;

    private static final String TAG = "mainLogIn";


    public void onStart(){
        super.onStart();
        //check if user is signed in and update UI accordingly
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null) {
            updateUI(currentUser);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.launcher);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_main_log_in);

        logo = findViewById(R.id.logo);

        logo.setAnimation(animation);
        mAuth = FirebaseAuth.getInstance();

        logBut = (Button) findViewById(R.id.log_button);
        create = (TextView) findViewById(R.id.create);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        progressDialog = new ProgressDialog(mainLogIn.this);

        logBut.setOnClickListener(this);
        create.setOnClickListener(this);
        facebookBut = (LoginButton) findViewById(R.id.login_button);

        callbackManager = CallbackManager.Factory.create();

        facebookBut.setReadPermissions("email", "public_profile");
        facebookBut.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
                updateUI(mAuth.getCurrentUser());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }




    public void onClick(View view){
        if(view == logBut){
            logInUser();
        }
        if(view == create){
            registerUser();
        }

    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        final AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getInstance().getCurrentUser();

                            updateUI(user);


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(mainLogIn.this, "Authentication failed. Check your credentials.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }

    private void logInUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter your email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter your password",Toast.LENGTH_LONG).show();
            return;
        }
        //if the input is valid
        progressDialog.setMessage("Hold on, we are logging you in...");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(mainLogIn.this,"successfully logged in",Toast.LENGTH_LONG  );
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(mainLogIn.this,"Log in failed, check your credentials", Toast.LENGTH_LONG);
                            Toast.makeText(mainLogIn.this, "Authentication failed.",
                                    Toast.LENGTH_LONG).show();
                            progressDialog.cancel();
                        }

                        // ...
                    }
                });

    }

    private void updateUI(FirebaseUser currentUser) {
            Intent intent = new Intent(this,HomePage.class );
            mAuth = FirebaseAuth.getInstance();
            startActivity(intent);


    }

    public void registerUser(){
        Intent intent = new Intent(this, Registration.class);
        startActivity(intent);
    }

}
