package com.example.vshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class HomePage extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    RecyclerView recyclerView;
    LinearLayout linearLayout;
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toast.makeText(this, "Welcome", Toast.LENGTH_LONG).show();

        recyclerView = findViewById(R.id.recyclerView);
        linearLayout = findViewById(R.id.llprogressbar);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        getData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.top_right_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout : firebaseAuth.signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
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
                RecyclerViewAdapterUser recycleViewAdapterUser = new RecyclerViewAdapterUser(HomePage.this, movieMap, linearLayout);
                recyclerView.setAdapter(recycleViewAdapterUser);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(HomePage.this);
                recyclerView.setLayoutManager(layoutManager);
            }
        });
    }
}
