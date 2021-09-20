package com.example.movies.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movies.R;
import com.example.movies.data.Trailer;

import java.util.ArrayList;
import java.util.Objects;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private ArrayList<Trailer> trailers;
    private onTrailerClickListener onTrailerClickListener;

    public TrailerAdapter(ArrayList<Trailer> trailers) {
        setTrailers(trailers);
    }

    public void setTrailers(ArrayList<Trailer> trailers) {
        this.trailers = trailers;
        notifyDataSetChanged();
    }

    public void setOnTrailerClickListener(TrailerAdapter.onTrailerClickListener onTrailerClickListener) {
        this.onTrailerClickListener = onTrailerClickListener;
    }

    public interface onTrailerClickListener {
        void onTrailerClick(String url);
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.trailer_item, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        Trailer trailer = trailers.get(position);

        holder.name.setText(trailer.getName());
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private ImageView play;

        public TrailerViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.textview_trailer_name);
            play = itemView.findViewById(R.id.textview_trailer_play);

            itemView.setOnClickListener(view -> {
                if (Objects.nonNull(onTrailerClickListener)) {
                    Trailer trailer = trailers.get(getAdapterPosition());
                    onTrailerClickListener.onTrailerClick(trailer.getKey());
                }
            });
        }
    }
}
