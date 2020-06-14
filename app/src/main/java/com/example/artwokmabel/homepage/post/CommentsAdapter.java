package com.example.artwokmabel.homepage.post;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwokmabel.HomeGraphDirections;
import com.example.artwokmabel.ProfileGraphDirections;
import com.example.artwokmabel.Utils.TransactFragment;
import com.example.artwokmabel.databinding.ItemCommentBinding;
import com.example.artwokmabel.databinding.ItemNormalListingBinding;
import com.example.artwokmabel.models.Comment;
import com.example.artwokmabel.R;
import com.example.artwokmabel.models.Listing;
import com.example.artwokmabel.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentHolder> {

    private static final String TAG = "CommentListAdapter";

    private List<Comment> commentsList;
    private CommentsViewModel viewModel;
    private String postId;
    private String postPosterId;
    private Context mContext;
    private NavController navController;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    public CommentsAdapter(Context context, String postId, String postPosterId, NavController navController) {
        this.mContext = context;
        this.mAuth = FirebaseAuth.getInstance();
        this.viewModel = ViewModelProviders.of((FragmentActivity)context).get(CommentsViewModel.class);
        this.postId = postId;
        this.postPosterId = postPosterId;
        this.navController = navController;
    }

    @NonNull
    @Override
    public CommentsAdapter.CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        ItemCommentBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_comment, parent,false);

        return new CommentHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int i) {
        Comment data = commentsList.get(i);
        holder.binding.setComment(data);
        holder.binding.setOnprofileclicked(new OnProfileClicked());

        Picasso.get().load(data.getPoster_url()).into(holder.binding.commentProfileImage);

        if(data.getUser_id().equals(mAuth.getCurrentUser().getUid())){
            holder.binding.deleteComment.setImageResource(R.drawable.garbage);
            holder.binding.setOndeleteclicked(new OnDeleteClicked());
            Log.d("commentshenan", "you and the comment poster are the same");
        }else{
            holder.binding.deleteComment.setVisibility(View.GONE);
            Log.d("commentshenan", "you and the comment poster are different so rubbish bin should not be there");
        }


//        mAuth = FirebaseAuth.getInstance();
//        if(mAuth.getCurrentUser().getUid().equals(data.getUser_id())){
//            holder.rubbish.setImageResource(R.drawable.garbage);
//            holder.rubbish.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    commenstList.remove(i); // remove the item from the data commenstList
//                    //CommentsFragment.getInstance().mComments.remove(i);
//                    CommentsFragment.getInstance().adapter.notifyDataSetChanged();
//
//                    db = FirebaseFirestore.getInstance();
//                    mAuth = FirebaseAuth.getInstance();
//                    db.collection("Users")
//                            .document(mAuth.getCurrentUser().getUid())
//                            .collection("Posts")
//                            .document(CommentsFragment.getInstance().postid)
//                            .collection("Comments")
//                            .document(data.getComment_id())
//                            .delete()
//                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//                                    Log.d(TAG, "DocumentSnapshot successfully written!");
//                                }
//                            })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Log.w(TAG, "Error writing document", e);
//                                }
//                            });
//                }
//            });
//
//        }else{
//            holder.rubbish.setImageResource(0);
//        }

    }

    public void setCommentsList(final List<Comment> comments) {
        if (this.commentsList == null) {
            this.commentsList = comments;
            notifyItemRangeInserted(0, comments.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return CommentsAdapter.this.commentsList.size();
                }

                @Override
                public int getNewListSize() {
                    return comments.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return CommentsAdapter.this.commentsList.get(oldItemPosition).getComment_id().equals(
                            comments.get(newItemPosition).getComment_id());
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return CommentsAdapter.this.commentsList.get(oldItemPosition).getComment_id().equals(
                            comments.get(newItemPosition).getComment_id());
                }
            });
            this.commentsList = comments;
            notifyDataSetChanged();
            result.dispatchUpdatesTo(this);
        }
    }
    
    
    public class OnDeleteClicked{
        public void onDeleteClicked(Comment comment){
            Log.d("commentshenan", "Does delete comment run");

            viewModel.deleteComment(comment.getComment_id(), postId, postPosterId);
        }
    }

    public class OnProfileClicked{
        public void onProfileClicked(Comment comment){
            int currentGraph = navController.getGraph().getId();
            if(currentGraph == R.id.home_graph){
                HomeGraphDirections.ActionGlobalProfileFragment action =
                        HomeGraphDirections.actionGlobalProfileFragment(comment.getUser_id());
                navController.navigate(action);
            }else if(currentGraph == R.id.profile_graph){
                ProfileGraphDirections.ActionProfileGraphSelf action =
                        ProfileGraphDirections.actionProfileGraphSelf(comment.getUser_id());
                navController.navigate(action);
            }
        }
    }

    class CommentHolder extends RecyclerView.ViewHolder {
        private ItemCommentBinding binding;

        public CommentHolder(@NonNull ItemCommentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @Override
    public int getItemCount() {
        return commentsList == null ? 0 : commentsList.size();
    }
}
