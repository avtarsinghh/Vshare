package com.example.vshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SignUp extends AppCompatActivity {
    private FirebaseAuth mAuth;
    User user;
    private String password;
    EditText emailET, passwordET, nameET, cellNumberET, dobET, bioET;
    Spinner genderSpinner;
    Button createUser;
    LinearLayout progressBar;
    DatePickerDialog.OnDateSetListener date;
    Calendar myCalendar;
    String[] spinnerData = {"Please Select Gender", "Male", "Female"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        emailET = findViewById(R.id.email);
        passwordET = findViewById(R.id.password);
        createUser = findViewById(R.id.createBtn);
        nameET = findViewById(R.id.fullName);
        dobET = findViewById(R.id.dobET);
        bioET = findViewById(R.id.bioET);
        genderSpinner = findViewById(R.id.genderSpinner);
        myCalendar = Calendar.getInstance();

        user = new User();
        ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_checked, spinnerData);
        genderSpinner.setAdapter(adapter);

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        dobET.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(SignUp.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        cellNumberET = findViewById(R.id.cellNumber);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.llprogressbar);
        createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.setEmail(emailET.getText().toString());
                password = passwordET.getText().toString();
                user.setName(nameET.getText().toString());
                user.setCellNumber(cellNumberET.getText().toString());
                user.setDob(dobET.getText().toString());
                user.setGender(genderSpinner.getSelectedItem().toString());
                user.setBio(bioET.getText().toString());
                if(verifyData())
                {
                    createUser();
                }
            }
        });
    }

    private void createUser() {
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(user.getEmail(), password)
                .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            user.setRole("user");
                            final FirebaseUser userFU = task.getResult().getUser();
                            db.collection("users").document(task.getResult().getUser().getUid()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    userFU.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull final Task<Void> task) {
                                            Snackbar.make(findViewById(android.R.id.content), "Sign Up Successful, A email link is sent to your email for verification purpose.", Snackbar.LENGTH_INDEFINITE).setAction("Ok", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    finishAffinity();
                                                    Intent intent = new Intent(getApplicationContext(), Login.class);
                                                    startActivity(intent);
                                                }
                                            }).show();

                                        }
                                    });
                                }
                            });
                            //    Toast.makeText(getApplicationContext(), "com.example.vshare.User Account created successfully", Toast.LENGTH_LONG).show();


                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }

    private void updateLabel() {
        String myFormat = "dd-MM-YYYY"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.CANADA);

        dobET.setText(sdf.format(myCalendar.getTime()));
    }

    private boolean verifyData() {
        if (user.getName().equals("") || user.getName().length() < 3) {
            nameET.setError("Please enter name!!!");
            return false;
        }
        if (user.getDob().equals("")) {
            final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Please enter email Id", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Ok", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    snackbar.dismiss();
                }
            }).setActionTextColor(getResources().getColor(android.R.color.holo_red_dark)).show();
            return false;
        }
        if (user.getGender().equals("") || genderSpinner.getSelectedItemPosition() == 0) {
            final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Please Select Gender", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Ok", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    snackbar.dismiss();
                }
            }).setActionTextColor(getResources().getColor(android.R.color.holo_red_dark)).show();
            return false;
        }
        if (user.getCellNumber().length() != 10) {
            cellNumberET.setError("Invalid cell number");
            return false;
        }
        if (user.getEmail().equals("") || !Patterns.EMAIL_ADDRESS.matcher(user.getEmail()).matches()) {
            emailET.setError("Invalid Email");
            return false;
        }

        if (password.equals("") || password.length() < 6) {
            passwordET.setError("Password is too short!!");
            return false;
        }

        if (user.getBio().equals("")){
            bioET.setError("Please enter Bio");
            return false;
        }

        return true;
    }
}
