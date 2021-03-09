package com.dmatrix.theapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.dmatrix.theapp.R;
import com.dmatrix.theapp.listeners.UsersListener;
import com.dmatrix.theapp.models.User;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder>{

    private List<User>users;
    private static UsersListener usersListener;//TODO - should listener be static???
    private static List<User> selectedUsers;

    public UsersAdapter(List<User> users, UsersListener usersListener ) {
        this.users = users;
        this.usersListener = usersListener;
        selectedUsers = new ArrayList<>();
    }

    public List<User> getSelectedUsers(){
        return  selectedUsers;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_user,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setUserData(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    static class UserViewHolder extends RecyclerView.ViewHolder{

        TextView textFirstChar, textUserName, textEmail;
        ImageView imageAudioCall, imageVideoCall, imageChat;
        ConstraintLayout userContainer;
        ImageView imageSelected;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textFirstChar = itemView.findViewById(R.id.textFirstChar);
            textUserName = itemView.findViewById(R.id.textUsername);
            textEmail = itemView.findViewById(R.id.textEmail);
            imageAudioCall = itemView.findViewById(R.id.imageAudioCall);
            imageVideoCall = itemView.findViewById(R.id.imageVideoCall);
            imageChat  = itemView.findViewById(R.id.imageChat);
            userContainer = itemView.findViewById(R.id.userContainer);
            imageSelected = itemView.findViewById(R.id.imageSelected);
        }

        void setUserData(User user){
            textFirstChar.setText(user.firstName.substring(0,1));
            textUserName.setText(String.format("%s %s", user.firstName, user.lastName));
            textEmail.setText(user.email);
            imageAudioCall.setOnClickListener(view -> usersListener.initiateAudioCall(user));
            imageVideoCall.setOnClickListener(view -> usersListener.initiateVideoCall(user));
            imageChat.setOnClickListener(v -> usersListener.initiateChat(user));

            userContainer.setOnLongClickListener(view -> {
                if (imageSelected.getVisibility() != View.VISIBLE){
                    selectedUsers.add(user);
                    imageSelected.setVisibility(View.VISIBLE);
                    imageVideoCall.setVisibility(View.GONE);
                    imageAudioCall.setVisibility(View.GONE);
                    usersListener.onMultipleUsersAction(true);
                }
                return true;
            });

            userContainer.setOnClickListener(view -> {
                if (imageSelected.getVisibility() == View.VISIBLE){
                    selectedUsers.remove(user);
                    imageSelected.setVisibility(View.GONE);
                    imageVideoCall.setVisibility(View.VISIBLE);
                    imageAudioCall.setVisibility(View.VISIBLE);
                    imageChat.setVisibility(View.VISIBLE);
                    if (selectedUsers.size() == 0){
                        usersListener.onMultipleUsersAction(false);
                    }
                }else{
                    if (selectedUsers.size() > 0){
                        selectedUsers.add(user);
                        imageSelected.setVisibility(View.VISIBLE);
                        imageVideoCall.setVisibility(View.GONE);
                        imageAudioCall.setVisibility(View.GONE);
                        imageChat.setVisibility(View.GONE);
                    }
                }
            });
        }
    }
}
