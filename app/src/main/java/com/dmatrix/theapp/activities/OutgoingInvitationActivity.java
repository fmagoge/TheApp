package com.dmatrix.theapp.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.dmatrix.theapp.R;
import com.dmatrix.theapp.models.User;

public class OutgoingInvitationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outgoing_invitation);

        ImageView imageCallType = findViewById(R.id.imageCallType);
        String callType = getIntent().getStringExtra("type");

        if(callType != null){
            if(callType.equals("video")){
                imageCallType.setImageResource(R.drawable.ic_video);
            }
        }

        TextView textFirstChar = findViewById(R.id.textFirstChar);
        TextView textUsername = findViewById(R.id.textUsername);
        TextView textEmail = findViewById(R.id.textEmail);

        User user = (User) getIntent().getSerializableExtra("user");
        if(user != null){
            textFirstChar.setText(user.firstName.substring(0,1));
            textUsername.setText(String.format("%s %s", user.firstName, user.lastName));
            textEmail.setText(user.email);
        }

        ImageView imageStopCall = findViewById(R.id.imageStopCall);
        imageStopCall.setOnClickListener(view -> onBackPressed());
    }
}