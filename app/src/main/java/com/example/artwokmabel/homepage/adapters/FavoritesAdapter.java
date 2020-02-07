package com.example.artwokmabel.homepage.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwokmabel.homepage.Activities.IndivPostActivity;
import com.example.artwokmabel.homepage.models.MainPost;
import com.example.artwokmabel.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavHolder> {

    private Context context;
    public ArrayList<MainPost> favs;
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    //private static FavoritesAdapter instance = null;

//    public static FavoritesAdapter getInstance(){
//        return instance;
//    }

//    public void removeFavItem(String postid){
//        for(MainPost favitem : favs){
//            if(favitem.getPostId() == postid){
//                favs.remove(favitem);
//            }
//        }
//    }
//
//    public void addFavItem(String postid, ArrayList<String> photos){
//        //MainPost newFav = new MainPost(postid, photos);
//        favs.add(newFav);
//        db = FirebaseFirestore.getInstance();
//        mAuth = FirebaseAuth.getInstance();
//
//        db.collection("Users")
//                .document( mAuth.getCurrentUser().getUid())
//                .update("fav_posts", FieldValue.arrayUnion(postid));
//    }

    public FavoritesAdapter(Context context, ArrayList<MainPost> favs) {
//        instance = this;
        Log.d("goddmamnImages", favs.toString());
        this.context = context;
        this.favs = favs;
    }

    @NonNull
    @Override
    public FavHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.fav_item, parent, false);
        return new FavHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavHolder holder, int position) {
        MainPost fav = favs.get(position);

        ArrayList<String> images = fav.getPhotos();
        Log.d("goddmamnImages", images.toString());

        ImageListener imageListener = new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                //imageView.setImageResource(sampleImages[position]);
                Picasso.get()
                        .load(images.get(position))
                        .placeholder(R.drawable.loading_image)
                        .error(R.drawable.rick_and_morty)
                        .into(imageView);
            }
        };

        if(images != null){
            holder.photos.setPageCount(images.size());
            holder.photos.setImageListener(imageListener);
        }

        //Below not working????
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ADW", "I got cliked");
                Intent intent = new Intent(context, IndivPostActivity.class);
                intent.putExtra("description", fav.getDesc());
                intent.putExtra("hashtags", fav.getHashtags());
                intent.putExtra("postid", fav.getPostId());
                intent.putExtra("userid", fav.getUser_id());
                intent.putExtra("username", fav.getUsername());
                intent.putExtra("photos", fav.getPhotos());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favs.size();
    }

    class FavHolder extends RecyclerView.ViewHolder {

        private CarouselView photos;
        private CardView parentLayout;

        FavHolder(View itemView) {
            super(itemView);
            photos = itemView.findViewById(R.id.carouselView);
            parentLayout = itemView.findViewById(R.id.parent_fav_layout);
        }
    }

//    public ArrayList<Category> getSelected() {
//        ArrayList<Category> selected = new ArrayList<>();
//        for (int i = 0; i < categories.size(); i++) {
//            if (categories.get(i).isChecked()) {
//                selected.add(categories.get(i));
//            }
//        }
//        return selected;
//    }

}
