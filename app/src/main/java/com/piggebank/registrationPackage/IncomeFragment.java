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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.piggebank.settingsPackage.RegistrationData2;
import com.piggebank.android.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class IncomeFragment extends Fragment {



    private EditText nameIncome;
    private EditText income;
    private TextView nextPage;
    private Spinner frequency;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_income, container, false);


        return rootView;
    }
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameIncome = (EditText) getView().findViewById(R.id.name_income_edit);
        income = (EditText) getView().findViewById(R.id.income);
        nextPage = (TextView) getView().findViewById(R.id.next_page2);
        frequency = (Spinner) getView().findViewById(R.id.frequency_select);

        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.frequency));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        frequency.setAdapter(adapter);

        if(frequency != null && income.getText().toString().length() >1){
            nextPage.setVisibility(View.VISIBLE);
        }

        income.setRawInputType(Configuration.KEYBOARD_12KEY);
        income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                income.addTextChangedListener(new TextWatcher(){
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
                            income.removeTextChangedListener(this);

                            String cleanString = s.toString().replaceAll("[$,.]", "");

                            double parsed = Double.parseDouble(cleanString);
                            String formatted = NumberFormat.getCurrencyInstance().format((parsed/100));

                            current = formatted;
                            income.setText(formatted);
                            income.setSelection(formatted.length());

                            income.addTextChangedListener(this);
                        }
                    }
                });
            }
        });


        checkThisPage();
    }

    public boolean pageIsReady() {
        checkThisPage();
        if (nextPage.getVisibility() == View.VISIBLE) {
            return true;
        } else {
            return false;
        }
    }

    public void checkThisPage(){
        if(income.getText().toString().length() >1 && frequency.getSelectedItem().toString().length() >1) {
            nextPage.setVisibility(View.VISIBLE);
        }
        else {
            nextPage.setVisibility(View.INVISIBLE);
        }
    }


    public static IncomeFragment newInstance() {
        IncomeFragment fragment = new IncomeFragment();
        return fragment;
    }


    public RegistrationData2 getData2(){
        double newIncome = Double.parseDouble(income.getText().toString());
        return new RegistrationData2 (nameIncome.getText().toString(), newIncome, frequency.getSelectedItem().toString());
    }

}
