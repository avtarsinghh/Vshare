package com.example.vshare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

public class RecycleViewAdapterAdmin extends RecyclerView.Adapter<RecycleViewAdapterAdmin.ViewHolder> {
    LayoutInflater layoutInflater;
    ArrayList<String> arrayList;
    Map<String, Movie> map;
    Context context;
    public RecycleViewAdapterAdmin(Context context, Map<String, Movie> map){
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.map = map;
        arrayList = new ArrayList<>(map.keySet());
    }

    @NonNull
    @Override
    public RecycleViewAdapterAdmin.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.admin_movie_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapterAdmin.ViewHolder holder, int position) {
        holder.nameTV.setText(map.get(arrayList.get(position)).getName());
        holder.imdbTV.setText(map.get(arrayList.get(position)).getImdb());
        holder.genreTV.setText(map.get(arrayList.get(position)).getGenre());
        holder.yearTV.setText(map.get(arrayList.get(position)).getYear());
        holder.durationTV.setText(map.get(arrayList.get(position)).getDuration());
        Picasso.get().load(map.get(arrayList.get(position)).getLinkImage()).fit().into(holder.posterIV);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTV, imdbTV, genreTV, yearTV, durationTV, modifyTV;
        ImageView posterIV, deleteIV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.movieName);
            imdbTV =itemView.findViewById(R.id.imdb);
            genreTV = itemView.findViewById(R.id.genre);
            yearTV = itemView.findViewById(R.id.year);
            durationTV = itemView.findViewById(R.id.duration);
            modifyTV = itemView.findViewById(R.id.modifyMovie);
            posterIV = itemView.findViewById(R.id.imageMovie);
            deleteIV = itemView.findViewById(R.id.deleteMovie);
        }
    }
}
