package com.dmatrix.theapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.dmatrix.theapp.R;
import com.dmatrix.theapp.adapters.UsersAdapter;
import com.dmatrix.theapp.models.User;
import com.dmatrix.theapp.utilities.Constants;
import com.dmatrix.theapp.utilities.PrefereneManager;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private PrefereneManager prefereneManager;
    private List<User> users;
    private UsersAdapter usersAdapter;
    private TextView textErrorMessage;
    private ProgressBar usersProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefereneManager = new PrefereneManager(getApplicationContext());

        TextView textTitle = findViewById(R.id.textTitle);
        textTitle.setText(String.format(
                "%s %s",
                prefereneManager.getString(Constants.KEY_FIRST_NAME),
                prefereneManager.getString(Constants.KEY_LAST_NAME)
        ));

        findViewById(R.id.textSignOut).setOnClickListener(view -> signOut());

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null){
                sendFCMTokenToDatabase(task.getResult().getToken());
            }
        });

        RecyclerView usersRecyclerView = findViewById(R.id.userRecyclerView);
        textErrorMessage = findViewById(R.id.textErrorMessage);
        usersProgressBar = findViewById(R.id.usersProgressBar);

        users = new ArrayList<>();
        usersAdapter = new UsersAdapter(users);
        usersRecyclerView.setAdapter(usersAdapter);

        getUsers();
    }

    private void getUsers(){
        usersProgressBar.setVisibility(View.VISIBLE);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USES)
                .get()
                .addOnCompleteListener(task -> {
                    usersProgressBar.setVisibility(View.GONE);
                    String myUserId = prefereneManager.getString(Constants.KEY_USER_ID);
                    if (task.isSuccessful() && task.getResult() != null){
                        for(QueryDocumentSnapshot documentSnapshot: task.getResult()){
                            if(myUserId.equals(documentSnapshot.getId())){
                                continue; //So as to skip current user
                            }else {
                                User user = new User();
                                user.firstName = documentSnapshot.getString(Constants.KEY_FIRST_NAME);
                                user.lastName = documentSnapshot.getString(Constants.KEY_LAST_NAME);
                                user.email = documentSnapshot.getString(Constants.KEY_EMAIL);
                                user.token = documentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                                users.add(user);
                            }

                            if (users.size()>0){
                                usersAdapter.notifyDataSetChanged();
                            }else {
                                textErrorMessage.setText(String.format("%s", "No users available"));
                                textErrorMessage.setVisibility(View.VISIBLE);
                            }
                        }
                    }else {
                        textErrorMessage.setText(String.format("%s", "No users available"));
                        textErrorMessage.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void sendFCMTokenToDatabase(String token){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USES).document(
                        prefereneManager.getString(Constants.KEY_USER_ID)
                );
        documentReference.update(Constants.KEY_FCM_TOKEN, token)
                .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Unable to send token: "+e.getMessage(),Toast.LENGTH_SHORT).show());
    }

    private void signOut(){
        Toast.makeText(MainActivity.this, "Signing out...",Toast.LENGTH_SHORT).show();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USES).document(
                        prefereneManager.getString(Constants.KEY_USER_ID)
                );
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(aVoid -> {
                    prefereneManager.clearPreferences();
                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Unable to sign out",Toast.LENGTH_SHORT).show());
    }
}