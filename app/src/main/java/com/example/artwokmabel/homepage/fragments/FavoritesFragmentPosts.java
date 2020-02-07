package com.example.artwokmabel.homepage.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwokmabel.homepage.adapters.FavoritesAdapter;
import com.example.artwokmabel.homepage.models.MainPost;
import com.example.artwokmabel.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FavoritesFragmentPosts extends Fragment {
    private RecyclerView recyclerView;
    private FavoritesAdapter adapter;

    private ArrayList<String> favPostIds;
    private ArrayList<MainPost> favPosts;

    FirebaseFirestore db;
    FirebaseAuth mAuth;
    private String TAG = "FavoritesFragmentPosts";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favourites_posts, container, false);
        recyclerView = view.findViewById(R.id.cat_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        favPostIds = new ArrayList<>();
        favPosts = new ArrayList<>();

        adapter = new FavoritesAdapter(getActivity(), favPosts);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        db.collection("Users")
                .document(mAuth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        if (snapshot != null && snapshot.exists()) {
                            Log.d(TAG, " data: " + snapshot.getData());

                            favPosts.clear();
                            favPostIds = (ArrayList<String>) snapshot.getData().get("fav_posts");
                            //Get array list of fav posts
                            Log.d("FIC", favPostIds.toString());
                            for (String postid : favPostIds) {
                                db.collectionGroup("Posts")
                                        .whereEqualTo("postId", postid)
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                for(DocumentSnapshot post: queryDocumentSnapshots){
                                                    MainPost favPost = new MainPost(
                                                            post.getString("user_id"),
                                                            post.getString("desc"),
                                                            post.getString("hashtags"),
                                                            post.getId(),
                                                            "stand-in-username",
                                                            (ArrayList<String>) post.get("photos"),
                                                            post.getString("timestamp"),
                                                            0000
                                                    );
                                                    //Fav favPost = new Fav(postid,  (ArrayList<String>) document.get("photos"));
                                                    Log.d("FIR", "FOOFOFOFFOFO" + post.get("photos").toString());
                                                    favPosts.add(favPost);
                                                    Log.d("FIR", "FOOFOFOFFOFO" + favPosts.toString());

                                                    adapter.notifyDataSetChanged();
                                                }
                                            }

                                        });
                            }

                        } else {
                            Log.d(TAG, " data: null");
                        }
                    }
                });

        return view;
    }


    public void removeFav(){

    }

    public void addFav(){

    }
}
