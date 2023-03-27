package com.example.qrcity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Observable;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;

public class UserDataModel extends Observable {
    HashMap<String, User> userList;
    DataBase database;
    private String userID;
    private User currentUser;
    private ArrayList<ScannableCode> userCodes;
    private ScannableCode savedCode;
    private int codePosition;
    private UserDataModel() {

        database = new DataBase();
    }
    private static final UserDataModel userDataModel = new UserDataModel();
    public static UserDataModel getInstance() {
        return userDataModel;
    }
    public ArrayList<User> getUserDataList() {
        ArrayList<User> userDataList = new ArrayList<>();
        for (Map.Entry<String, User> pair : userList.entrySet()) {
            userDataList.add(pair.getValue());
        }
        return userDataList;
    }
    public ArrayList<ScannableCode> getUserCodes() {

        return this.userCodes;
    }
    public User getCurrentUser() {

        return currentUser;
    }
    public Collection<User> getUsers() {

        return userList.values();
    }
    public HashMap<String, User> getUserList() {

        return userList;
    }
    public void addCode(ScannableCode code) {
        User user = getCurrentUser();
        user.add_To_Score(code.getScore());
        user.add_To_Num_Codes();
        user.addCode(code);
        setUserCodes();
        database.addUser(user);
        database.addCode(code);
        setChanged();
        notifyObservers();
    }
    public void editUser(User user,User user_remove){
        database.removerUserData(user_remove);
        database.addUser(user);
        setChanged();
        notifyObservers();
    }
    public void removeUser(User user){

        database.removerUserData(user);
    }
    public void newUser(String userID, String userName) {
        User user = new User(userID, userName);
        user.setTotalScore(0);
        database.addUser(user);
        this.userID = userID;
        setCurrentUser(user);
    }
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
    public void getAllUserData() {
        userList = database.getAllUserData();
        setUserCodes();
    }
    public void setUserCodes() {
        List<Map> localDataset = currentUser.getUserCodeList();
        ArrayList<ScannableCode> codeList = new ArrayList();
        for (Map<String, String> m : localDataset) {
            ScannableCode code = new ScannableCode();
            String codeName = new String();
            for (Map.Entry<String,String> entry : m.entrySet()) {
                String key = entry.getKey();
                if(key.equals("id")) {
                    code = database.getCode(entry.getValue());
                }
                if(key.equals("codeName")) {
                    codeName = entry.getValue();
                }
            }
            code.setName(codeName);
            codeList.add(code);
        }
        this.userCodes = codeList;
    }
    public ArrayList<Map> getUserCodeList() {
        List<Map> localDataset = currentUser.getUserCodeList();
        ArrayList<Map> codeList = new ArrayList(localDataset);
        return codeList;
    }
    public void saveCode(ScannableCode code) {

        this.savedCode = code;
    }
    public ScannableCode getSavedCode() {

        return savedCode;
    }
    public ArrayList<ScannableCode> getAllCodes() {

        return database.getAllCodeData();
    }
    public void addOwner(String android_id) {
        database.addOwner(android_id, currentUser.getName());
    }
    public void removeOwner(String android_id) {

        database.removeOwner(android_id);
    }

    public ArrayList<User> removeCodeFromUsers(ArrayList<User> users, String id) {
        for(User user : users) {
            ArrayList<Map> codeList = (ArrayList<Map>) user.getUserCodeList();
            Iterator iterator = codeList.iterator();
            while (iterator.hasNext()) {
                Map<String,Object> m = (Map<String, Object>) iterator.next();
                for (Map.Entry<String,Object> entry : m.entrySet()) {
                    String key = entry.getKey();
                    if(key.equals("id")) {
                        if(entry.getValue().equals(id)) {
                            iterator.remove();
                        }
                    }
                }
            }
        }
        return users;
    }

    public void deleteCode(String id) {

        database.deleteUserCode(id);
    }
    public int getCodePosition() {

        return codePosition;
    }
    public void setCodePosition(int position) {

        this.codePosition = position;
    }
}

