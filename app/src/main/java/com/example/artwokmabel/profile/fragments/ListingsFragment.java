package com.example.artwokmabel.profile.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwokmabel.Auth.LoginActivity;
import com.example.artwokmabel.databinding.ActivityIndivListingNewBinding;
import com.example.artwokmabel.databinding.ActivityListingsFragmentBinding;
import com.example.artwokmabel.homepage.adapters.ListingsAdapter;
import com.example.artwokmabel.homepage.models.Listing;
import com.example.artwokmabel.profile.Activites.AddListingActivity;
import com.example.artwokmabel.R;
import com.example.artwokmabel.profile.Adapters.ProfileListingsAdapter;
import com.example.artwokmabel.profile.Models.ProfileListings;
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

import static com.facebook.FacebookSdk.getApplicationContext;


public class ListingsFragment extends Fragment {

    RecyclerView recyclerView;
    List<ProfileListings> profileListingsList;

    ActivityListingsFragmentBinding binding;

    public ListingsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.activity_listings_fragment, container, false);


        binding.addListingsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), AddListingActivity.class);
                startActivity(intent);
            }
        });

        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        final ListingsAdapter notesAdapter = new ListingsAdapter(getActivity());
        recyclerView.setAdapter(notesAdapter);

//        profileListingsList = new ArrayList<>();
//        profileListingsList.add(new ProfileListings("Emerald Necklace", "SGD 2,000", R.drawable.handmade_jewelry));
//        profileListingsList.add(new ProfileListings("Stuffed Tiger","SGD 26", R.drawable.tiger));
//        profileListingsList.add(new ProfileListings("Chocolate Cake","SGD 81", R.drawable.chocolate_cake));
//        profileListingsList.add(new ProfileListings("Goyard","SGD 25,000", R.drawable.bags));
//        profileListingsList.add(new ProfileListings("Jean Castille","SGD 430", R.drawable.paintings));
//        profileListingsList.add(new ProfileListings("Forkie","SGD 130", R.drawable.toys));
//
//        ProfileListingsAdapter adapter = new ProfileListingsAdapter(getContext(), profileListingsList);
//        binding.profileListingsRecyclerview.setLayoutManager(new GridLayoutManager(getContext(), 2));
//        binding.profileListingsRecyclerview.setAdapter(adapter);



//    RecyclerView recyclerView;
//    FirebaseFirestore db;
//    FirebaseAuth mAuth;
//    List<Listing> list = new ArrayList<>();
//
//    public ListingsFragment() {
//
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        View view = inflater.inflate(R.layout.activity_listings_fragment, container, false);
//
//        Button add_listing_button = (Button) view.findViewById(R.id.add_listings_button);
//
//        add_listing_button.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                Intent intent = new Intent(getApplicationContext(), AddListingActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        recyclerView = view.findViewById(R.id.profile_listings_recyclerview);
//        recyclerView.setHasFixedSize(true);
//
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(layoutManager);
//
//        final ListingsAdapter notesAdapter = new ListingsAdapter(getActivity(), list);
//        recyclerView.setAdapter(notesAdapter);
//
//        LoginActivity.uid = mAuth.getInstance().getCurrentUser().getUid();
//
//        db = FirebaseFirestore.getInstance();
//        db.collection("Users")
//                .document(LoginActivity.uid)
//                .collection("Listings")
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//
//                        if (e != null) {
//                            Log.w("TAG", "Listen failed.", e);
//                            return;
//                        }
//
//                        list.clear();
//                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
//                            Listing listdata = new Listing(
//                                    doc.getString("user_id"),
//                                    doc.getString("return_exchange"),
//                                    (long)doc.get("price"),
//                                    (ArrayList<String>) doc.get("photos"),
//                                    doc.getString("name"),
//                                    doc.getString("hashtags"),
//                                    doc.getString("desc"),
//                                    doc.getString("delivery"),
//                                    "placeholder username",
//                                    doc.getId(),
//                                    (long) doc.get("nanopast"),
//                                    (ArrayList<String>) doc.get("categories")
//                            );
//                            list.add(listdata);
//                            Log.d("TAG SHIT SHIT ShIT", "This is apparently document id for posts" + doc.getId());
//                        }
//
//                        class SortMain implements Comparator<Listing> {
//                            public int compare(Listing a, Listing b){
//                                return (int)b.getNanopast() - (int)a.getNanopast();
//                            }
//                        }
//
//                        Collections.sort((ArrayList)list, new SortMain());
//                        notesAdapter.notifyDataSetChanged();
//                    }
//                });
//
        return view;
    }
}
