package com.example.vshare;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;

public class RecyclerViewAdapterUserList extends RecyclerView.Adapter<RecyclerViewAdapterUserList.ViewHolder> {
    LayoutInflater layoutInflater;
    LinearLayout linearLayout;
    Movie movie;
    ArrayList<User> userList;
    Invitation invitation;
    Context context;
    FirebaseAuth auth;
    User sender;
    RecyclerViewAdapterUserList(Context context, ArrayList<User> userList, Movie movie, LinearLayout linearLayout, User sender){
        this.context = context;
        this.linearLayout = linearLayout;
        this.movie = movie;
        this.userList = userList;
        this.sender = sender;
        layoutInflater = LayoutInflater.from(context);
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public RecyclerViewAdapterUserList.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.user_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewAdapterUserList.ViewHolder holder, final int position) {
        holder.nameTV.setText(userList.get(position).getName());
        holder.genderTV.setText(userList.get(position).getGender());
        holder.bioTV.setText(userList.get(position).getBio());
        holder.ageTV.setText(""+getAge(userList.get(position).getDob()));
        if(userList.get(position).getGender().equalsIgnoreCase("Male")){
            holder.profilePicture.setImageResource(R.drawable.man);
        }
        else holder.profilePicture.setImageResource(R.drawable.women);
        holder.bioTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DescriptionPopUp descriptionPopUp  = new DescriptionPopUp();
                descriptionPopUp.showPopupWindow(holder.itemView, holder.bioTV.getText().toString());
            }
        });

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FirebaseFirestore db = FirebaseFirestore.getInstance();
                invitation = new Invitation();
                invitation.setReceiver(userList.get(position));
                invitation.setMovie(movie.getName());
                invitation.setSender(sender);
                new AlertDialog.Builder(context).setTitle("Invite User").setMessage("Do you want to invite this person?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        linearLayout.setVisibility(View.VISIBLE);
                        db.collection("invitations").document(auth.getCurrentUser().getEmail()+"^#"+invitation.getReceiver().getEmail()+"^#"+movie.getName())
                                .set(invitation).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Intent intent = new Intent(context, HomePage.class);
                                context.startActivity(intent);
                                Toast.makeText(context, "User Invitation sent successfully", Toast.LENGTH_SHORT).show();
                                ((Activity)context).finish();
                                linearLayout.setVisibility(View.GONE);
                            }
                        });
                    }
                }).setNegativeButton("No", null).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTV, ageTV, bioTV, genderTV;
        ImageView profilePicture;
        ConstraintLayout layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.nameTV);
            ageTV = itemView.findViewById(R.id.ageTV);
            bioTV = itemView.findViewById(R.id.descriptionTV);
            genderTV = itemView.findViewById(R.id.genderTV);
            profilePicture = itemView.findViewById(R.id.profileIV);
            layout = itemView.findViewById(R.id.constraintLayout);
        }
    }

    public int getAge(String birthDate){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        String date[] = birthDate.split("-");
        dob.set(Integer.parseInt(date[2]), Integer.parseInt(date[1]), Integer.parseInt(date[0]));
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if(today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }
        return age;
    }
}
