package com.piggebank.uitgavenOverzicht;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.piggebank.android.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class uitgaveOverzicht extends AppCompatActivity {

    ListView resultsListView;

    DatabaseReference databaseExpenses;
    List<uitgaven> uitgavenlijst;
    List<HashMap<String, String>> listItems;
    SimpleAdapter adapter;
    double totalprice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uitgave_overzicht);

        totalprice = 0;
        uitgavenlijst = new ArrayList<>();
        resultsListView = (ListView) findViewById(R.id.ListView);
        databaseExpenses = FirebaseDatabase.getInstance().getReference().child("Expenses")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        listItems = new ArrayList<>();
        adapter = new SimpleAdapter(this,listItems, R.layout.list_item,
                new String[]{"First Line","Second Line"},
                new int[]{R.id.text1,R.id.text2});

        laadUitgaven();

    }

    public void laadUitgaven(){

        databaseExpenses.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                uitgavenlijst.clear();

                for(DataSnapshot expensesSnapshot : dataSnapshot.getChildren()){

                    uitgaven uitgave = expensesSnapshot.getValue(uitgaven.class);

                    uitgavenlijst.add(uitgave);
                }

                display();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void display(){
        for(int i = 0;i<uitgavenlijst.size();i++){

            HashMap<String,String> resultsMap = new HashMap<>();

            String item1 = uitgavenlijst.get(i).getCatergory()+" €"+uitgavenlijst.get(i).getPrice();
            String item2 = uitgavenlijst.get(i).getDate();

            totalprice = totalprice+uitgavenlijst.get(i).getPrice();

            resultsMap.put("First Line",item1);
            resultsMap.put("Second Line",item2);
            listItems.add(resultsMap);
        }

        HashMap<String,String> resultsMap = new HashMap<>();

        String item1 = "Total: €"+totalprice;
        String item2 = "";

        resultsMap.put("First Line",item1);
        resultsMap.put("Second Line",item2);
        listItems.add(resultsMap);

        resultsListView.setAdapter(adapter);
    }
}