package com.example.qrcity.user;

public interface UserCallback {
    User onUserRetrieved(User user);
    void onUserRetrievalError(Exception e);
}
