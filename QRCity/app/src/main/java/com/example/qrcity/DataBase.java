package com.example.qrcity;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class DataBase {

    private static DataBase instance;
    final private FirebaseFirestore db;
    final private FirebaseStorage storage;

    private DataBase() {
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    public static DataBase getInstance() {
        if (instance == null) {
            instance = new DataBase();
        }
        return instance;
    }

    public void addUser(User user){
        // Collection reference
        CollectionReference cr = db.collection("users");

        Map<String,Object> data = new HashMap<>();
        data.put("name",user.getName());
        data.put("contactinfo",user.getContactInfo());
        data.put("totalscore",user.getTotalScore());
        data.put("numcodes", user.getNumCodes());
        cr.document(user.getUserId()).set(data);
    }

    public void getUser(String userId, OnGetUserListener listener){
        CollectionReference cr = db.collection("users");
        DocumentReference dr = cr.document(userId);
        dr.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                User user = new User();
                DocumentSnapshot doc = task.getResult();
                if(doc.exists()){
                    String name= (String) doc.getData().get("name");
                    String contactInfo= (String) doc.getData().get("contactinfo");
                    long totalScore= (long) doc.getData().get("totalscore");
                    long numCodes = (long) doc.getData().get("numcodes");
                    user.setId(userId);
                    user.setName(name);
                    user.setContactInfo(contactInfo);
                    user.set_Total_Score(totalScore);
                    user.set_Num_Codes(numCodes);
                }
                else{
                    user = null;
                }
                listener.getUserListener(user);
            }
        });

    }
}
