package com.example.qrcity.user;

public interface UserCallback {
    void onUserRetrieved(User user);
    void onUserRetrievalError(Exception e);
}
