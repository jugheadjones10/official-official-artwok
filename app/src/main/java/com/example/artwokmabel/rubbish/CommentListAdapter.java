package com.example.artwokmabel.rubbish;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.artwokmabel.homepage.post.CommentsFragment;
import com.example.artwokmabel.models.Comment;
import com.example.artwokmabel.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by User on 8/22/2017.
 */

public class CommentListAdapter extends ArrayAdapter<Comment> {

    private static final String TAG = "CommentListAdapter";

    private LayoutInflater mInflater;
    private int layoutResource;
    private Context mContext;
    private List<Comment> list;

    private FirebaseFirestore DB;
    private String userID;
    private String postID;
    private ArrayList databaseComments;
    private FirebaseAuth mAuth;


    public CommentListAdapter(@NonNull Context context, @LayoutRes int resource,
                              @NonNull List<Comment> objects, FirebaseFirestore db,
                              String userid, String postid, ArrayList databasecomments) {
        super(context, resource, objects);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        layoutResource = resource;
        list = objects;
        DB = db;
        userID = userid;
        postID = postid;
        databaseComments = databasecomments;
    }

    private static class ViewHolder{
        TextView comment, username, timestamp, reply;
        CircleImageView profileImage;
        ImageView like;
    }



    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final ViewHolder holder;
        mAuth = FirebaseAuth.getInstance();

        if(convertView == null){
            convertView = mInflater.inflate(layoutResource, parent, false);
            holder = new ViewHolder();

            holder.comment = (TextView) convertView.findViewById(R.id.comment);
            holder.username = (TextView) convertView.findViewById(R.id.comment_username);
            holder.timestamp = (TextView) convertView.findViewById(R.id.comment_time_posted);
            holder.reply = (TextView) convertView.findViewById(R.id.comment_reply);
            holder.profileImage = (CircleImageView) convertView.findViewById(R.id.comment_profile_image);

            if(mAuth.getCurrentUser().getUid().equals(CommentsFragment.getInstance().userid)){
                holder.like = (ImageView) convertView.findViewById(R.id.comment_like);

                holder.like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        list.remove(position); // remove the item from the data list
                        databaseComments.remove(position);
                        DB.collection("Users")
                                .document(userID)
                                .collection("Posts")
                                .document(postID)
                                .update("comments", databaseComments)
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
                        notifyDataSetChanged();
                    }
                });
            }else{

            }

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        //set the comment
        holder.comment.setText(getItem(position).getComment());

        //set the timestamp difference
//        String timestampDifference = getTimestampDifference(getItem(position));
//        if(!timestampDifference.equals("0")){
//            holder.timestamp.setText(timestampDifference + " d");
//        holder.timestamp.setText(getItem(position).getDate_created());
//        }else{
//            holder.timestamp.setText("today");
//        }

//        holder.like.setOnClickListener(new View.OnClickListener() {
//        @Override
//            public void onClick(View view) {
//
//            }
//        });







        //set the username and profile ic_dm
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//        Query query = reference
//                .child(mContext.getString(R.string.dbname_user_account_settings))
//                .orderByChild(mContext.getString(R.string.field_user_id))
//                .equalTo(getItem(position).getUser_id());
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for ( DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
//                    holder.username.setText(
//                            singleSnapshot.getValue(UserAccountSettings.class).getUsername());
//
//                    ImageLoader imageLoader = ImageLoader.getInstance();
//
//                    imageLoader.displayImage(
//                            singleSnapshot.getValue(UserAccountSettings.class).getProfile_photo(),
//                            holder.profileImage);
//                }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.d(TAG, "onCancelled: query cancelled.");
//            }
//        });

        try{
            if(position == 0){
                //holder.like.setVisibility(View.GONE);
                //holder.likes.setVisibility(View.GONE);
                //holder.reply.setVisibility(View.GONE);
            }
        }catch (NullPointerException e){
            Log.e(TAG, "getView: NullPointerException: " + e.getMessage() );
        }


        return convertView;
    }

    /**
     * Returns a string representing the number of days ago the post was made
     * @return
     */
//    private String getTimestampDifference(Comment comment){
//        Log.d(TAG, "getTimestampDifference: getting timestamp difference.");
//
//        String difference = "";
//        Calendar c = Calendar.getInstance();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
//        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));//google 'android list of timezones'
//        Date today = c.getTime();
//        sdf.format(today);
//        Date timestamp;
//        final String photoTimestamp = comment.getDate_created();
//        try{
//            timestamp = sdf.parse(photoTimestamp);
//            difference = String.valueOf(Math.round(((today.getTime() - timestamp.getTime()) / 1000 / 60 / 60 / 24 )));
//        }catch (ParseException e){
//            Log.e(TAG, "getTimestampDifference: ParseException: " + e.getMessage() );
//            difference = "0";
//        }
//        return difference;
//    }
}






























