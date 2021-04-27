package com.usccsci571dhruv.uscfilms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailsCastAdapter extends RecyclerView.Adapter<DetailsCastAdapter.ViewHolder> {

    private ArrayList<DetailsClasses.Cast> castArrayList;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        CircleImageView img;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            view = itemView;
            img = view.findViewById(R.id.cast_img);
            name = view.findViewById(R.id.cast_name);
        }
    }

    public DetailsCastAdapter(Context applicationContext, ArrayList<DetailsClasses.Cast> casts) {
        context = applicationContext;
        castArrayList = casts;
    }

    @NonNull
    @Override
    public DetailsCastAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.details_cast_layout, parent, false);

        return new DetailsCastAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsCastAdapter.ViewHolder holder, int position) {
        DetailsClasses.Cast cast = castArrayList.get(position);

        Glide.with(holder.itemView)
                .load(cast.profile_path)
                .into(holder.img);

        holder.name.setText(cast.name);
    }

    @Override
    public int getItemCount() {
        return castArrayList.size();
    }
}
