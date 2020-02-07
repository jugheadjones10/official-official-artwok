package com.example.artwokmabel.homepage.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwokmabel.homepage.adapters.ListingsAdapter;
import com.example.artwokmabel.homepage.models.Listing;
import com.example.artwokmabel.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeHandmadeFragment extends Fragment {


    private String TAG = "69";
    RecyclerView recyclerView;
    FirebaseFirestore db;
    List<Listing> list = new ArrayList<>();

    private FirebaseAuth mAuth;

    private ArrayList  following = new ArrayList();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_handmade_fragment, container, false);

        recyclerView = view.findViewById(R.id.handmade_recyclerview);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        final ListingsAdapter giftsAdapter = new ListingsAdapter(getActivity());
        recyclerView.setAdapter(giftsAdapter);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        db = FirebaseFirestore.getInstance();

        db.collectionGroup("Listings")
                .whereArrayContains("categories", "Handmade")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        list.clear();
                        for(DocumentSnapshot single: queryDocumentSnapshots){
                            Listing listdata = new Listing(
                                    single.getString("user_id"),
                                    single.getString("return_exchange"),
                                    (long)single.get("price"),
                                    (ArrayList<String>) single.get("photos"),
                                    single.getString("name"),
                                    single.getString("hashtags"),
                                    single.getString("desc"),
                                    single.getString("delivery"),
                                    single.getString("temporary username holder"),
                                    single.getId(),
                                    (long)single.get("nanopast"),
                                    (ArrayList<String>) single.get("categories")
                            );
                            list.add(listdata);

                            class SortMain implements Comparator<Listing> {
                                public int compare(Listing a, Listing b){
                                    return (int)b.getNanopast() - (int)a.getNanopast();
                                }
                            }
                            Collections.sort(list, new SortMain());
                            giftsAdapter.notifyDataSetChanged();
                        }
                    }
                });
        return view;
    }
}
