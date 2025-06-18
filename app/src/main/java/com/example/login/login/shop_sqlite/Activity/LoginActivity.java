package com.example.login.login.shop_sqlite.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.login.login.shop_sqlite.Entity.User;
import com.example.login.login.shop_sqlite.R;
import com.example.login.login.shop_sqlite.ViewModel.UserViewModel;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private TextView txterr;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        txterr = findViewById(R.id.errorText);
        Button loginButton = findViewById(R.id.loginButton);
        Button createButton = findViewById(R.id.createButton);

        // Khởi tạo ViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        createButton.setOnClickListener(v -> {
            String username = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (username.isEmpty()) {
                txterr.setText("Username is empty, please input username");
                emailInput.requestFocus();
            } else if (password.isEmpty()) {
                txterr.setText("Password is empty, please input password");
                passwordInput.requestFocus();
            } else {
                User newUser = new User(username, password);
                userViewModel.insert(newUser);
                txterr.setText("Account created successfully");
            }
        });
        loginButton.setOnClickListener(v -> {
            String username = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (username.isEmpty()) {
                txterr.setText("Username is empty, please input username");
                emailInput.requestFocus();
            } else if (password.isEmpty()) {
                txterr.setText("Password is empty, please input password");
                passwordInput.requestFocus();
            } else {
                userViewModel.getUserByUsernameAndPassword(username, password).observe(this, user -> {
                    if (user == null) {
                        txterr.setText("Email or password is incorrect");
                    } else {
                        Intent intent = new Intent(LoginActivity.this, ProductListActivity.class);
                        intent.putExtra("username", user.username);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }
}
