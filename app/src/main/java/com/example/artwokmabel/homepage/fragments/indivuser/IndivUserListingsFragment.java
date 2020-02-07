package com.example.artwokmabel.homepage.fragments.indivuser;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwokmabel.homepage.adapters.ListingsAdapter;
import com.example.artwokmabel.homepage.models.Listing;
import com.example.artwokmabel.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class IndivUserListingsFragment extends Fragment {

    RecyclerView recyclerView;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    List<Listing> list = new ArrayList<>();

    private String indivUserId;
    private String indivUsername;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_indiv_user_listings, container, false);

        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        final ListingsAdapter notesAdapter = new ListingsAdapter(getActivity());
        recyclerView.setAdapter(notesAdapter);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            indivUserId = bundle.getString("indiv_user_id");
            indivUsername = bundle.getString("indiv_username");
        }

        //Todo: error that indivUserId is null
        db = FirebaseFirestore.getInstance();
        db.collection("Users")
                .document(indivUserId)
                .collection("Listings")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        if (e != null) {
                            Log.w("TAG", "Listen failed.", e);
                            return;
                        }

                        list.clear();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            Listing listdata = new Listing(
                                    doc.getString("user_id"),
                                    doc.getString("return_exchange"),
                                    (long)doc.get("price"),
                                    (ArrayList<String>) doc.get("photos"),
                                    doc.getString("name"),
                                    doc.getString("hashtags"),
                                    doc.getString("desc"),
                                    doc.getString("delivery"),
                                    indivUsername,
                                    doc.getId(),
                                    (long) doc.get("nanopast"),
                                    (ArrayList<String>) doc.get("categories")
                            );
                            list.add(listdata);
                            Log.d("TAG SHIT SHIT ShIT", "This is apparently document id for posts" + doc.getId());
                        }

                        class SortMain implements Comparator<Listing> {
                            public int compare(Listing a, Listing b){
                                return (int)b.getNanopast() - (int)a.getNanopast();
                            }
                        }
                        Collections.sort(list, new SortMain());
                        notesAdapter.notifyDataSetChanged();
                    }
                });

        return view;
    }

    // for expandable list
//    private void initListData() {
//        listGroup.add(getString(R.string.orderdelivery));
//        listGroup.add(getString(R.string.orderrefund));
//
//        String[] array;
//
//        List<String> delivery = new ArrayList<>();
//        array = getResources().getStringArray(R.array.orderdelivery);
//        for (String item : array) {
//            delivery.add(item);
//        }
//
//        List<String> refund = new ArrayList<>();
//        array = getResources().getStringArray(R.array.orderrefund);
//        for (String item : array) {
//            refund.add(item);
//        }
//
//        listItem.put(listGroup.get(0), delivery);
//        listItem.put(listGroup.get(1), refund);
//        adapter.notifyDataSetChanged();
//    }
}
