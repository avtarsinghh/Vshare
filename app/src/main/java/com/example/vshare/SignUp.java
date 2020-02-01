package com.example.vshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    private FirebaseAuth mAuth;
    String email, password, name, cellNumber;
    EditText emailET, passwordET, nameET, cellNumberET;
    Button createUser;
    LinearLayout progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        emailET = findViewById(R.id.email);
        passwordET = findViewById(R.id.password);
        createUser = findViewById(R.id.createBtn);
        nameET = findViewById(R.id.fullName);
        cellNumberET = findViewById(R.id.cellNumber);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.llprogressbar);
        createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = emailET.getText().toString();
                password = passwordET.getText().toString();
                name = nameET.getText().toString();
                cellNumber = cellNumberET.getText().toString();
                if (name.equals("") || name.length() < 3) {
                    nameET.setError("Please enter name!!!");
                } else if (email.equals("") || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailET.setError("Invalid Email");
                } else if (cellNumber.length() != 10) {
                    cellNumberET.setError("Invalid cell number");
                } else if (password.equals("") || password.length() < 6) {
                    passwordET.setError("Password is too short!!");
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                        Map<String, String> map = new HashMap<>();
                                        map.put("name", name);
                                        map.put("email", email);
                                        map.put("cellNumber", cellNumber);
                                        map.put("role", "user");
                                        db.collection("users").document(task.getResult().getUser().getUid()).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(SignUp.this, "Success", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    //    Toast.makeText(getApplicationContext(), "User Account created successfully", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(getApplicationContext(), "Authentication failed.",
                                                Toast.LENGTH_LONG).show();
                                    }

                                }
                            });
                }
            }
        });
    }
}
