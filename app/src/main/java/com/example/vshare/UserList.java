package com.example.vshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UserList extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerViewAdapterUserList userListAdapter;
    ArrayList<User> users;
    Intent intent;
    LinearLayout linearLayout;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        recyclerView = findViewById(R.id.recyclerView);
        users = new ArrayList<>();
        intent = getIntent();
        final Movie movie = (Movie) intent.getSerializableExtra("movie");
        linearLayout = findViewById(R.id.llprogressbar);
        auth = FirebaseAuth.getInstance();

        FirebaseFirestore.getInstance().collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    User sender = new User();
                    for(DocumentSnapshot snapshot : task.getResult()){
                        if(auth.getCurrentUser().getEmail().equalsIgnoreCase(snapshot.get("email").toString())){
                            sender = snapshot.toObject(User.class);
                            continue;
                        }
                        User user = new User();
                        user.setName(snapshot.get("name").toString());
                        user.setGender(snapshot.get("gender").toString());
                        user.setRole(snapshot.get("role").toString());
                        user.setBio(snapshot.get("bio").toString());
                        user.setCellNumber(snapshot.get("cellNumber").toString());
                        user.setDob(snapshot.get("dob").toString());
                        user.setEmail(snapshot.get("email").toString());
                        users.add(user);
                    }
                    userListAdapter = new RecyclerViewAdapterUserList(UserList.this, users, movie, linearLayout, sender);
                    layoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(userListAdapter);
                }
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
                AlertDialog.Builder builder = new AlertDialog.Builder(UserList.this).setTitle("Log Out??").setMessage("Do you want to logout?").
                        setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                auth.signOut();
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
