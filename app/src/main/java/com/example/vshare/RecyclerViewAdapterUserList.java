package com.example.vshare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class RecyclerViewAdapterUserList extends RecyclerView.Adapter<RecyclerViewAdapterUserList.ViewHolder> {
    LayoutInflater layoutInflater;
    ArrayList<User> userList;
    Context context;
    RecyclerViewAdapterUserList(Context context, ArrayList<User> userList){
        this.context = context;
        this.userList = userList;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerViewAdapterUserList.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.user_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterUserList.ViewHolder holder, int position) {
        holder.nameTV.setText(userList.get(position).getName());
        holder.genderTV.setText(userList.get(position).getGender());
        holder.bioTV.setText(userList.get(position).getBio());
        holder.ageTV.setText(""+getAge(userList.get(position).getDob()));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTV, ageTV, bioTV, genderTV;
        ImageView profilePicture;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.nameTV);
            ageTV = itemView.findViewById(R.id.ageTV);
            bioTV = itemView.findViewById(R.id.descriptionTV);
            genderTV = itemView.findViewById(R.id.genderTV);
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
