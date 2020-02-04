package com.example.artwokmabel.homepage.listing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwokmabel.models.Review;
import com.example.artwokmabel.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListingReviewsAdapter extends RecyclerView.Adapter<ListingReviewsAdapter.reviewHolder> {

    private static final String TAG = "CommentListAdapter";

    private Context mContext;
    private List<Review> list;

    private FirebaseFirestore DB;
    private FirebaseAuth mAuth;


    public ListingReviewsAdapter(Context context, List<Review> reviews) {
        mContext = context;
        list = reviews;
    }

    @NonNull
    @Override
    public ListingReviewsAdapter.reviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_review,viewGroup,false);

        ListingReviewsAdapter.reviewHolder myHolder = new ListingReviewsAdapter.reviewHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListingReviewsAdapter.reviewHolder myHolder, int i) {
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
