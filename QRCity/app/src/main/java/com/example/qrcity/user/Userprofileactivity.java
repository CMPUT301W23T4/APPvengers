package com.example.qrcity.user;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.qrcity.R;
import com.example.qrcity.qr.MainActivity;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class Userprofileactivity extends AppCompatActivity implements Removeprofile.OnFragmentInteractionListener, editprofile.OnFragmentInteractionListener , Observer {

    private User user;
    private TextView userName;
    private TextView contactInfo;
    private TextView score;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        String android_ID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        userName = findViewById(R.id.UserName);
        contactInfo = findViewById(R.id.ContactInfo);
        score = findViewById(R.id.UserScore);
        userName.setText(user.getName());
        contactInfo.setText(user.getContactInfo());
        db = FirebaseFirestore.getInstance();
        final String TAG = "what you want to leave here";
        final CollectionReference collectionReference = db.collection("Users");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
            FirebaseFirestoreException error) {
                HashMap<String, User> userDataList = new HashMap<>();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    String userId = new String();
                    Map<String,Object> user = doc.getData();
                    User user1 = new User();
                    boolean success = false;
                    for (Map.Entry<String, Object> pair : user.entrySet()) {

                        String key = pair.getKey();

                        if (pair.getKey().equals("userId")) {
                            user1.setId((String) pair.getValue());
                            userId = (String) pair.getValue();
                        }
                        if (pair.getKey().equals("name")) {
                            user1.setName((String) pair.getValue());
                        }
                        if (pair.getKey().equals("contactInfo")){
                            user1.setContactInfo((String) pair.getValue());
                        }
                    }
                    if(true) {
                        userDataList.put(userId, user1);
                        Log.d(TAG, "User " + userId + " downloaded");
                    }
                }

                if (!userDataList.isEmpty()){
                        user = userDataList.get(android_ID);
                    if(user!=null){
                        userName.setText(user.getName());
                        contactInfo.setText(user.getContactInfo());
                        score.setText(String.valueOf(user.getTotalScore()));
                    }
                }
            }

        });

    }


    @Override
    public void onRemoveOKPressed() {
        Intent intent = new Intent(this, MainActivity.class);
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
