package com.piggebank.friendPackage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.piggebank.paybackPackage.PayBackMain;
import com.piggebank.registrationPackage.User;
import com.piggebank.android.R;

public class AddFriend extends PayBackMain {

    EditText newFriend;
    DatabaseReference mRef;
    boolean found;
    boolean dubbel;
    String uidFriend;
    String ownNickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_friend);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        found = false;
        dubbel=false;

        getWindow().setLayout((int) (width * 0.5), (int) (height * 0.3));

        mRef = FirebaseDatabase.getInstance().getReference().child("Users");

        laadEigenNaam();
    }


    public void add(View view) {

        newFriend = (EditText) findViewById(R.id.newFriend);
        String sNewFriend = newFriend.getText().toString();
        String ownUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        laadUsers(sNewFriend);
        checkDubbel(sNewFriend);

        if (found && !dubbel && !(ownNickname==null)) {

            friendDataBank(sNewFriend, uidFriend,ownUid);
            friendDataBank(ownNickname,ownUid,uidFriend);

            found = false;
            dubbel = false;
            uidFriend = null;
        } else if(dubbel) {

          Toast.makeText(AddFriend.this, sNewFriend+" is already a friend", Toast.LENGTH_LONG).show();
          dubbel = false;
        } else{

          Toast.makeText(AddFriend.this, "Friend has not been added, please try again.", Toast.LENGTH_LONG).show();
        }
    }

    public void friendDataBank(String name, String key,String uid) {
        friend vriend = new friend(name, key);
        FirebaseDatabase.getInstance().getReference("Friends")
                .child(uid).push()
                .setValue(vriend).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    Toast.makeText(AddFriend.this, "Friend has been added succesfully.", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(AddFriend.this, PayBackMain.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(AddFriend.this, "Friend has not been added, please try again.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void laadUsers(final String nfriend) {


        Query query = mRef.orderByChild("nickname").equalTo(nfriend);
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {

                    User gebruiker = userSnapshot.getValue(User.class);

                    if (gebruiker.getNickname().equals(nfriend)) {
                        uidFriend = userSnapshot.getKey();
                        found = true;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void laadEigenNaam() {

        mRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    User u = dataSnapshot.getValue(User.class);

                    ownNickname = u.getNickname();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void checkDubbel(String naam){

       for(int i=0; i<getFriendslist().size();i++){

           if(naam.equals(getFriendslist().get(i))){
               dubbel = true;
           }
       }
    }
}
