package com.example.vshare;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

public class RecyclerViewAdapterUser extends RecyclerView.Adapter<RecyclerViewAdapterUser.ViewHolder> {
    LayoutInflater layoutInflater;
    ArrayList<String> arrayList;
    Map<String, Movie> map;
    LinearLayout linearLayout;
    Context context;
    public RecyclerViewAdapterUser(Context context, Map<String, Movie> map, LinearLayout linearLayout){
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.map = map;
        arrayList = new ArrayList<>(map.keySet());
        this.linearLayout = linearLayout;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterUser.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.user_movie_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterUser.ViewHolder holder, final int position) {
        holder.nameTV.setText(map.get(arrayList.get(position)).getName());
        holder.imdbTV.setText(map.get(arrayList.get(position)).getImdb());
        holder.genreTV.setText(map.get(arrayList.get(position)).getGenre());
        holder.yearTV.setText(map.get(arrayList.get(position)).getYear());
        holder.durationTV.setText(map.get(arrayList.get(position)).getDuration());
        Picasso.get().load(map.get(arrayList.get(position)).getLinkImage()).fit().into(holder.posterIV);

        holder.inviteTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UserList.class);
                intent.putExtra("name", map.get(arrayList.get(position)).getName());
                intent.putExtra("imdb", map.get(arrayList.get(position)).getImdb());
                intent.putExtra("genre", map.get(arrayList.get(position)).getGenre());
                intent.putExtra("duration", map.get(arrayList.get(position)).getDuration());
                intent.putExtra("year", map.get(arrayList.get(position)).getYear());
                intent.putExtra("link", map.get(arrayList.get(position)).getLinkImage());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTV, imdbTV, genreTV, yearTV, durationTV, inviteTV;
        ImageView posterIV, deleteIV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.movieName);
            imdbTV =itemView.findViewById(R.id.imdb);
            genreTV = itemView.findViewById(R.id.genre);
            yearTV = itemView.findViewById(R.id.year);
            durationTV = itemView.findViewById(R.id.duration);
            inviteTV = itemView.findViewById(R.id.invitePersons);
            posterIV = itemView.findViewById(R.id.imageMovie);
        }
    }
}
