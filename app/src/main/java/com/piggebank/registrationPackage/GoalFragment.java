package com.piggebank.registrationPackage;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.piggebank.settingsPackage.RegistrationData3;
import com.piggebank.android.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class GoalFragment extends Fragment {

    private EditText nameOfGoal;
    private EditText priceOfGoal;
    private EditText periodInt;
    private Spinner periodValue;
    private CheckBox addToHome;
    private TextView nextPage;
    private boolean addToHomeCheck;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_goal, container, false);
        addToHomeCheck = false;


        return rootView;
    }
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

         nameOfGoal = getView().findViewById(R.id.name_of_goal_edit);
         priceOfGoal = getView().findViewById(R.id.price_of_goal_edit);
         periodInt = getView().findViewById(R.id.freq_number);
         periodValue = getView().findViewById(R.id.period_freq);
         addToHome = getView().findViewById(R.id.add_to_homepage_box);
         nextPage = getView().findViewById(R.id.next_page3);

        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.period));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        periodValue.setAdapter(adapter);
        addToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setAddToHome();
            }
        });

        priceOfGoal.setRawInputType(Configuration.KEYBOARD_12KEY);
        priceOfGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                priceOfGoal.addTextChangedListener(new TextWatcher(){
                    DecimalFormat dec = new DecimalFormat("0.00");
                    @Override
                    public void afterTextChanged(Editable arg0) {
                    }
                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }
                    private String current = "";
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if(!s.toString().equals(current)){
                            priceOfGoal.removeTextChangedListener(this);

                            String cleanString = s.toString().replaceAll("[$,.]", "");

                            double parsed = Double.parseDouble(cleanString);
                            String formatted = NumberFormat.getCurrencyInstance().format((parsed/100));

                            current = formatted;
                            priceOfGoal.setText(formatted);
                            priceOfGoal.setSelection(formatted.length());

                            priceOfGoal.addTextChangedListener(this);
                        }
                    }
                });
            }
        });

         checkThisPage();

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

    public void checkThisPage(){
        if(nameOfGoal.getText().toString().length() >1 && priceOfGoal.getText().toString().length() >1 && periodInt.getText().toString().length() > 1 && periodValue.getSelectedItem().toString().length() > 1) {
            nextPage.setVisibility(View.VISIBLE);
        }
        else {
            nextPage.setVisibility(View.INVISIBLE);
        }
    }

    public static GoalFragment newInstance() {
        GoalFragment fragment = new GoalFragment();
        return fragment;
    }

    public boolean setAddToHome(){
        addToHomeCheck = !addToHomeCheck;
        return addToHomeCheck;
    }


    public RegistrationData3 getData3(){
       return new RegistrationData3(
               nameOfGoal.getText().toString(),
               Double.parseDouble(priceOfGoal.getText().toString()),
               Integer.parseInt(periodInt.getText().toString()),
               periodValue.getSelectedItem().toString(),
               setAddToHome());
    }


}
