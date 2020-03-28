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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class InvitationsList extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseFirestore firebaseFirestore;
    Invitation invitation;
    RecyclerViewAdapterInvitation adapterInvitation;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    Spinner spinner;
    LinearLayout linearLayout;
    String[] spinnerData = {"All","Received", "Sent"};

     @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitations);
        spinner = findViewById(R.id.spinnerInvitation);
        recyclerView = findViewById(R.id.recyclerViewInvitation);
         ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, spinnerData);
         spinner.setAdapter(arrayAdapter);
         auth = FirebaseAuth.getInstance();
         linearLayout = findViewById(R.id.llprogressbar);
         firebaseFirestore = FirebaseFirestore.getInstance();

         spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                 linearLayout.setVisibility(View.VISIBLE);
                if (adapterView.getItemAtPosition(i).toString().equalsIgnoreCase("All")){
                    firebaseFirestore.collection("invitations").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                ArrayList<Invitation> invitations = new ArrayList<>();
                                for(QueryDocumentSnapshot snapshot : task.getResult()){
                                    Log.i("Data", snapshot.toString());
                                    Invitation invitation = snapshot.toObject(Invitation.class);
                                    if(invitation.getReceiver().getEmail().equalsIgnoreCase(auth.getCurrentUser().getEmail()) || invitation.getSender().getEmail().equalsIgnoreCase(auth.getCurrentUser().getEmail()))
                                    invitations.add(invitation);
                                }
                                adapterInvitation = new RecyclerViewAdapterInvitation(InvitationsList.this, linearLayout, invitations, "A");
                                recyclerView.setAdapter(adapterInvitation);
                                layoutManager = new LinearLayoutManager(InvitationsList.this);
                                recyclerView.setLayoutManager(layoutManager);
                                linearLayout.setVisibility(View.GONE);
                            }
                        }
                    });
                } else if (adapterView.getItemAtPosition(i).toString().equalsIgnoreCase("Received")) {
                    firebaseFirestore.collection("invitations").whereEqualTo("receiver.email", auth.getCurrentUser().getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                ArrayList<Invitation> invitations = new ArrayList<>();
                                for(QueryDocumentSnapshot snapshot : task.getResult()){
                                    Log.i("Data", snapshot.toString());
                                    Invitation invitation = snapshot.toObject(Invitation.class);
                                        invitations.add(invitation);
                                }
                                adapterInvitation = new RecyclerViewAdapterInvitation(InvitationsList.this, linearLayout, invitations, "R");
                                recyclerView.setAdapter(adapterInvitation);
                                layoutManager = new LinearLayoutManager(InvitationsList.this);
                                recyclerView.setLayoutManager(layoutManager);
                                linearLayout.setVisibility(View.GONE);
                            }
                        }
                    });
                }else{
                    firebaseFirestore.collection("invitations").whereEqualTo("sender.email", auth.getCurrentUser().getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                ArrayList<Invitation> invitations = new ArrayList<>();
                                for(QueryDocumentSnapshot snapshot : task.getResult()){
                                    Log.i("Data", snapshot.toString());
                                    Invitation invitation = snapshot.toObject(Invitation.class);
                                    invitations.add(invitation);
                                }
                                adapterInvitation = new RecyclerViewAdapterInvitation(InvitationsList.this, linearLayout, invitations, "S");
                                recyclerView.setAdapter(adapterInvitation);
                                layoutManager = new LinearLayoutManager(InvitationsList.this);
                                recyclerView.setLayoutManager(layoutManager);
                                linearLayout.setVisibility(View.GONE);
                            }
                        }
                    });
                }
             }

             @Override
             public void onNothingSelected(AdapterView<?> adapterView) {

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
                AlertDialog.Builder builder = new AlertDialog.Builder(InvitationsList.this).setTitle("Log Out??").setMessage("Do you want to logout?").
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
