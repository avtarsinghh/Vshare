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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    FirebaseAuth mAuth;
    Button login;
    EditText emailET, passwordET;
    String email, pass;
    LinearLayout progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        emailET = findViewById(R.id.email);
        passwordET = findViewById(R.id.password);
        login = findViewById(R.id.loginBtn);
        progressBar = findViewById(R.id.llprogressbar);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                            progressBar.setVisibility(View.GONE);
                            if(task.isSuccessful()){
                                Intent intent = new Intent(getApplicationContext(), HomePage.class);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(Login.this, "Please check your credentials!!!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
