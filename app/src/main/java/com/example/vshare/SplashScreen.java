package com.example.vshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashScreen extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    user.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (!user.isEmailVerified()) {
                                Snackbar.make(findViewById(android.R.id.content), "Email not verified!!!", Snackbar.LENGTH_INDEFINITE).setAction("Resend email verification link", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(SplashScreen.this, "Email Sent, Please check your email.", Toast.LENGTH_LONG).show();
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
                    });
                }
                else{
                    Thread thread= new Thread(){
                        public void run(){
                            try {
                                sleep(3000);
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    thread.start();
                }
    }
}
