package com.piggebank.settingsPackage;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.widget.DatePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.piggebank.android.R;

import java.util.Calendar;

import static com.piggebank.paybackPackage.tab3.TAG;

public class MyPreferenceActivity extends PreferenceActivity {


    static String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();

    }

    public static class MyPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings);


            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot dataSnapshot) {

                    //Information
                    findPreference("nickname").setSummary(dataSnapshot.child("nickname").getValue().toString());
                    findPreference("firstName").setSummary(dataSnapshot.child("Information").child("firstName").getValue().toString());
                    findPreference("lastName").setSummary(dataSnapshot.child("Information").child("lastName").getValue().toString());
                    findPreference("email").setSummary(dataSnapshot.child("email").getValue().toString());
                    findPreference("birthdate").setSummary(dataSnapshot.child("Information").child("birthdate").getValue().toString());

                    //Income
                    findPreference("nameOfIncome").setSummary(dataSnapshot.child("Income").child("nameOfIncome").getValue().toString());
                    findPreference("priceOfIncome").setSummary(dataSnapshot.child("Income").child("priceOfIncome").getValue().toString());
                    findPreference("frequencyOfIncome").setSummary(dataSnapshot.child("Income").child("frequencyOfIncome").getValue().toString());

                    //Goal
                    findPreference("nameOfGoal").setSummary(dataSnapshot.child("Goal").child("nameOfGoal").getValue().toString());
                    findPreference("priceOfGoal").setSummary(dataSnapshot.child("Goal").child("priceOfGoal").getValue().toString());
                    findPreference("numberOfPeriod").setSummary(dataSnapshot.child("Goal").child("numberOfPeriod").getValue().toString());
                    findPreference("period").setSummary(dataSnapshot.child("Goal").child("period").getValue().toString());
                    CheckBoxPreference checker = (CheckBoxPreference) findPreference("addToHome");
                    if(dataSnapshot.child("Goal").child("addToHome").getValue().toString() == "false"){
                        checker.setChecked(false);
                    }
                    else checker.setChecked(true);

                    //Profile picture
                    //findPreference("picture_image").setSummary(dataSnapshot.child("Profile Picture").getValue().toString());
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            Preference nickname = findPreference("nickname");
            Preference firstname = findPreference("firstName");
            Preference lastname = findPreference("lastName");
            Preference email = findPreference("email");
            final Preference birthdate = findPreference("birthdate");
            Preference nameOfIncome = findPreference("nameOfIncome");
            Preference priceOfIncome = findPreference("priceOfIncome");
            Preference frequencyOfIncome= findPreference("frequencyOfIncome");
            Preference nameOfGoal = findPreference("nameOfGoal");
            Preference priceOfGoal = findPreference("priceOfGoal");
            Preference numberOfPeriod = findPreference("numberOfPeriod");
            Preference period = findPreference("period");
            Preference checkBox = findPreference("addToHome");

            birthdate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int day) {
                            month = month+1;
                            Log.d(TAG,"onDateSet: mm/dd/yy: "+year + "/"+month+"/"+day);
                            date= day+"/"+month+"/"+year;
                            birthdate.setSummary(date);
                            ref.child("Information").child("birthdate").setValue(date);
                        }
                    };

                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                            android.R.style.Theme_Holo_Dialog_NoActionBar_MinWidth,dateSetListener,
                            year,month,day);
                    datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    datePickerDialog.show();

                    return false;
                }
            });


            Preference.OnPreferenceChangeListener onPreferenceChangeListener = new Preference.OnPreferenceChangeListener() {
                @TargetApi(Build.VERSION_CODES.O)
                @Override
                public boolean onPreferenceChange(final Preference preference, final Object newValue) {
                    if(preference == findPreference("nickname")){
                        ref.child("nickname").setValue(newValue);
                    }
                    else if(preference == findPreference("email")){
                        ref.child("email").setValue(newValue);
                    }
                    else if(preference == findPreference("firstName")){
                        ref.child("Information").child("firstName").setValue(newValue);
                    }
                    else if(preference == findPreference("lastName") ){
                        ref.child("Information").child("lastName").setValue(newValue);
                    }
                    /*else if(preference == findPreference("birthdate")){
                        ref.child("Information").child("birthdate").setValue(newValue);
                        birthdate.setSummary(newValue.toString());
                    }
                    */
                    else if(preference == findPreference("nameOfIncome") ){
                        ref.child("Income").child("nameOfIncome").setValue(newValue);
                    }
                    else if(preference == findPreference("priceOfIncome") ){
                        ref.child("Income").child("priceOfIncome").setValue(newValue);
                    }
                    else if(preference == findPreference("frequencyOfIncome") ){ //todo: spinner
                        ref.child("Income").child("frequencyOfIncome").setValue(newValue);
                    }
                    else if(preference == findPreference("nameOfGoal") ){
                        ref.child("Goal").child("nameOfGoal").setValue(newValue);
                    }
                    else if(preference == findPreference("priceOfGoal") ){
                        ref.child("Goal").child("priceOfGoal").setValue(newValue);
                    }
                    else if(preference == findPreference("numberOfPeriod") ){
                        ref.child("Goal").child("numberOfPeriod").setValue(newValue);
                    }
                    else if(preference == findPreference("period") ){ //todo: spinner
                        ref.child("Goal").child("period").setValue(newValue);
                    }else if(preference == findPreference("addToHome") ){
                        ref.child("Goal").child("addToHome").setValue(newValue);
                    }

                    if(preference != birthdate) {
                        preference.setSummary(newValue.toString());
                    }

                    return false;
                }
            };
            //information

            nickname.setOnPreferenceChangeListener(onPreferenceChangeListener);

            firstname.setOnPreferenceChangeListener(onPreferenceChangeListener);

            lastname.setOnPreferenceChangeListener(onPreferenceChangeListener);

            email.setOnPreferenceChangeListener(onPreferenceChangeListener);





            birthdate.setOnPreferenceChangeListener(onPreferenceChangeListener);
            //income

            nameOfIncome.setOnPreferenceChangeListener(onPreferenceChangeListener);

            priceOfIncome.setOnPreferenceChangeListener(onPreferenceChangeListener);

            frequencyOfIncome.setOnPreferenceChangeListener(onPreferenceChangeListener);
            //Goal

            nameOfGoal.setOnPreferenceChangeListener(onPreferenceChangeListener);

            priceOfGoal.setOnPreferenceChangeListener(onPreferenceChangeListener);

            numberOfPeriod.setOnPreferenceChangeListener(onPreferenceChangeListener);
            period.setOnPreferenceChangeListener(onPreferenceChangeListener);

            checkBox.setOnPreferenceChangeListener(onPreferenceChangeListener);
            //profilePicture





        }
    }
}


