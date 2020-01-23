package com.example.artwokmabel.homepage.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwokmabel.homepage.adapters.CatCardsAdapter;
import com.example.artwokmabel.homepage.models.Category;
import com.example.artwokmabel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class    FavoritesFragmentListings extends Fragment {

    private RecyclerView recyclerView;
    private CatCardsAdapter adapter;
    private ArrayList<Category> catsArrayList;
    FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favourites_listings, container, false);

        recyclerView = view.findViewById(R.id.cat_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        catsArrayList = new ArrayList<>();
//        adapter = new CatCardsAdapter(getActivity(), catsArrayList);
        adapter = new CatCardsAdapter(getContext());
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        db.collection("Meta")
                .document("categories")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                document.getData().getClass();
                                HashMap<String, Object> map = (HashMap<String, Object>)document.getData();

                                if (map != null) {
                                    for (Object entry : map.values()) {
                                        Category cat = new Category(
                                                ((HashMap<String, String>) entry).get("title"),
                                                ((HashMap<String, String>) entry).get("ic_dm")
                                        );
                                        catsArrayList.add(cat);
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            } else {
                                Log.d("Edit Categories", "No such document");
                            }
                        } else {
                            Log.d("Edit Categories", "get failed with ", task.getException());
                        }
                    }
                });

        return view;
    }

    private void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}
