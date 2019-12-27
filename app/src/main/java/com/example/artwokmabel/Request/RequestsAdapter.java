package com.example.artwokmabel.Request;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwokmabel.R;

import java.util.ArrayList;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.ViewHolder> {

    ArrayList<RequestsModel> requestsModels;
    Context context;

    public RequestsAdapter(Context context, ArrayList<RequestsModel> requestsModels) {
        this.context = context;
        this.requestsModels = requestsModels;
    }

    @NonNull
    @Override
    public RequestsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_requests, parent, false);
        return new RequestsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestsAdapter.ViewHolder holder, int position) {
        // set Logo to Imageview
        holder.imageView.setImageResource(requestsModels.get(position).getRequestImages());
        // set Name to Textview
        holder.textView.setText(requestsModels.get(position).getRequestName());
        // set Name to Textview2
        holder.textView2.setText(requestsModels.get(position).getRequestUsername());
        // set Name to Textview3
        holder.textView3.setText(requestsModels.get(position).getRequestPrice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), IndivRequestsActivity.class);

                ActivityOptions activityOptions = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    activityOptions = ActivityOptions.makeCustomAnimation(view.getContext(), R.anim.fromright, R.anim.toleft);
                    context.startActivity(intent, activityOptions.toBundle());
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return requestsModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // Initialize Variable
        ImageView imageView;
        TextView textView;
        TextView textView2;
        TextView textView3;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.requestsImages);
            textView = itemView.findViewById(R.id.requestsNames);
            textView2 = itemView.findViewById(R.id.requestsUsernames);
            textView3 = itemView.findViewById(R.id.requestsPrices);
        }
    }
}
