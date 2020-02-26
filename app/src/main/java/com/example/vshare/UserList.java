package com.example.vshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UserList extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerViewAdapterUserList userListAdapter;
    ArrayList<User> users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        recyclerView = findViewById(R.id.recyclerView);
        users = new ArrayList<>();

        FirebaseFirestore.getInstance().collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot snapshot : task.getResult()){
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
                    userListAdapter = new RecyclerViewAdapterUserList(getApplicationContext(), users);
                    layoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(userListAdapter);
                }
            }
        });
    }
}
