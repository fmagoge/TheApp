package com.dmatrix.theapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmatrix.theapp.R;

public class MessageActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView chat_recyclerview;
    ImageView imageProfile, send_button;
    TextView textviewName, textviewUserStatus;
    EditText edit_message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        chat_recyclerview = findViewById(R.id.chat_recyclerview);
        imageProfile = findViewById(R.id.imageProfile);
        send_button = findViewById(R.id.send_button);
        textviewName = findViewById(R.id.textviewName);
        textviewUserStatus = findViewById(R.id.textviewUserStatus);
        edit_message = findViewById(R.id.edit_message);

    }
}