package com.example.artwokmabel.settings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwokmabel.models.SettingsModel;
import com.example.artwokmabel.R;

import java.util.ArrayList;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.ViewHolder> {

    ArrayList<SettingsModel> settingsModels;
    Context context;

    public SettingsAdapter(Context context, ArrayList<SettingsModel> settingsModels) {
        this.context = context;
        this.settingsModels = settingsModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_settings, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // set Logo to Imageview
        holder.imageView.setImageResource(settingsModels.get(position).getLangLogo());
        // set Name to Textview
        holder.textView.setText(settingsModels.get(position).getLangName());
    }

    @Override
    public int getItemCount() {
        return settingsModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // Initialize Variable
        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.settings_imageview);
            textView = itemView.findViewById(R.id.settings_textview);
        }
    }
}
