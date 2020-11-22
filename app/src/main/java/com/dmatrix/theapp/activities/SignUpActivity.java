package com.dmatrix.theapp.activities;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dmatrix.theapp.R;
import com.dmatrix.theapp.utilities.Constants;
import com.dmatrix.theapp.utilities.PrefereneManager;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    private EditText inputFirstName, inputLastName, inputEmail, inputPassword, inputConfirmPassword;
    private MaterialButton buttonSignup;
    private ProgressBar signUpProgressBar;
    private PrefereneManager prefereneManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        prefereneManager = new PrefereneManager(getApplicationContext());
        findViewById(R.id.imageBack).setOnClickListener(view -> onBackPressed());
        findViewById(R.id.textSignIn).setOnClickListener(view -> onBackPressed());

        inputFirstName = findViewById(R.id.inputFirstName);
        inputLastName = findViewById(R.id.inputLastName);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword);
        buttonSignup = findViewById(R.id.buttonSignUp);
        signUpProgressBar = findViewById(R.id.signUpProgressBar);

        buttonSignup.setOnClickListener(view -> {
            if (inputFirstName.getText().toString().trim().isEmpty()){
                Toast.makeText(SignUpActivity.this, "Enter first name", Toast.LENGTH_SHORT).show();
            }else if (inputLastName.getText().toString().trim().isEmpty()){
                Toast.makeText(SignUpActivity.this, "Enter last name", Toast.LENGTH_SHORT).show();
            }else if (inputEmail.getText().toString().trim().isEmpty()){
                Toast.makeText(SignUpActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
            }else if (inputPassword.getText().toString().trim().isEmpty()){
                Toast.makeText(SignUpActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
            }else if (inputConfirmPassword.getText().toString().trim().isEmpty()){
                Toast.makeText(SignUpActivity.this, "Confirm your password", Toast.LENGTH_SHORT).show();
            }else if (!inputPassword.getText().toString().trim().equals(inputConfirmPassword.getText().toString().trim())){
                Toast.makeText(SignUpActivity.this, "Password & confirm password must be the same", Toast.LENGTH_SHORT).show();
            }else{
                signUp();
            }
        });
    }

    private void signUp(){

        buttonSignup.setVisibility(View.INVISIBLE);
        signUpProgressBar.setVisibility(View.VISIBLE);

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();
        user.put(Constants.KEY_FIRST_NAME,inputFirstName.getText().toString().trim());
        user.put(Constants.KEY_LAST_NAME,inputLastName.getText().toString().trim());
        user.put(Constants.KEY_EMAIL, inputEmail.getText().toString().trim());
        user.put(Constants.KEY_PASSWORD, inputPassword.getText().toString().trim());

        database.collection(Constants.KEY_COLLECTION_USES)
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(SignUpActivity.this, "User Inserted", Toast.LENGTH_SHORT).show();
                    prefereneManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                    prefereneManager.putString(Constants.KEY_USER_ID, documentReference.getId());
                    prefereneManager.putString(Constants.KEY_FIRST_NAME, inputFirstName.getText().toString().trim());
                    prefereneManager.putString(Constants.KEY_LAST_NAME, inputLastName.getText().toString().trim());
                    prefereneManager.putString(Constants.KEY_EMAIL, inputEmail.getText().toString().trim());
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    signUpProgressBar.setVisibility(View.INVISIBLE);
                    buttonSignup.setVisibility(View.VISIBLE);
                    Toast.makeText(SignUpActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                });

    }
}