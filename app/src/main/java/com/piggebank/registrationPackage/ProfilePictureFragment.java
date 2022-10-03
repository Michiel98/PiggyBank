package com.piggebank.registrationPackage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.piggebank.HomePage;
import com.piggebank.settingsPackage.RegistrationData4;
import com.piggebank.android.R;

import java.io.FileNotFoundException;

import static android.app.Activity.RESULT_OK;

public class ProfilePictureFragment extends Fragment {

    private ImageView profilePicture;
    private Button addProfilePicture;
    private TextView textTargetUri;
    private CheckBox addLater;
    private Button save;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_profile_picture, container, false);

        return rootView;
    }
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profilePicture = getView().findViewById(R.id.profile_picture);
        addProfilePicture =  getView().findViewById(R.id.add_picture);
        textTargetUri = getView().findViewById(R.id.targeturi);
        save = getView().findViewById(R.id.save);
        addLater = getView().findViewById(R.id.add_later_box);

        if(allSet()){
            save.setVisibility(View.VISIBLE);
        }
        addProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(firstLogIn.getContextOfApplication(), HomePage.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            Uri targetUri = data.getData();
            textTargetUri.setText(targetUri.toString());
            Bitmap bitmap;
            try {
                Context applicationContext = firstLogIn.getContextOfApplication();
                bitmap = BitmapFactory.decodeStream(applicationContext.getContentResolver().openInputStream(targetUri));
                profilePicture.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


    }

    private boolean allSet() {

        if(profilePicture.getDrawable()!= Drawable.createFromPath("com_facebook_profile_picture_blank_square") || addLater.isChecked()){
            return true;
        }
        else { return false;}
    }

    public boolean pageIsReady(){
        if(save.getVisibility() == View.VISIBLE){
            return true;
        }
        else{
            return false;
        }
    }
    public static ProfilePictureFragment newInstance() {
        ProfilePictureFragment fragment = new ProfilePictureFragment();
        return fragment;
    }

    //Use url to write profilepic to firebase

    public RegistrationData4 getData4(){
        return new RegistrationData4(profilePicture, addLater.isChecked());
    }



}
