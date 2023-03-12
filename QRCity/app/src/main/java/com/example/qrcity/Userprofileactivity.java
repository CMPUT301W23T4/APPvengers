package com.example.qrcity;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;

//import com.google.firebase.firestore.CollectionReference;
//import com.google.firebase.firestore.EventListener;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.FirebaseFirestoreException;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.google.firebase.firestore.QuerySnapshot;

import java.util.Observable;
import java.util.Observer;
public class Userprofileactivity extends AppCompatActivity implements Removeprofile.OnFragmentInteractionListener, editprofile.OnFragmentInteractionListener , Observer {

    private User user;
    private TextView userName;
    private TextView contactInfo;
    private TextView score;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.user_profile);
        String android_ID = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        user = new User(android_ID,"name");
        userName = findViewById(R.id.UserName);
        contactInfo = findViewById(R.id.ContactInfo);
        score = findViewById(R.id.UserScore);
        userName.setText(user.getName());
    }
    


    @Override
    public void onRemoveOKPressed() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onEditOKPressed(User user) {
        userName.setText(user.getName());
        contactInfo.setText(user.getContactInfo());
        this.user = user;
    }

    @Override
    public void update(Observable o, Object arg) {
        userName.setText(user.getName());
        contactInfo.setText(user.getContactInfo());
        this.user = user;
    }
    public void editButton(View view){
        new editprofile(user).show(getSupportFragmentManager(),"EDIT");
    }
    public void RemoveProfile(View view){
        new Removeprofile().show(getSupportFragmentManager(),"Try_Remove");
    }

}
