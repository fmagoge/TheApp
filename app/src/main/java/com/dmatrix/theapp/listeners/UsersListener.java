package com.dmatrix.theapp.listeners;

import com.dmatrix.theapp.models.User;

public interface UsersListener {

    void initiateVideoCall(User user);

    void initiateAudioCall(User user);

    void onMultipleUsersAction(Boolean isMultipleUsersSelected);
}
