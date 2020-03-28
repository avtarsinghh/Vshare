package com.example.vshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdminAddModifyMovie extends AppCompatActivity {
    Intent intent;
    LinearLayout linearLayout;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseFirestore firebaseFirestore;
    EditText nameET, imdbET, durationET, releaseYearET, genreET, linkImageET;
    String name, imdb, duration, releaseYear, genre, linkImage;
    Button addModify;
    String movie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_movie);

        linearLayout = findViewById(R.id.llprogressbar);
        nameET = findViewById(R.id.name);
        imdbET = findViewById(R.id.imdb);
        durationET = findViewById(R.id.duration);
        releaseYearET = findViewById(R.id.releaseYear);
        genreET = findViewById(R.id.genre);
        linkImageET = findViewById(R.id.linkImage);
        addModify = findViewById(R.id.addModifyBtn);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        intent = getIntent();
        firebaseFirestore = FirebaseFirestore.getInstance();

        final String s = intent.getStringExtra("mode");
        if(s.equalsIgnoreCase("A"))
            addModify.setText("Add Movie");
        else{
            addModify.setText("Modify Movie");
        }

        if(s.equalsIgnoreCase("M")){
            nameET.setText(intent.getStringExtra("name"));
            imdbET.setText(intent.getStringExtra("imdb"));
            durationET.setText(intent.getStringExtra("duration"));
            releaseYearET.setText(intent.getStringExtra("year"));
            genreET.setText(intent.getStringExtra("genre"));
            linkImageET.setText(intent.getStringExtra("link"));
        }

        movie = intent.getStringExtra("name");

        addModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = nameET.getText().toString();
                imdb = imdbET.getText().toString();
                duration = durationET.getText().toString();
                releaseYear = releaseYearET.getText().toString();
                genre = genreET.getText().toString();
                linkImage = linkImageET.getText().toString();
                if (name.equalsIgnoreCase("") || name == null){
                    nameET.setError("Enter name of movie");
                }else
                if (duration.equalsIgnoreCase("") || duration == null){
                    durationET.setError("Enter name of movie");
                }else
                if (releaseYear.equalsIgnoreCase("") || releaseYear == null){
                    releaseYearET.setError("Enter name of movie");
                }else
                if (imdb.equalsIgnoreCase("") || imdb == null){
                    imdbET.setError("Enter name of movie");
                }else
                if (linkImage.equalsIgnoreCase("") || linkImage == null){
                    linkImageET.setError("Enter name of movie");
                }else
                if (genre.equalsIgnoreCase("") || genre == null){
                    genreET.setError("Enter name of movie");
                }else{
                    if(s.equalsIgnoreCase("M") && (movie !=null || !movie.equalsIgnoreCase(""))){
                        deleteMovie(movie, firebaseFirestore);
                    }
                    linearLayout.setVisibility(View.VISIBLE);
                    Map<String, String> map = new HashMap<>();
                    map.put("name", name);
                    map.put("duration", duration);
                    map.put("releaseYear", releaseYear);
                    map.put("imdb", imdb);
                    map.put("linkImage", linkImage);
                    map.put("genre", genre);
                    firebaseFirestore.collection("movies").document(name).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent intent = new Intent(getApplicationContext(), AdminHomePage.class);
                            startActivity(intent);
                            linearLayout.setVisibility(View.GONE);
                            finishAffinity();
                        }
                    });
                }
            }
        });
    }
    public static void deleteMovie(String movie, FirebaseFirestore db){
        db.collection("movies").document(movie)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.top_right_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminAddModifyMovie.this).setTitle("Log Out??").setMessage("Do you want to logout?").
                        setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                firebaseAuth.signOut();
                                Intent intent = new Intent(getApplicationContext(), LandingPage.class);
                                startActivity(intent);
                                finishAffinity();
                            }
                        }).setNegativeButton("No", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }
}
