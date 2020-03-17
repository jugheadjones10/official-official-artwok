package com.example.artwokmabel.profile.user;

import android.content.Intent;
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
import com.example.artwokmabel.databinding.FragmentProfileListingsBinding;
import com.example.artwokmabel.homepage.adapters.ListingsAdapter;
import com.example.artwokmabel.models.Listing;
import com.example.artwokmabel.profile.uploadlisting.UploadListingAcitvity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;


public class ProfileListingsFragment extends Fragment {
    private FragmentProfileListingsBinding binding;
    private ListingsAdapter adapter;
    private ProfileListingsViewModel viewModel;
    private String userId;
    private FirebaseAuth mAuth;

    public ProfileListingsFragment(String userId) {
        this.userId = userId;
        this.mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_listings, container, false);

        if(mAuth.getCurrentUser().getUid().equals(userId)){
            binding.setAddlistingcallback(new OnAddListingClicked());
        }else{
            binding.addListingsButton.setVisibility(View.GONE);
        }

        binding.profileListingsRecyclerview.setHasFixedSize(true);
        binding.profileListingsRecyclerview.setLayoutManager(new GridLayoutManager(getContext(), 2));

        adapter = new ListingsAdapter(getActivity());
        binding.profileListingsRecyclerview.setAdapter(adapter);

        return binding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(ProfileListingsViewModel.class);

        observeViewModel(viewModel);
    }

    private void observeViewModel(ProfileListingsViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getListingsObeservable(userId).observe(this, new Observer<List<Listing>>() {
            @Override
            public void onChanged(@Nullable List<Listing> listings) {
                if (listings != null) {
                    adapter.setListingsList(listings);
                }
            }
        });
    }

    public class OnAddListingClicked{
        public void onAddListingClicked(){
            Intent intent = new Intent(getApplicationContext(), UploadListingAcitvity.class);
            startActivity(intent);
        }
    }

}
