package com.example.vshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class AdminHomePage extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseFirestore firestore;
    RecyclerView recyclerView;
    LinearLayout linearLayout;
    FloatingActionButton add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_page);
        add = findViewById(R.id.addMovie);
        recyclerView = findViewById(R.id.recyclerView);
        linearLayout = findViewById(R.id.llprogressbar);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        getData();


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AdminAddModifyMovie.class);
                intent.putExtra("mode", "A");
                startActivity(intent);
            }
        });
    }

    private void getData() {
        firestore.collection("movies").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Map<String, Movie> movieMap = new HashMap<>();
                for (QueryDocumentSnapshot snapshot : task.getResult()){
                    Movie movie = new Movie();
                    movie.setName(""+snapshot.getData().get("name"));
                    movie.setDuration(""+snapshot.getData().get("duration"));
                    movie.setImdb(""+snapshot.getData().get("imdb"));
                    movie.setLinkImage(""+snapshot.getData().get("linkImage"));
                    movie.setGenre(""+snapshot.getData().get("genre"));
                    movie.setYear(""+snapshot.getData().get("releaseYear"));
                    movieMap.put(snapshot.getId(), movie);
                }
                RecycleViewAdapterAdmin recycleViewAdapterAdmin = new RecycleViewAdapterAdmin(AdminHomePage.this, movieMap, linearLayout);
                recyclerView.setAdapter(recycleViewAdapterAdmin);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(AdminHomePage.this);
                recyclerView.setLayoutManager(layoutManager);
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
               AlertDialog.Builder builder = new AlertDialog.Builder(AdminHomePage.this).setTitle("Log Out??").setMessage("Do you want to logout?").
                        setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                firebaseAuth.signOut();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }).setNegativeButton("No", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }
}
