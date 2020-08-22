package com.example.artwokmabel.homepage.listing;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwokmabel.HomeGraphDirections;
import com.example.artwokmabel.ProfileGraphDirections;
import com.example.artwokmabel.Utils.TimeWrangler;
import com.example.artwokmabel.Utils.TransactFragment;
import com.example.artwokmabel.databinding.ItemCommentBinding;
import com.example.artwokmabel.databinding.ItemReviewBinding;
import com.example.artwokmabel.homepage.post.CommentsAdapter;
import com.example.artwokmabel.homepage.post.CommentsViewModel;
import com.example.artwokmabel.models.Comment;
import com.example.artwokmabel.models.Review;
import com.example.artwokmabel.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListingReviewsAdapter extends RecyclerView.Adapter<ListingReviewsAdapter.ReviewHolder> {
    private static final String TAG = "CommentListAdapter";

    private List<Review> reviewsList;
    private Context mContext;
    private NavController navController;

    public ListingReviewsAdapter(Context context, NavController navController) {
        this.mContext = context;
        this.navController = navController;
    }

    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        ItemReviewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_review, parent,false);

        return new ReviewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewHolder holder, int i) {
        Review data = reviewsList.get(i);
        holder.binding.setReview(data);
        holder.binding.setOnprofileclicked(new OnProfileClicked());
        holder.binding.setTime(TimeWrangler.changeNanopastToReadableDate(data.getNanopast()));

        Picasso.get().load(data.getPosterurl()).into(holder.binding.reviewProfileImage);

//        if(data.getUser_id().equals(mAuth.getCurrentUser().getUid())){
//            holder.binding.deleteComment.setImageResource(R.drawable.garbage);
//            holder.binding.setOndeleteclicked(new CommentsAdapter.OnDeleteClicked());
//            Log.d("commentshenan", "you and the comment poster are the same");
//        }else{
//            holder.binding.deleteComment.setVisibility(View.GONE);
//            Log.d("commentshenan", "you and the comment poster are different so rubbish bin should not be there");
//        }
    }

    public void setReviewsList(final List<Review> reviews) {
        if (this.reviewsList == null) {
            this.reviewsList = reviews;
            notifyItemRangeInserted(0, reviews.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return ListingReviewsAdapter.this.reviewsList.size();
                }

                @Override
                public int getNewListSize() {
                    return reviews.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return ListingReviewsAdapter.this.reviewsList.get(oldItemPosition).getReviewId().equals(
                            reviews.get(newItemPosition).getReviewId());
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return ListingReviewsAdapter.this.reviewsList.get(oldItemPosition).getReviewId().equals(
                            reviews.get(newItemPosition).getReviewId());
                }
            });
            this.reviewsList = reviews;
            notifyDataSetChanged();
            result.dispatchUpdatesTo(this);
        }
    }


    public class OnDeleteClicked{
        public void onDeleteClicked(Comment comment){
            Log.d("commentshenan", "Does delete comment run");

            //viewModel.deleteComment(comment.getComment_id(), postId, postPosterId);
        }
    }

    public class OnProfileClicked{
        public void onProfileClicked(Review review){
            int currentGraph = navController.getGraph().getId();
            if(currentGraph == R.id.home_graph){
                HomeGraphDirections.ActionGlobalProfileFragment2 action =
                        HomeGraphDirections.actionGlobalProfileFragment2(review.getPosterid());
                navController.navigate(action);
            }else if(currentGraph == R.id.profile_graph){
                ProfileGraphDirections.ActionProfileGraphSelf action =
                        ProfileGraphDirections.actionProfileGraphSelf(review.getPosterid());
                navController.navigate(action);
            }
        }
    }

    class ReviewHolder extends RecyclerView.ViewHolder {
        private ItemReviewBinding binding;

        public ReviewHolder(@NonNull ItemReviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @Override
    public int getItemCount() {
        return reviewsList == null ? 0 : reviewsList.size();
    }
}
