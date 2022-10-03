package com.piggebank.paybackPackage;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.piggebank.android.R;
import com.piggebank.friendPackage.friend;

import java.util.ArrayList;
import java.util.List;

public class PayBackMain extends AppCompatActivity implements tab1.OnFragmentInteractionListener,tab2.OnFragmentInteractionListener
        ,tab3.OnFragmentInteractionListener,tab4.OnFragmentInteractionListener{

    DatabaseReference databasePayback;
    DatabaseReference databasefriends;
    static DatabaseReference databaseinfo;
    static List<PayBackObject> paybacklijst;
    static List<friend> friendslist;
    static String nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_back_main);

        databasePayback = FirebaseDatabase.getInstance().getReference().child("Payback")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        paybacklijst = new ArrayList<>();
        databasefriends = FirebaseDatabase.getInstance().getReference().child("Friends")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        friendslist = new ArrayList<>();


        laadUitgaven();
        laadvrienden();

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tablelayout);
        tabLayout.addTab(tabLayout.newTab().setText("Add payback"));
        tabLayout.addTab(tabLayout.newTab().setText("total"));
        tabLayout.addTab(tabLayout.newTab().setText("borrowed"));
        tabLayout.addTab(tabLayout.newTab().setText("loan"));

        final ViewPager viewPager = (ViewPager)findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    public void laadUitgaven(){
        databasePayback.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                paybacklijst.clear();

                for(DataSnapshot pboSnapshot : dataSnapshot.getChildren()){

                    PayBackObject pbo = pboSnapshot.getValue(PayBackObject.class);

                    paybacklijst.add(pbo);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void laadvrienden(){

        databasefriends.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                friendslist.clear();

                for (DataSnapshot friendsSnapshot : dataSnapshot.getChildren()) {

                    friend f = friendsSnapshot.getValue(friend.class);
                    friendslist.add(f);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public static String getNickName(String uid){
        nickname = "Name not found";
        for(int i = 0;i<friendslist.size();i++){
            if(!(friendslist.get(i).getUid()==null) && friendslist.get(i).getUid().equals(uid)){
                nickname = friendslist.get(i).getNickname();
            }
        }
        return nickname;
    }

    public static List<PayBackObject> getPaybacks(){
        return paybacklijst;
    }

    public static List<friend> getFriendslist(){
        return friendslist;
    }
}
