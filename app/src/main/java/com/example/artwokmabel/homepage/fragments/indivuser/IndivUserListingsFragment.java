package com.example.artwokmabel.homepage.fragments.indivuser;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.FragmentIndivUserListingsBinding;
import com.example.artwokmabel.homepage.adapters.ListingsAdapter;
import com.example.artwokmabel.homepage.models.Listing;

import java.util.List;

public class IndivUserListingsFragment extends Fragment {

    private String indivUserId;
    private String indivUsername;

    private IndivUserListingsViewModel viewModel;
    private FragmentIndivUserListingsBinding binding;
    private ListingsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_indiv_user_listings, container, false);

        binding.recyclerview.setHasFixedSize(true);
        binding.recyclerview.setLayoutManager(new GridLayoutManager(getContext(), 2));

        adapter = new ListingsAdapter(getActivity());
        binding.recyclerview.setAdapter(adapter);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            indivUserId = bundle.getString("indiv_user_id");
            indivUsername = bundle.getString("indiv_username");
        }

        //Todo: error that indivUserId is null
//        db = FirebaseFirestore.getInstance();
//        db.collection("Users")
//                .document(indivUserId)
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
//                                    indivUsername,
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
//                        Collections.sort(list, new SortMain());
//                        notesAdapter.notifyDataSetChanged();
//                    }
//                });

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(IndivUserListingsViewModel.class);

        observeViewModel(viewModel);
    }

    private void observeViewModel(IndivUserListingsViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getUserListingsObservable().observe(this, new Observer<List<Listing>>() {
            @Override
            public void onChanged(@Nullable List<Listing> listings) {
                if (listings != null) {
                    adapter.setListingsList(listings);
                }
            }
        });
    }
}
