//package com.example.artwokmabel.HomePage.Adapters;
//
//import android.content.Context;
//import android.content.Intent;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.cardview.widget.CardView;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.artwokmabel.HomePage.Activities.IndivPostActivity;
//import com.example.artwokmabel.HomePage.Models.MainGift;
//import com.example.artwokmabel.HomePage.Models.MainPost;
//import com.example.artwokmabel.R;
//import com.squareup.picasso.Picasso;
//import com.synnapps.carouselview.CarouselView;
//import com.synnapps.carouselview.ImageClickListener;
//import com.synnapps.carouselview.ImageListener;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class GiftAdapter extends RecyclerView.Adapter<GiftAdapter.myHolder> {
//    private Context mContext;
//
//    List<MainGift> giftList;
//
//    public GiftAdapter(Context context, List<MainGift> postList) {
//        this.giftList = giftList;
//        this.mContext = context;
//    }
//
//    @NonNull
//    @Override
//    public myHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_posts,viewGroup,false);
//        myHolder myHolder = new myHolder(view);
//        return myHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull myHolder myHolder, int i) {
//        MainGift data = giftList.get(i);
//
//        ArrayList<String> images = data.getPhotos();
//        ImageListener imageListener = new ImageListener() {
//            @Override
//            public void setImageForPosition(int position, ImageView imageView) {
//                //imageView.setImageResource(sampleImages[position]);
//                Picasso.get()
//                        .load(images.get(position))
//                        .placeholder(R.drawable.user)
//                        .error(R.drawable.rick_and_morty)
//                        .into(imageView);
//            }
//        };
//        myHolder.carouselView.setPageCount(images.size());
//        myHolder.carouselView.setImageListener(imageListener);
//
//
//        myHolder.userid.setText(data.getHashtags());
//        myHolder.username.setText(data.getName());
//        myHolder.desc.setText(data.getDesc());
//
//        myHolder.carouselView.setImageClickListener(new ImageClickListener() {
//            @Override
//            public void onClick(int position) {
//                Intent intent = new Intent(mContext, IndivPostActivity.class);
//                intent.putExtra("description", data.getDesc());
//                intent.putExtra("hashtags", data.getHashtags());
//                intent.putExtra("delivery", data.getDelivery());
//                intent.putExtra("price", data.getPrice());
//                intent.putExtra("name", data.getName());
//
//                intent.putExtra("photos", data.getPhotos());
//                mContext.startActivity(intent);
//            }
//        });
//
//        myHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("TAG fuck's sake", "onClick: clicked on: " + i);
//
//                //Toast.makeText(mContext, mImageNames.get(position), Toast.LENGTH_SHORT).show();
//
//                Intent intent = new Intent(mContext, IndivPostActivity.class);
//                intent.putExtra("description", data.getDesc());
//                intent.putExtra("hashtags", data.getHashtags());
//                intent.putExtra("postid", data.getpostId());
//                intent.putExtra("userid", data.getId());
//                intent.putExtra("username", data.getUsername());
//
//                intent.putExtra("photos", data.getPhotos());
//                mContext.startActivity(intent);
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return giftList.size();
//    }
//
//    class myHolder extends RecyclerView.ViewHolder {
//        TextView desc, userid, username;
//        ImageView postimg;
//        CardView parentLayout;
//
//        CarouselView carouselView;
//
//        public myHolder(@NonNull View itemView) {
//            super(itemView);
//            userid =itemView.findViewById(R.id.userid);
//            desc=itemView.findViewById(R.id.desc);
//            username=itemView.findViewById(R.id.poster_name);
//
//            carouselView = itemView.findViewById(R.id.carouselView);
//
//            parentLayout = itemView.findViewById(R.id.parent_layout_card);
//        }
//    }
//}
//
