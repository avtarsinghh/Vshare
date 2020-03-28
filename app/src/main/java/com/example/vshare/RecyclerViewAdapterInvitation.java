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
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;

public class RecyclerViewAdapterInvitation extends RecyclerView.Adapter<RecyclerViewAdapterInvitation.ViewHolder> {
    LayoutInflater layoutInflater;
    Context context;
    ArrayList<Invitation> invitations;
    FirebaseFirestore firebaseFirestore;
    LinearLayout linearLayout;
    String mode;
    FirebaseAuth auth;

    RecyclerViewAdapterInvitation(Context context, LinearLayout linearLayout, ArrayList<Invitation> invitations, String mode) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.linearLayout = linearLayout;
        this.invitations = invitations;
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        this.mode = mode;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterInvitation.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.invitation_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterInvitation.ViewHolder holder, final int position) {
        if (mode.equalsIgnoreCase("s")) {
            holder.rejectIV.setVisibility(View.GONE);
            holder.acceptIV.setVisibility(View.GONE);

            holder.nameTV.setText(invitations.get(position).getReceiver().getName());
            holder.ageTV.setText("" + getAge(invitations.get(position).getReceiver().getDob()));

            if (invitations.get(position).getReceiver().getGender().equalsIgnoreCase("Male")) {
                holder.profileIV.setImageResource(R.drawable.man);
            } else holder.profileIV.setImageResource(R.drawable.women);

            holder.bioTV.setText(invitations.get(position).getReceiver().getBio());
        } else if (mode.equalsIgnoreCase("a")) {
            if (invitations.get(position).getSender().getEmail().equalsIgnoreCase(auth.getCurrentUser().getEmail())) {
                holder.rejectIV.setVisibility(View.GONE);
                holder.acceptIV.setVisibility(View.GONE);

                holder.nameTV.setText(invitations.get(position).getReceiver().getName());
                holder.ageTV.setText("" + getAge(invitations.get(position).getReceiver().getDob()));

                if (invitations.get(position).getReceiver().getGender().equalsIgnoreCase("Male")) {
                    holder.profileIV.setImageResource(R.drawable.man);
                } else holder.profileIV.setImageResource(R.drawable.women);

                holder.bioTV.setText(invitations.get(position).getReceiver().getBio());
            } else {
                if(!invitations.get(position).getStatus().equalsIgnoreCase("Pending")){
                    holder.rejectIV.setVisibility(View.GONE);
                    holder.acceptIV.setVisibility(View.GONE);
                }
                holder.nameTV.setText(invitations.get(position).getSender().getName());
                holder.ageTV.setText("" + getAge(invitations.get(position).getSender().getDob()));

                if (invitations.get(position).getSender().getGender().equalsIgnoreCase("Male")) {
                    holder.profileIV.setImageResource(R.drawable.man);
                } else holder.profileIV.setImageResource(R.drawable.women);

                holder.bioTV.setText(invitations.get(position).getSender().getBio());
            }
        } else if (mode.equalsIgnoreCase("r")) {
            if(!invitations.get(position).getStatus().equalsIgnoreCase("Pending")) {
                holder.rejectIV.setVisibility(View.GONE);
                holder.acceptIV.setVisibility(View.GONE);
            }

            holder.nameTV.setText(invitations.get(position).getSender().getName());
            holder.ageTV.setText("" + getAge(invitations.get(position).getSender().getDob()));

            if (invitations.get(position).getSender().getGender().equalsIgnoreCase("Male")) {
                holder.profileIV.setImageResource(R.drawable.man);
            } else holder.profileIV.setImageResource(R.drawable.women);

            holder.bioTV.setText(invitations.get(position).getSender().getBio());
        }

        holder.rejectIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invitations.get(position).setStatus("Refused");

                new AlertDialog.Builder(context).setTitle("Refuse Request").setMessage("Do you want to refuse this request?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        linearLayout.setVisibility(View.VISIBLE);
                        firebaseFirestore.collection("invitations").document(invitations.get(position).getSender().getEmail() + "^#" + invitations.get(position).getReceiver().getEmail() + "^#" + invitations.get(position).getMovie()).set(invitations.get(position)).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(context, "Invitation refused", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(context, InvitationsList.class);
                                context.startActivity(intent);
                                ((Activity) context).finish();
                                linearLayout.setVisibility(View.GONE);
                            }
                        });
                    }
                }).setNegativeButton("No", null).show();

            }
        });

        holder.acceptIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invitations.get(position).setStatus("Accepted");
                new AlertDialog.Builder(context).setTitle("Accept Request?").setMessage("Do you want to accept this request?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        linearLayout.setVisibility(View.VISIBLE);
                        firebaseFirestore.collection("invitations").document(invitations.get(position).getSender().getEmail() + "^#" + invitations.get(position).getReceiver().getEmail() + "^#" + invitations.get(position).getMovie()).set(invitations.get(position)).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(context, "Invitation Accepted", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(context, InvitationsList.class);
                                context.startActivity(intent);
                                ((Activity) context).finish();
                                linearLayout.setVisibility(View.GONE);
                            }
                        });
                    }
                }).setNegativeButton("No", null).show();
            }
        });
        holder.statusTV.setText(invitations.get(position).getStatus());
    }

    @Override
    public int getItemCount() {
        return invitations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTV, ageTV, genderTV, bioTV, statusTV;
        ImageView profileIV, rejectIV, acceptIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.nameTV);
            ageTV = itemView.findViewById(R.id.ageTV);
            genderTV = itemView.findViewById(R.id.genderTV);
            bioTV = itemView.findViewById(R.id.descriptionTV);
            statusTV = itemView.findViewById(R.id.statusTV);
            profileIV = itemView.findViewById(R.id.profileIV);
            rejectIV = itemView.findViewById(R.id.deleteInvitation);
            acceptIV = itemView.findViewById(R.id.acceptInvitation);
        }
    }

    public int getAge(String birthDate) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        String date[] = birthDate.split("-");
        dob.set(Integer.parseInt(date[2]), Integer.parseInt(date[1]), Integer.parseInt(date[0]));
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        return age;
    }
}
