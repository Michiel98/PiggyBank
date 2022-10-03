package com.piggebank.uitgavenBoeken;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.piggebank.HomePage;
import com.piggebank.android.R;
import com.piggebank.uitgavenOverzicht.uitgaven;

import java.util.HashMap;
import java.util.Map;

public class Popup extends uitgaveBoeken {

    String category;
    String price;
    String date;
    double totalPrice;
    FirebaseFirestore db;

    FirebaseAuth firebaseAuth;


    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.popupwindow);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*0.8), (int)(height*0.6));

        Bundle bundle = getIntent().getExtras();
        category = bundle.getString("category");
        price = bundle.getString("price");
        date = bundle.getString("date");
        TextView bevesting = (TextView) findViewById(R.id.bevestiging);
        bevesting.setText(getBevesting());
        db = FirebaseFirestore.getInstance();

    }

    public String getBevesting() {
        String bevestiging = "Category: " + category + "\n" + "Price: € " + price + "\n" + "Date: " + date ;
        return bevestiging;
    }
    public void backHome(View view) { // bevestig geklikt
        double prijs = Double.parseDouble(price);
        uitgaven uitgave = new uitgaven(category,prijs,date);

        UitgavenDataBank(category,prijs,date); //zet gegevens in firestore db


        Map<String, Object> dbuitgave = new HashMap<>();
        dbuitgave.put(FirebaseAuth.getInstance().getUid(), uitgave);

        db.collection("Users")
                .add(uitgave)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

        //uitgaveOverzicht.expenses.add(uitgave);

        startActivity(new Intent(this,HomePage.class));
    }

    public void nieuweUitgave(View view) {

        double prijs = Double.parseDouble(price);
        uitgaven uitgave = new uitgaven(category,prijs,date);
        //uitgaveOverzicht.expenses.add(uitgave);

        UitgavenDataBank(category,prijs,date);

        startActivity(new Intent(this,uitgaveBoeken.class));
    }

    public void terugNaarUitgave(View view) {
        uitgaveBoeken.setReturned(true);
        Intent intent = new Intent(Popup.this, uitgaveBoeken.class);
        intent.putExtra("cat", category );
        intent.putExtra("prix", price);
        intent.putExtra("dte", date);
        startActivity(intent);
    }

    public void UitgavenDataBank(String catergory, double price, String date){
        uitgaven uitgave = new uitgaven(catergory,price,date);
        FirebaseDatabase.getInstance().getReference("Expenses")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push()
                .setValue(uitgave).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    Toast.makeText(Popup.this, "Expsense has been added succesfully.", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(Popup.this, "Expense has not been added, please try again.", Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}
