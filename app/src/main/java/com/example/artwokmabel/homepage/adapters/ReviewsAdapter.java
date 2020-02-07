package com.example.artwokmabel.homepage.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwokmabel.homepage.models.Review;
import com.example.artwokmabel.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.reviewHolder> {

    private static final String TAG = "CommentListAdapter";

    private Context mContext;
    private List<Review> list;

    private FirebaseFirestore DB;
    private FirebaseAuth mAuth;


    public ReviewsAdapter(Context context, List<Review> reviews) {
        mContext = context;
        list = reviews;
    }

    @NonNull
    @Override
    public ReviewsAdapter.reviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_review,viewGroup,false);

        ReviewsAdapter.reviewHolder myHolder = new ReviewsAdapter.reviewHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsAdapter.reviewHolder myHolder, int i) {
        Review data = list.get(i);

        myHolder.review.setText(data.getReview());
        myHolder.username.setText(data.getUsername());
        Picasso.get().load(data.getPosterurl()).into(myHolder.profileImg);

        myHolder.reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }


    class reviewHolder extends RecyclerView.ViewHolder {
        TextView review, username, reply;
        CircleImageView profileImg;

        public reviewHolder(@NonNull View itemView) {
            super(itemView);
            review = itemView.findViewById(R.id.comment);
            username = itemView.findViewById(R.id.comment_username);
            reply = itemView.findViewById(R.id.comment_reply);
            profileImg = itemView.findViewById(R.id.comment_profile_image);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
