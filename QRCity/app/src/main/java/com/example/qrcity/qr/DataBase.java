package com.example.qrcity.qr;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.qrcity.user.OnGetUserListener;
import com.example.qrcity.user.OnGetUsersListener;
import com.example.qrcity.user.User;
import com.example.qrcity.user.UserCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataBase {

    private static DataBase instance;
    final private FirebaseFirestore db;
    final private FirebaseStorage storage;
    CollectionReference collectionReference;
    CollectionReference ownerCollection;
    CollectionReference codeCollection;
    StorageReference photoColletion;

    final String TAG = "what to put here";


    public DataBase() {
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        collectionReference = db.collection("Users");
        ownerCollection = db.collection("Owners");
        photoColletion = storage.getReference();
        codeCollection = db.collection("ScannableCodes");

    }


    public static DataBase getInstance() {
        if (instance == null) {
            instance = new DataBase();
        }
        return instance;
    }
    public ArrayList<User> getUsersByCode(String codeId) {
        ArrayList<User> userDataList = new ArrayList<>();
        //snapshot listener to watch for changes in the database
        db.collection("Users")
                .whereArrayContains("userCodeList", codeId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                User user = new User();
                                Map<String, Object> userData = document.getData();
                                // Get all fields from the current document and construct a User
                                for (Map.Entry<String, Object> pair : userData.entrySet()) {

                                    String key = pair.getKey();
                                    if (key.equals("userId")) {
                                        user.setId((String) pair.getValue());
                                    }
                                    if (pair.getKey().equals("name")) {
                                        user.setName((String) pair.getValue());
                                    }
                                    if (pair.getKey().equals("contactInfo")) {
                                        user.setContactInfo((String) pair.getValue());
                                    }
                                    if (pair.getKey().equals("userCodeList")) {
                                        user.setCodeList((List<Map>) pair.getValue());
                                    }
                                }
                                // Add the user from the current document to userDataList
                                if (user.getUserId() != null && user.getName() != null && user.getUserCodeList() != null
                                        && user.getContactInfo() != null) {
                                    userDataList.add(user);
                                    Log.d(TAG, "User " + user.getUserId() + " downloaded");
                                } else {
                                    Log.d(TAG, "User " + user.getUserId() + " not downloaded");

                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return userDataList;
    }

    public HashMap<String, User> getAllUserData() {
        HashMap<String, User> userDataList = new HashMap<>();
        //snapshot listener to watch for changes in the database
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                // Iterate over all documents in the collection
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

                    List<Map> userCodeList = null;
                    String userId = new String();
                    User user = new User();
                    Map<String, Object> userData = doc.getData();
                    boolean success = false;
                    // Get all fields from the current document and construct a User
                    for (Map.Entry<String, Object> pair : userData.entrySet()) {
                        String key = pair.getKey();

                        if (key.equals("userId")) {
                            user.setId((String) pair.getValue());
                            userId = (String) pair.getValue();
                        }
                        if (key.equals("name")) {
                            user.setName((String) pair.getValue());
                        }
                        if (key.equals("contactInfo")) {
                            user.setContactInfo((String) pair.getValue());
                        }
                        if (key == "userCodeList") {
                            user.setCodeList((List<Map>) pair.getValue());
                        }
                        if (key.equals("numCodes")) {
                            Integer numCodes;
                            numCodes = ((Long) pair.getValue()).intValue();
                            user.setNumCodes(numCodes);
                        }
                        if (key.equals("totalScore")) {
                            Integer totalScore;
                            totalScore = ((Long) pair.getValue()).intValue();
                            user.setTotalScore(totalScore);
                        }
                    }
                    userDataList.put(userId, user);
                    Log.d(TAG, "User downloaded");
                    Log.d(TAG, "Server document data: " + doc.getData());
                }
            }
        });
        return userDataList;
    }
    public void addCode(ScannableCode code) {
        // When a new code is added is must be added to the ScannableCodes collection and appended
        // to the codeList of the current user document from the Users collection

        // Add to ScannableCodes collection
        // First check if the code already exists
        Map<String, Object> codeData = new HashMap<>();
        codeData.put("codeName", code.getName());
        codeData.put("codeScore", code.getScore());
        codeData.put("Location", code.getLocation());
        codeData.put("Comment",code.getComment());
        if (code.getPhoto() == null) {
            codeData.put("photo", null);
        } else {
            Bitmap bitmap = code.getPhoto();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bytes = stream.toByteArray();
            //value of key "photo" has a type of Base64 NOT Bitmap
            //call converToBitmap to convert
            codeData.put("photo", Base64.encodeToString(bytes, Base64.DEFAULT));
        }
        codeCollection
                .document(code.getId())
                .set(codeData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //if data is successfully uploaded
                        Log.d(TAG, "Code " + code.getId() + "  successfully uploaded");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //if data upload fails
                        Log.d(TAG, "User " + code.getId() + " data failed to upload: " + e.toString());
                    }
                });
    }
    public ScannableCode getCode(String id) {
        ScannableCode code = new ScannableCode();
        DocumentReference docRef = db.collection("ScannableCodes").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        Map<String, Object> user = document.getData();
                        code.setId(id);
                        for (Map.Entry<String, Object> pair : user.entrySet()) {
                            String key = pair.getKey();
                            if (key.equals("codeScore")) {
                                Integer codeScore;
                                codeScore = ((Long) pair.getValue()).intValue();
                                code.setScore((Integer) codeScore);
                            }
                            if (key.equals("codeName")) {
                                code.setName((String) pair.getValue());
                            }
                            if (key.equals("Location")) {
                                code.setLocation((List<Double>) pair.getValue());
                            }
                            if (key.equals("Comment")){

                                code.setComment((String) pair.getValue());
                            }
                            if(key.equals("photo")){
                                byte [] encodeByte = Base64.decode(pair.getValue().toString(),Base64.DEFAULT);
                                Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                code.setPhoto(bitmap);
                            }

                        }
                        Log.d(TAG, "Code document exists");

                    } else {
                        Log.d(TAG, "No such code document " + id);
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        return code;
    }
    /**
     * Retrieves a list of every code from the the "ScannableCodes" collection
     * @return
     * ArrayList of all ScannableCodes from the ScannableCodes collection
     */
    public ArrayList<ScannableCode> getAllCodeData() {
        ArrayList<ScannableCode> codeList = new ArrayList<>();
        //snapshot listener to watch for changes in the database
        codeCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                // Iterate over all documents in the collection
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

                    ScannableCode code = new ScannableCode();
                    Map<String, Object> codeData = doc.getData();

                    // Get all fields from the current document and construct a User
                    for (Map.Entry<String, Object> pair : codeData.entrySet()) {
                        String key = pair.getKey();

                        if (key.equals("codeScore")) {
                            Integer codeScore;
                            codeScore = ((Long) pair.getValue()).intValue();
                            code.setScore((Integer) codeScore);
                        }
                        if (key.equals("codeName")) {
                            code.setName((String) pair.getValue());
                        }

                        if (key.equals("Location")) {
                            code.setLocation((List<Double>) pair.getValue());
                        }
                        if (key.equals("Comment")){

                            code.setComment((String) pair.getValue());
                        }
                        if(key.equals("photo")){
                            byte [] encodeByte = Base64.decode(pair.getValue().toString(),Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                            code.setPhoto(bitmap);
                        }
                    }
                    codeList.add(code);
                    Log.d(TAG, "Code downloaded");
                    Log.d(TAG, "Server document data: " + doc.getData());
                }
            }
        });
        return codeList;
    }
    public void addUser(User user) {
        // Collection reference
        CollectionReference cr = db.collection("Users");

        Map<String, Object> user_data = new HashMap<>();
        user_data.put("name", user.getName());
        user_data.put("contactInfo", user.getContactInfo());
        user_data.put("totalscore", user.getTotalScore());
        user_data.put("numcodes", user.getNumCodes());
        user_data.put("userId", user.getUserId());
        user_data.put("userCodeList", user.getUserCodeList());
        cr.document(user.getUserId()).set(user_data);


        collectionReference
                .document(user.getUserId())
                .set(user_data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //if data is successfully uploaded
                        Log.d(TAG, "User " + user.getUserId() + "  successfully uploaded");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //if data upload fails
                        Log.d(TAG, "User " + user.getUserId() + " data failed to upload: " + e.toString());
                    }
                });
    }

    public void editUser(User user) {
        // Get a reference to the "Users" collection
        CollectionReference cr = db.collection("Users");

        // Get a reference to the specific user's document
        DocumentReference userRef = cr.document(user.getUserId());

        // Update the document with the new user data
        Map<String, Object> updates = new HashMap<>();
        updates.put("name", user.getName());
        updates.put("contactInfo", user.getContactInfo());
        updates.put("totalscore", user.getTotalScore());
        updates.put("numcodes", user.getNumCodes());
        updates.put("userCodeList", user.getUserCodeList());
        userRef.update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        // If data is successfully updated
                        Log.d(TAG, "User " + user.getUserId() + " successfully updated");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // If data update fails
                        Log.d(TAG, "User " + user.getUserId() + " data failed to update: " + e.toString());
                    }
                });
    }





    public void getUser(String userId, OnGetUserListener listener) {
        CollectionReference cr = db.collection("Users");
        DocumentReference dr = cr.document(userId);
        dr.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                User user = new User();
                DocumentSnapshot doc = task.getResult();
                if (doc.exists()) {
                    String name = (String) doc.getData().get("name");
                    String contactInfo = (String) doc.getData().get("contactInfo");
                    long totalScore = (long) doc.getData().get("totalscore");
                    long numCodes = (long) doc.getData().get("numcodes");
                    user.setId(userId);
                    user.setName(name);
                    user.setContactInfo(contactInfo);
                    user.set_Total_Score(totalScore);
                    user.set_Num_Codes(numCodes);
                } else {
                    user = null;
                }
                listener.getUserListener(user);
            }
        });

    }

    public void getUserById(String userId, UserCallback callback) {
        DocumentReference docRef = db.collection("Users").document(userId);
        Source source = Source.SERVER;

        docRef.get(source).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        User user = new User();
                        Map<String, Object> userData = document.getData();
                        if (userData != null) {
                            for (Map.Entry<String, Object> pair : userData.entrySet()) {
                                String key = pair.getKey();
                                if (key.equals("userId")) {
                                    user.setId((String) pair.getValue());
                                }
                                if (key.equals("name")) {
                                    user.setName((String) pair.getValue());
                                }
                                if (key.equals("contactInfo")) {
                                    user.setContactInfo((String) pair.getValue());
                                }
                                if (key.equals("userCodeList")) {
                                    user.setCodeList((List<Map>) pair.getValue());
                                }
                                if (key.equals("numCodes")) {
                                    Integer numCodes = ((Long) pair.getValue()).intValue();
                                    user.setNumCodes(numCodes);
                                }
                                if (key.equals("totalScore")) {
                                    Integer totalScore = ((Long) pair.getValue()).intValue();
                                    user.setTotalScore(totalScore);
                                }
                            }
                            Log.d(TAG, "User downloaded");
                            Log.d(TAG, "Server document data: " + document.getData());
                            callback.onUserRetrieved(user);
                        } else {
                            Log.d(TAG, "No such document");
                            callback.onUserRetrievalError(new Exception("User document has no data"));
                        }
                    } else {
                        Log.d(TAG, "No such document");
                        callback.onUserRetrievalError(new Exception("User document does not exist"));
                    }
                } else {
                    Log.d(TAG, "Server get failed: ", task.getException());
                    callback.onUserRetrievalError(task.getException());
                }
            }
        });
    }

    public User getOwnerById(String androidId) {
        User user = new User();
        DocumentReference docRef = db.collection("Owners").document(androidId);
        Source source = Source.SERVER;
        docRef.get(source).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    // Document found on the server
                    DocumentSnapshot document = task.getResult();
                    Map<String, Object> userData = document.getData();
                    if (userData != null) {
                        for (Map.Entry<String, Object> pair : userData.entrySet()) {
                            String key = pair.getKey();
                            if (key.equals("name")) {
                                user.setName((String) pair.getValue());
                            }
                        }
                    }

                    Log.d(TAG, "User downloaded");
                    Log.d(TAG, "Server document data: " + document.getData());
                } else {
                    Log.d(TAG, "Server get failed: ", task.getException());
                }
            }
        });
        return user;
    }


    public void getUsers(OnGetUsersListener listener) {
        CollectionReference cr = db.collection("Users");
        cr.get().addOnCompleteListener(task -> {
            ArrayList<String> userIds = new ArrayList<>();
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    String userid = doc.getId();
                    userIds.add(userid);
                }
            }
            listener.getUsersListener(userIds);
        });
    }
    public ArrayList<User> getUsersByName(String userName) {
        ArrayList<User> userDataList = new ArrayList<>();
        //snapshot listener to watch for changes in the database
        db.collection("Users")
                .whereEqualTo("name", userName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                User user = new User();
                                Map<String, Object> userData = document.getData();
                                // Get all fields from the current document and construct a User
                                for (Map.Entry<String, Object> pair : userData.entrySet()) {

                                    String key = pair.getKey();
                                    if (key.equals("userId")) {
                                        user.setId((String) pair.getValue());
                                    }
                                    if (pair.getKey().equals("name")) {
                                        user.setName((String) pair.getValue());
                                    }
                                    if (pair.getKey().equals("contactInfo")) {
                                        user.setContactInfo((String) pair.getValue());
                                    }
                                    if (pair.getKey().equals("userCodeList")) {
                                        user.setCodeList((List<Map>) pair.getValue());
                                    }
                                }
                                // Add the user from the current document to userDataList
                                if (user.getUserId() != null && user.getName() != null && user.getUserCodeList() != null
                                        && user.getContactInfo() != null) {
                                    userDataList.add(user);
                                    Log.d(TAG, "User " + user.getUserId() + " downloaded");
                                } else {
                                    Log.d(TAG, "User " + user.getUserId() + " not downloaded");

                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return userDataList;
    }

    public void addCode(ScannableCode code, String hash) {
        CollectionReference cr = db.collection("codes");
        DocumentReference dr = cr.document(hash);
        Map<String, Object> user_data = new HashMap<>();
        user_data.put("score", code.getScore());
        user_data.put("comment", code.getComment());
        user_data.put("location", code.getLocation());
        user_data.put("name", code.getName());
        if (code.getPhoto() == null) {
            user_data.put("photo", null);
        } else {
            Bitmap bitmap = code.getPhoto();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bytes = stream.toByteArray();

            user_data.put("photo", Base64.encodeToString(bytes, Base64.DEFAULT));
        }

        dr.set(user_data);
    }


    public void removerUserData(User user) {//removes User data from the firebase
        collectionReference
                .document(user.getUserId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //if data is successfully uploaded
                        Log.d(TAG, "User " + user.getUserId() + "  successfully deleted");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //if data upload fails
                        Log.d(TAG, "User " + user.getUserId() + " data failed to delete: " + e.toString());
                    }
                });
    }

    public void addOwner(String OwnerName, String name) {
        Map<String, Object> ownerData = new HashMap<>();
        ownerData.put("name", name);
        ownerCollection
                .document(OwnerName)
                .set(ownerData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //if data is successfully uploaded
                        Log.d(TAG, "Code " + OwnerName + "  successfully uploaded");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //if data upload fails
                        Log.d(TAG, "User " + OwnerName + " data failed to upload: " + e.toString());
                    }
                });
    }
    public void removeOwner(String OwnerName) {
        ownerCollection
                .document(OwnerName)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //if data is successfully uploaded
                        Log.d(TAG, "User " + OwnerName + "  successfully deleted");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //if data upload fails
                        Log.d(TAG, "User " + OwnerName + " data failed to delete: " + e.toString());
                    }
                });
    }
    public void addUsers(ArrayList<User> users) {
        for (User user : users) {
            addUser(user);
        }
    }


}
