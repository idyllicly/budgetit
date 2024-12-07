package com.cc102.budgetit;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword, editTextText, editTextTextPassword;
    private Button btnLogin, button, button2;
    private TextView tvSignUp;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        setContentView(R.layout.activity_new_login);

        // Initialize UI elements
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignUp = findViewById(R.id.tvSignUp);

        //my edit
//        etEmail = findViewById(R.id.editTextText);
//        etPassword = findViewById(R.id.editTextTextPassword);
//        btnLogin = findViewById(R.id.button);
//        tvSignUp = findViewById(R.id.button2);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Check if user is already logged in
        if (auth.getCurrentUser() != null) {
            navigateToMainActivity();
        }

        // Login button click listener
        btnLogin.setOnClickListener(v -> loginUser());

        // Sign up text click listener
        tvSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required!");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required!");
            return;
        }

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                        navigateToMainActivity();
                    } else {
                        Toast.makeText(LoginActivity.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToSignUp() {
        // Redirect to a Sign-Up screen (to be implemented in the next step).
        // Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        // startActivity(intent);
    }
}
