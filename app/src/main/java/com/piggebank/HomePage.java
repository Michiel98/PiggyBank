package com.piggebank;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.piggebank.android.R;
import com.piggebank.paybackPackage.PayBackMain;
import com.piggebank.settingsPackage.MyPreferenceActivity;
import com.piggebank.uitgavenBoeken.uitgaveBoeken;
import com.piggebank.uitgavenOverzicht.uitgaveOverzicht;

/* Homepage is the start activity of the app. We might replace it with a start up screen.
 Still to do:
 Lay-out
 AccountSettings button
*/

public class HomePage extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public void getBooking(View view){
        Intent intent = new Intent(this,uitgaveBoeken.class);
        startActivity(intent);
    }

    public void getSummary(View view){
        Intent intent = new Intent(this,uitgaveOverzicht.class);
        startActivity(intent);
    }

    public void getPayBack(View view){

        Intent intent = new Intent(this,PayBackMain.class); // start slidingTabs
        startActivity(intent);
    }

    public void getSettings(View view){
        Intent intent = new Intent(this, MyPreferenceActivity.class );
        startActivity(intent);
    }


    public void logOut(View view){
        mAuth.signOut();

        Intent intent = new Intent(this, mainLogIn.class);
        startActivity(intent);
    }


}

