package com.piggebank.registrationPackage;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.piggebank.android.R;

import java.util.Timer;
import java.util.TimerTask;

public class firstLogIn extends AppCompatActivity {


    String [] fragments = {"Information", "Income", "Goal", "Personalize"};
    TextView fragmentName;

    PageAdapter pageAdapter;
    CustomViewPager viewPager;
    GestureDetector gestureDector;

    int tabCount;

    LinearLayout mDotsLayout;
    private TextView[] mDots;
    private int fragCounter;
    private boolean canScroll;


    @Override
    protected void onStart(){
        super.onStart();
        addDotsIndicator(0);

    }
    // a static variable to get a reference of our application context
    public static Context contextOfApplication;

    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_log_in);
        tabCount = 4;

        fragmentName = findViewById(R.id.fragment_text);
        mDotsLayout = findViewById(R.id.mDotsLayout);

        fragmentName.setText(fragments[0]);
        fragCounter = -1;

        contextOfApplication = getApplicationContext();

        viewPager =  findViewById(R.id.cviewPager);
        pageAdapter = new PageAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(pageAdapter);
        viewPager.setCurrentItem(0);
        setCurrentItemPosition(0);

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                handlePagingEnable();
            }

        }, 2000, 100);//put here time 1000 milliseconds=1 second

        gestureDector = new GestureDetector(this, new OnGestureListener() {
            private final int minDistance = 1;

            @Override
            public boolean onDown(MotionEvent event) {
                canScroll = true;
                return true;
            }

            @Override
            public void onShowPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float distanceX, float distanceY) {
                if((motionEvent.getX() > motionEvent1.getX()) && (distanceX > minDistance) && getCurrentItemPosition() > fragCounter){
                    if(handlePagingEnable()) {
                        handleScrollToNextPage(getCurrentItemPosition());
                    }
                    else{
                        viewPager.setCurrentItem(getCurrentItemPosition());
                        Toast.makeText(contextOfApplication, "Fill in all components to continue", Toast.LENGTH_LONG).show();
                    }
                    return true;

                }
                if((motionEvent.getX() < motionEvent1.getX()) && (distanceX < -minDistance) && fragCounter < getCurrentItemPosition()){
                    if(handlePagingEnable()) {
                        handleScrollToPreviousPage();
                        if (getCurrentItemPosition() > 0) {
                            setCurrentItemPosition(getCurrentItemPosition() - 1);

                        }
                    }
                    return true;
                }
                else{
                    fragCounter--;
                }
                return true;
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if(e2.getActionMasked() == MotionEvent.ACTION_MOVE){
                    if((e1.getX() > e2.getX()) && (velocityX > minDistance)){
                        handlePagingEnable();
                        handleScrollToNextPage(getCurrentItemPosition());

                    }
                    if((e1.getX() < e2.getX()) && (velocityX < -minDistance)){
                        handlePagingEnable();
                        handleScrollToPreviousPage();

                    }
                }
                return true;
            }
        });

        viewPager.setGestureDetector(gestureDector);
    }

    private void handleScrollToPreviousPage() {
        Toast.makeText(contextOfApplication, "You can change your settings later", Toast.LENGTH_LONG).show();
    }

    private boolean handlePagingEnable(){
        if(checkIfReady() && fragmentName.getText().toString() != "Personalize"){
            viewPager.setPagingEnabled(true);
            return true;
        }
        else{
            viewPager.setPagingEnabled(false);
            return false;
        }
    }

    private void handleScrollToNextPage(int currentItemPosition) {
        sendData();
        viewPager.setPageTransformer(false, new FlipPageViewTransformer());
        pageAdapter.getItem(currentItemPosition+1);
        viewPager.setCurrentItem(currentItemPosition+1);

        fragmentName.setText(fragments[currentItemPosition +1 ]);
        addDotsIndicator(currentItemPosition +1 );

        setCurrentItemPosition(currentItemPosition + 1);

        fragCounter = fragCounter + 2;

    }

    private boolean checkIfReady() {

        boolean isReady =false;
        android.support.v4.app.Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.cviewPager + ":" + viewPager.getCurrentItem());
        if (page != null) {
            switch (viewPager.getCurrentItem()) {
                case 0:
                    InformationFragment fragment1 = (InformationFragment) page;
                    isReady =  fragment1.pageIsReady();
                    break;

                case 1:
                    IncomeFragment fragment2 = (IncomeFragment) page;
                    isReady =  fragment2.pageIsReady();
                    break;

                case 2:
                    GoalFragment fragment3 = (GoalFragment) page;
                    isReady =  fragment3.pageIsReady();
                    break;

                case 3:
                    ProfilePictureFragment fragment4 = (ProfilePictureFragment) page;
                    isReady = fragment4.pageIsReady();
                    break;
            }
        }
        return isReady;

    }

    private void sendData() {
        //todo: implement inheritance to avoid duplicate code for writing to database
        android.support.v4.app.Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.cviewPager + ":" + viewPager.getCurrentItem());
        if (page != null) {
            switch (viewPager.getCurrentItem()) {
                case 0:
                    InformationFragment fragment1 = (InformationFragment) page;
                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(fragmentName.getText().toString())
                            .setValue(fragment1.getData1()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                Toast.makeText(firstLogIn.this, "data has been added succesfully.", Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(firstLogIn.this, "data has not been added, please try again.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    break;

                case 1:
                    IncomeFragment fragment2 = (IncomeFragment) page;

                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(fragmentName.getText().toString())
                            .setValue(fragment2.getData2()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                Toast.makeText(firstLogIn.this, "data has been added succesfully.", Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(firstLogIn.this, "data has not been added, please try again.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    break;

                case 2:
                    GoalFragment fragment3 = (GoalFragment) page;

                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(fragmentName.getText().toString())
                            .setValue(fragment3.getData3()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                Toast.makeText(firstLogIn.this, "data has been added succesfully.", Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(firstLogIn.this, "data has not been added, please try again.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    break;

                case 3:
                    ProfilePictureFragment fragment4 = (ProfilePictureFragment) page;
                    //todo: Write profilepicture to firebase database
                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Profile Picture")
                            .setValue(fragment4.getData4());
                        break;
                    }

            }
        }

    public void addDotsIndicator(int position){
        mDotsLayout.removeAllViewsInLayout();
        mDots = new TextView[4];
        for(int i = 0 ; i <mDots.length; i++){
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.gray));

            mDotsLayout.addView(mDots[i]);
        }
        if(mDots.length > 0){
            mDots[position].setTextColor(getResources().getColor(R.color.black));
            mDots[position].setTextSize(50);
        }

    }

    public int getCurrentItemPosition() {
        return viewPager.getCurrentItem();
    }

    public void setCurrentItemPosition(int pos){
        viewPager.setCurrentItem(pos);
    }
}

