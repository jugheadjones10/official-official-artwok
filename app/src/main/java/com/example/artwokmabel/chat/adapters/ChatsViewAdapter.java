package com.example.artwokmabel.chat.adapters;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwokmabel.R;
import com.example.artwokmabel.chat.models.Comment;
import com.example.artwokmabel.databinding.ItemChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChatsViewAdapter extends RecyclerView.Adapter<ChatsViewAdapter.MessageViewHolder>{
    private List<Comment> comments;
    private String uid;

    public ChatsViewAdapter() {
        this.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("dod", "I'm gettting inflated hahahah");
        ItemChatBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_chat,parent,false);

        return new MessageViewHolder(binding);
    }

    public void setMessages(final List<Comment> messages) {
        if (this.comments == null) {
            this.comments = messages;
            notifyItemRangeInserted(0, messages.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return ChatsViewAdapter.this.comments.size();
                }

                @Override
                public int getNewListSize() {
                    return messages.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return ChatsViewAdapter.this.comments.get(oldItemPosition).getTimestamp().equals(messages.get(newItemPosition).getTimestamp());
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return ChatsViewAdapter.this.comments.get(oldItemPosition).getTimestamp().equals(messages.get(newItemPosition).getTimestamp());
                }
            });
            this.comments = messages;
            result.dispatchUpdatesTo(this);
        }
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.binding.setComment(comment);
        holder.binding.setCurrentid(uid);

        //내가보낸 메세지
        //Todo: why do i need to check if getSenderId is null?
        if(comment.getSenderId().equals(uid)){
            //messageViewHolder.textView_message.setText(comments.get(position).getMessage());
            holder.binding.messageItemTextviewMessage.setBackgroundResource(R.drawable.my_message);

            //messageViewHolder.linearLayout_destination.setVisibility(View.INVISIBLE);
            //messageViewHolder.linearLayout_main.setGravity(Gravity.RIGHT);
            //messageViewHolder.textView_name.setVisibility(View.INVISIBLE);
            //상대방이 보낸 메세지
        }else {
            Picasso.get().load(comment.getOtherUserPic()).into(holder.binding.messageItemImageviewProfile);

//                Glide.with(holder.itemView.getContext())
//                        .load(comments.get(position).getOtherUserPic())
//                        .apply(new RequestOptions().circleCrop())
//                        .into(messageViewHolder.imageView_profile);
            // messageViewHolder.textView_name.setText(comments.get(position).getSenderId());
            //messageViewHolder.linearLayout_destination.setVisibility(View.VISIBLE);
            holder.binding.messageItemTextviewMessage.setBackgroundResource(R.drawable.their_message);
//                messageViewHolder.textView_message.setText(comments.get(position).getMessage());
            //messageViewHolder.linearLayout_main.setGravity(Gravity.LEFT);

        }
//            long unixTime = (long) comments.get(position).getTimestampId();
//            Date date = new Date(unixTime);
//            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
//            String time = simpleDateFormat.format(date);
//            messageViewHolder.textView_timestamp.setText(time);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return comments == null ? 0 : comments.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public ItemChatBinding binding;

        public MessageViewHolder(ItemChatBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}