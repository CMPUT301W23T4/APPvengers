package com.example.qrcity;

import android.graphics.Bitmap;
import android.util.Base64;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
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

    public void getUsers(OnGetUsersListener listener){
        CollectionReference cr = db.collection("users");
        cr.get().addOnCompleteListener(task -> {
            ArrayList<String> userIds = new ArrayList<>();
            if(task.isSuccessful()){
                for(QueryDocumentSnapshot doc: task.getResult()){
                    String userid = doc.getId();
                    userIds.add(userid);
                }
            }
            listener.getUsersListener(userIds);
        });
    }

    public void addCode(ScannableCode code, String hash){
        CollectionReference cr = db.collection("codes");
        DocumentReference dr = cr.document(hash);
        Map<String, Object> data = new HashMap<>();
        data.put("score",code.getScore());
        data.put("comment",code.getComment());
        data.put("location",code.getLocation());
        data.put("name",code.getName());
        if (code.getPhoto() == null) {
            data.put("photo", null);
        } else {
            /* https://programmer.ink/think/how-to-use-bitmap-to-store-pictures-into-database.html
             * Author: bretx
             */
            Bitmap bitmap = code.getPhoto();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bytes = stream.toByteArray();

            data.put("photo", Base64.encodeToString(bytes, Base64.DEFAULT));
        }

        dr.set(data);
    }
}
