package com.piggebank.uitgavenBoeken;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;

import com.piggebank.android.R;

public class extraCategory extends uitgaveBoeken {

    EditText newCat;

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.extracategory);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*0.5),(int)(height*0.3));

    }


    public void addCategory(View view) {

        newCat = (EditText) findViewById(R.id.newCat);
        String sNewCat = newCat.getText().toString();
        uitgaveBoeken.addToCategory(sNewCat);
        Intent intent = new Intent(extraCategory.this, uitgaveBoeken.class);
        startActivity(intent);
    }
}
