package com.example.qrcity.user;


import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.qrcity.R;
import com.example.qrcity.qr.DataBase;
import com.example.qrcity.qr.MainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class Userprofileactivity extends AppCompatActivity implements Removeprofile.OnFragmentInteractionListener, editprofile.OnFragmentInteractionListener , Observer {

    private User current_user;
    private TextView userName;
    private TextView contactInfo;
    private TextView score;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private DataBase dataBase = new DataBase();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        String android_ID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        getUserByAndroidId(android_ID);

        userName = findViewById(R.id.UserName);
        contactInfo = findViewById(R.id.ContactInfo);
        score = findViewById(R.id.UserScore);

        if(current_user != null){
            userName.setText(current_user.getName());
            contactInfo.setText(current_user.getContactInfo());
            score.setText((int) current_user.getTotalScore());
        }

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
                        if (pair.getKey() == "userCodeList") {
                            user1.setCodeList((List) pair.getValue());
                        }
                        if (pair.getKey().equals("totalScore")) {
                            Integer totalScore;
                            totalScore = ((Long) pair.getValue()).intValue();
                            user1.setTotalScore(totalScore);
                        }
                    }
                    if(true) {
                        userDataList.put(userId, user1);
                        Log.d(TAG, "User " + userId + " downloaded");
                    }
                }

                if (!userDataList.isEmpty()){
                    current_user = userDataList.get(android_ID);
                    if(current_user!=null){
                        userName.setText(current_user.getName());
                        contactInfo.setText(current_user.getContactInfo());
                        score.setText(String.valueOf(current_user.getTotalScore()));
                    }
                }
            }

        });

    }
    public void getUserByAndroidId(String userId){
        db.collection("Users").document(userId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            User user = documentSnapshot.toObject(User.class);
                            current_user = user;
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Error getting document: " + e);
                    }
                });
    }

    public void updateUser(User user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = user.getUserId();

        db.collection("Users").document(userId).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "User updated successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Error updating user: " + e);
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
        //userName.setText(current_user.getName());
        //contactInfo.setText(current_user.getContactInfo());
        //this.user = user;
        updateUser(user);
        //dataBase.editUser(current_user);
    }

    @Override
    public void update(Observable o, Object arg) {
        userName.setText(current_user.getName());
        contactInfo.setText(current_user.getContactInfo());
    }
    public void editButton(View view){
        new editprofile(current_user).show(getSupportFragmentManager(),"EDIT");
    }
    public void RemoveProfile(View view){
        new Removeprofile().show(getSupportFragmentManager(),"Try_Remove");
    }
}
