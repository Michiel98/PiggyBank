package com.piggebank.uitgavenBoeken;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.piggebank.android.R;
import com.piggebank.uitgavenOverzicht.uitgaven;

import java.util.ArrayList;
import java.util.Calendar;

public class uitgaveBoeken extends AppCompatActivity {

    String sCategory;
    String sDate;
    String sPrice;

    static ArrayList<String> categorie = new ArrayList<String>();
    Spinner category;
    TextView date;
    EditText price;

    int itemSpinner;
    String positionSpinner;

    DatabaseReference databaseExpenses;

    static boolean returned;

    public static final String TAG = "uitgavenBoeken";
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uitgave_boeken);

        databaseExpenses = FirebaseDatabase.getInstance().getReference().child("Expenses")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        laadCat();

        category = (Spinner) findViewById(R.id.categoryInput);
        date = (TextView) findViewById(R.id.dateInput);
        price =(EditText) findViewById(R.id.priceInput);
        itemSpinner = category.getSelectedItemPosition();
        positionSpinner = (String) category.getItemAtPosition(itemSpinner);

         adapter = makeCategoryListDropDown();


        if(returned) {

            Bundle bundle = getIntent().getExtras();
            if (bundle.getString("cat") != null) {
                category.setSelection(((ArrayAdapter<String>)category.getAdapter()).getPosition(bundle.getString("cat")));
            }
            if (bundle.getString("prix") != null) {
                price.setText(bundle.getString("prix"));
            }
            if (bundle.getString("dte") != null) {
                date.setText(bundle.getString("dte"));
            }

        }

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month+1;

                Log.d(TAG,"onDateSet: mm/dd/yy: "+year + "/"+month+"/"+day);

                sDate = day+"/"+month+"/"+year;
                date.setText(sDate);
            }
        };
        adapter.notifyDataSetChanged();
    }

    public ArrayAdapter<String> makeCategoryListDropDown(){ // maakt spinner van categoriën

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(uitgaveBoeken.this,
                android.R.layout.simple_list_item_1, categorie);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(adapter);
        return adapter;

    }

    public static void addToCategory(String c){
        categorie.add(c);
    }

    public void laadCat(){

        databaseExpenses.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //categorie.clear();

                for (DataSnapshot expensesSnapshot : dataSnapshot.getChildren()) {

                    uitgaven uitgave = expensesSnapshot.getValue(uitgaven.class);

                    String newCat = uitgave.getCatergory();

                    boolean found = false;
                    for (int i = 0; i < categorie.size(); i++) {

                        if (categorie.get(i).equals(newCat)) {
                            found = true;
                        }
                    }
                    if (!found) {
                        categorie.add(newCat);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setDate(View v) {

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(uitgaveBoeken.this,
                android.R.style.Theme_Holo_Dialog_NoActionBar_MinWidth,mDateSetListener,
                year,month,day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public static void setReturned(boolean returned) { //bepalen op wijzig uitgaven gedrukt
        uitgaveBoeken.returned = returned;
    }

    public void addCat(View view){
        Intent intent = new Intent(this, extraCategory.class);
        startActivity(intent);
    }

    public void setOrder(View view){

        /*if(category.getSelectedItem().toString().isEmpty()){
            ((TextView)category.getSelectedView()).setError("Category is required");
        }*/

        if(isEmpty(price)){
            price.setError("Price is required");
        }

        if(date.getText().toString().matches("")){
            date.setError("Date is required");
        }

        else {
            sCategory = category.getSelectedItem().toString();
            sPrice = price.getText().toString();
            returned = false;
            Intent intent = new Intent(this, Popup.class);
            intent.putExtra(Intent.EXTRA_TEXT, "message");
            intent.putExtra("category", sCategory);
            intent.putExtra("date", sDate);
            intent.putExtra("price", sPrice);
            startActivity(intent);
        }
    }
    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }
}
