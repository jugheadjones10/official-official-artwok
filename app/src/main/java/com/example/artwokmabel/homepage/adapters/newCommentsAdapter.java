package com.example.artwokmabel.homepage.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.artwokmabel.homepage.fragments.newViewCommentsFragment;
import com.example.artwokmabel.homepage.models.Comment;
import com.example.artwokmabel.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class newCommentsAdapter extends RecyclerView.Adapter<newCommentsAdapter.commentHolder> {

    private static final String TAG = "CommentListAdapter";

    private Context mContext;
    private List<Comment> list;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;


    public newCommentsAdapter(Context context, List<Comment> comments) {
        this.mContext = context;
        this.list = comments;
    }

    @NonNull
    @Override
    public newCommentsAdapter.commentHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_comment,viewGroup,false);

        newCommentsAdapter.commentHolder myHolder = new newCommentsAdapter.commentHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull newCommentsAdapter.commentHolder myHolder, int i) {
        Comment data = list.get(i);

        Log.d("DIC", data.toString());
        myHolder.comment.setText(data.getComment());
        myHolder.username.setText(data.getUsername());
        Picasso.get().load(data.getPoster_url()).into(myHolder.profileImg);

        myHolder.reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser().getUid().equals(data.getUser_id())){
            myHolder.rubbish.setImageResource(R.drawable.garbage);
            myHolder.rubbish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    list.remove(i); // remove the item from the data list
                    //newViewCommentsFragment.getInstance().mComments.remove(i);
                    newViewCommentsFragment.getInstance().adapter.notifyDataSetChanged();

                    db = FirebaseFirestore.getInstance();
                    mAuth = FirebaseAuth.getInstance();
                    db.collection("Users")
                            .document(mAuth.getCurrentUser().getUid())
                            .collection("Posts")
                            .document(newViewCommentsFragment.getInstance().postid)
                            .collection("Comments")
                            .document(data.getComment_id())
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing document", e);
                                }
                            });
                }
            });

        }else{
            myHolder.rubbish.setImageResource(0);
        }

    }


    class commentHolder extends RecyclerView.ViewHolder {
        TextView comment, username, reply;
        CircleImageView profileImg;
        ImageView rubbish;


        public commentHolder(@NonNull View itemView) {
            super(itemView);
            comment = itemView.findViewById(R.id.comment);
            username = itemView.findViewById(R.id.comment_username);
            reply = itemView.findViewById(R.id.comment_reply);
            profileImg = itemView.findViewById(R.id.comment_profile_image);
            rubbish = itemView.findViewById(R.id.comment_like);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
