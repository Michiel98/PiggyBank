package com.piggebank.registrationPackage;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.piggebank.settingsPackage.RegistrationData1;
import com.piggebank.android.R;

import java.util.Calendar;

public class InformationFragment extends Fragment {


    private EditText lastName;
    private EditText firstName;
    private TextView birthdate;
    private TextView nextPage;

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    public static final String TAG = "InformationFragment";
    String date;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
       super.onCreate(savedInstanceState);
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_information, container, false);


        return rootView;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lastName = (EditText) getView().findViewById(R.id.lastNameEdit);
        firstName = (EditText) getView().findViewById(R.id.firstNameEdit);
        nextPage = (TextView) getView().findViewById(R.id.next_page1);
        birthdate = (TextView) getView().findViewById(R.id.birthdate);

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month+1;

                Log.d(TAG,"onDateSet: mm/dd/yy: "+year + "/"+month+"/"+day);

                 date= day+"/"+month+"/"+year;
                birthdate.setText(date);

            }
        };
        birthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate();

            }

        });

        checkThisPage();
    }


    public void checkThisPage(){
        if(lastName.getText().toString().length() >1 && firstName.getText().toString().length() >1) {
            nextPage.setVisibility(View.VISIBLE);
        }
        else {
        nextPage.setVisibility(View.INVISIBLE);
        }
    }



    public boolean pageIsReady(){
        checkThisPage();
        if(nextPage.getVisibility() == View.VISIBLE){
            return true;
        }
        else{
            return false;
        }
    }

    private void setDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(getContext(),
                android.R.style.Theme_Holo_Dialog_NoActionBar_MinWidth,mDateSetListener,
                year,month,day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public static InformationFragment newInstance() {
        InformationFragment fragment = new InformationFragment();
        return fragment;
    }

    public RegistrationData1 getData1(){

        return new RegistrationData1(firstName.getText().toString(), lastName.getText().toString(), birthdate.getText().toString());
    }



}
