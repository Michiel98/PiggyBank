package com.piggebank.paybackPackage;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.piggebank.android.R;
import com.piggebank.friendPackage.AddFriend;
import com.piggebank.friendPackage.friend;
import com.piggebank.registrationPackage.InformationFragment;

import java.util.ArrayList;
import java.util.Calendar;

//import android.app.Fragment;

public class tab3 extends Fragment {

        String sDate;

        static ArrayList<String> friends = new ArrayList<String>();
        static ArrayList<String> friendsKey = new ArrayList<String>();
        Spinner vrienden;
        Spinner vrienden2;
        TextView date;
        EditText price;
        Button pb;
        Button add;
        Button del;

        int itemSpinner;
        int itemSpinner2;
        String positionSpinner;
        String positionSpinner2;
        String UidFriend;

        DatabaseReference databaseFriends;
        DatabaseReference databaseNickname;
        DatabaseReference databasePayback;
        ProgressDialog progressDialog;

        public static final String TAG = "payback";
        private DatePickerDialog.OnDateSetListener mDateSetListener;
        ArrayAdapter<String> adapter;
        ArrayAdapter<String>adapter2;


        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            ViewGroup rootView = (ViewGroup) inflater.inflate(
                    R.layout.fragment_tab3, container, false);

            return rootView;
        }

        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            databaseFriends = FirebaseDatabase.getInstance().getReference().child("Friends")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            databaseNickname = FirebaseDatabase.getInstance().getReference().child("Users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            databasePayback = FirebaseDatabase.getInstance().getReference().child("Payback")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            progressDialog = new ProgressDialog(getContext());

            laadvrienden();

            vrienden = getView().findViewById(R.id.friendInput);
            date = getView().findViewById(R.id.dateInput);
            price = getView().findViewById(R.id.priceInput);
            itemSpinner = vrienden.getSelectedItemPosition();
            positionSpinner = (String) vrienden.getItemAtPosition(itemSpinner);
            pb = getView().findViewById(R.id.schuldBoeken);
            add = getView().findViewById(R.id.extrafriend);

            vrienden2 = getView().findViewById(R.id.friendInput2);
            itemSpinner2 = vrienden2.getSelectedItemPosition();
            positionSpinner2 = (String) vrienden2.getItemAtPosition(itemSpinner2);
            del = getView().findViewById(R.id.deletepayback);

            adapter = makeCategoryListDropDown();
            adapter2 = makeCategoryListDropDown2();
            UidFriend = null;

            mDateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    month = month + 1;

                    Log.d(TAG, "onDateSet: mm/dd/yy: " + year + "/" + month + "/" + day);

                    sDate = day + "/" + month + "/" + year;
                    date.setText(sDate);
                }
            };

            date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDate(v);

                }

            });

            pb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bevestig(v);

                }

            });

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addfriend(v);

                }

            });

            del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delete(v);

                }

            });

            adapter.notifyDataSetChanged();
            adapter2.notifyDataSetChanged();

        }


    public void setDate(View v) {

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

        public ArrayAdapter<String> makeCategoryListDropDown(){ // maakt spinner van vrienden

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_list_item_1, friends);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            vrienden.setAdapter(adapter);
            return adapter;

        }

    public ArrayAdapter<String> makeCategoryListDropDown2(){ // maakt spinner van vrienden

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, friends);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vrienden2.setAdapter(adapter2);
        return adapter2;

    }

        public void addfriend(View view){
            Intent intent = new Intent(getContext(), AddFriend.class);
            startActivity(intent);
        }



        public void laadvrienden(){

            databaseFriends.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    friends.clear();
                    friendsKey.clear();

                    for (DataSnapshot friendsSnapshot : dataSnapshot.getChildren()) {

                        friend f = friendsSnapshot.getValue(friend.class);

                        friends.add(f.getNickname());
                        friendsKey.add(f.getUid());

                    }
                    adapter.notifyDataSetChanged();
                    adapter2.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        public static ArrayList getFriends(){
            return friends;
        }

    public void delete(View v) {
        String friend = vrienden2.getSelectedItem().toString();
        String UidPerson2 = getuid(friend);
        String UidPerson1 = FirebaseAuth.getInstance().getCurrentUser().getUid();

        deleteInDb(UidPerson1,UidPerson2);
        deleteInDb(UidPerson2,UidPerson1);

        Toast.makeText(getContext(),"Payback is removed.",Toast.LENGTH_LONG).show();


    }

    public void deleteInDb(String uid1, final String uid2){
        DatabaseReference delpayback = FirebaseDatabase.getInstance().getReference("Payback").child(uid1);

        delpayback.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot :  snapshot.getChildren())
                {
                    if(dataSnapshot.child("uidFriend").getValue().toString().equals(uid2))
                    {
                        dataSnapshot.getRef().setValue(null);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

        public void bevestig(View view){
            if(price.getText().toString().trim().length() == 0){
                price.setError("Price is required");
            }
            if(date.getText().toString().matches("")){
                date.setError("Date is required");
            }
            else {
                preconditions();
            }
        }

        public void preconditions(){

            String friend = vrienden.getSelectedItem().toString();
            String UidPerson2 = getuid(friend);
            //checkUpdate(UidPerson2);
            String UidPerson1 = FirebaseAuth.getInstance().getCurrentUser().getUid();
            double prijs = Double.parseDouble(price.getText().toString());
            String datum = date.getText().toString();

                addPayback(UidPerson1, UidPerson2, datum, prijs, true); // payback in db schuldige geld
                addPayback(UidPerson2, UidPerson1, datum, prijs, false); // payback in db ontvanger geld

        }

        public void addPayback(String Uidreciever, String friend, String date, double price, boolean borrowed){

            PayBackObject pbo = new PayBackObject(date,price,(!borrowed),friend);
            FirebaseDatabase.getInstance().getReference("Payback")
                    .child(Uidreciever).push()
                    .setValue(pbo)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                Toast.makeText(getContext(), "Payback has been added succesfully.", Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(getContext(), "Payback has not been added, please try again.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        }

        public String getuid(String vriend){

            for(int i = 0;i<friends.size();i++){

                if(friends.get(i).equals(vriend)){
                    UidFriend = friendsKey.get(i).toString();
                }
            }
            return UidFriend;
        }

        public interface OnFragmentInteractionListener {
            void onFragmentInteraction(Uri uri);
        }

        public static InformationFragment newInstance() {
            InformationFragment fragment = new InformationFragment();
            return fragment;
        }
    }


