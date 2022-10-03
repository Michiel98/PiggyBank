package com.piggebank.registrationPackage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.piggebank.android.R;
import com.piggebank.mainLogIn;

public class Registration extends AppCompatActivity {

    EditText nickname;
    EditText mail;
    EditText password1;
    EditText password2;
    Button registration;
    Button back;

    ProgressDialog progressDialog;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        nickname = (EditText) findViewById(R.id.fsname);
        mail =(EditText) findViewById(R.id.email);
        password1 =(EditText) findViewById(R.id.wachtwoord1);
        password2 =(EditText) findViewById(R.id.wachtwoord2);
        registration = (Button) findViewById(R.id.registration);
        back = (Button) findViewById(R.id.Back);
    }


    public void back(View view){
        Intent intent = new Intent(this, mainLogIn.class);
        startActivity(intent);
    }

    public void registreren(View view){

        registerUser();
    }

    public void registerUser(){

        final String Nickname = nickname.getText().toString().trim();
        final String email = mail.getText().toString().trim();
        String passwordA = password1.getText().toString().trim();
        String passwordB = password2.getText().toString().trim();

        if(TextUtils.isEmpty(Nickname)){
            //email is leeg
            Toast.makeText(this,"Please enter your name", Toast.LENGTH_SHORT).show();
            //stop functie
            return;
        }

        if(TextUtils.isEmpty(email)){
            //email is leeg
            Toast.makeText(this,"Please enter email", Toast.LENGTH_SHORT).show();
            //stop functie
            return;
        }

        if(TextUtils.isEmpty(passwordA)||TextUtils.isEmpty(passwordB)){
            //wachtwoord is leeg
            Toast.makeText(this,"Please enter password", Toast.LENGTH_SHORT).show();
            // stop functie
            return;
        }


        if(!passwordA.equals(passwordB)){
            //wachtwoorden matchen niet
            Toast.makeText(this,"Please enter the same password", Toast.LENGTH_SHORT).show();
            // stop functie
            return;
        }

        if(passwordA.length()<6||passwordB.length()<6){
            //wachtwoorden matchen niet
            Toast.makeText(this,"Please enter a bigger password", Toast.LENGTH_SHORT).show();
            // stop functie
            return;
        }

        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,passwordA)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //user is geregistreerd

                            User user = new User(Nickname,email);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        FirebaseUser user = firebaseAuth.getCurrentUser();
                                        firstLogIn(user);
                                        Toast.makeText(Registration.this, "Loging you in", Toast.LENGTH_LONG).show();
                                    }else{
                                        Toast.makeText(Registration.this, "Login failed", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });


                            progressDialog.cancel();
                            Toast.makeText(Registration.this,"Registered Successfully",Toast.LENGTH_SHORT).show();
                        }else {
                            progressDialog.cancel();
                            Toast.makeText(Registration.this, "Could not register, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void firstLogIn(FirebaseUser user){
        Intent intent = new Intent(this, firstLogIn.class);
        firebaseAuth = FirebaseAuth.getInstance();
        startActivity(intent);
    }
}
