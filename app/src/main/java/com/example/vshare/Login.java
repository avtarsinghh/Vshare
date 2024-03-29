package com.example.vshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {
    FirebaseAuth mAuth;
    Button login;
    EditText emailET, passwordET;
    String email, pass;
    LinearLayout progressBar;
    FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        emailET = findViewById(R.id.email);
        passwordET = findViewById(R.id.password);
        login = findViewById(R.id.loginBtn);
        progressBar = findViewById(R.id.llprogressbar);

        mAuth.signOut();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authenticateUser();
            }
        });
    }

    private void authenticateUser() {
        email = emailET.getText().toString();
        pass = passwordET.getText().toString();
        if (email.equals("") || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailET.setError("Invalid Email");
        }
        else if(pass.equals("")){
            passwordET.setError("Please enter password!!");
        }
        else{
            progressBar.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        final FirebaseUser user = task.getResult().getUser();
                        if (!user.isEmailVerified()) {
                            Snackbar.make(findViewById(android.R.id.content), "Email not verified!!!", Snackbar.LENGTH_INDEFINITE).setAction("Resend email verification link", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(Login.this, "Email Sent, Please check your email.", Toast.LENGTH_LONG).show();
                                            finishAffinity();
                                        }
                                    });
                                }
                            }).setActionTextColor(getResources().getColor(android.R.color.holo_red_dark)).show();
                        } else {
                            DocumentReference reference = firebaseFirestore.collection("users").document(user.getUid());
                            reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    progressBar.setVisibility(View.GONE);
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    if (documentSnapshot.get("role").equals("admin")) {
                                        Intent intent = new Intent(getApplicationContext(), AdminHomePage.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Intent intent = new Intent(getApplicationContext(), HomePage.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });

                        }
                    }
                    else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(Login.this, "Please check your credentials!!!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
