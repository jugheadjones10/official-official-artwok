package com.example.artwokmabel.profile.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.FragmentProfileBinding;
import com.example.artwokmabel.databinding.ViewProfileToolbarMeBinding;
import com.example.artwokmabel.databinding.ViewProfileToolbarOthersBinding;
import com.example.artwokmabel.models.User;
import com.example.artwokmabel.profile.people.PeopleActivity;
import com.example.artwokmabel.profile.settings.SettingsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    private ProfileFragmentViewModel viewModel;
    private FragmentProfileBinding binding;
    private FirebaseAuth mAuth;
    private String userId;

    private ViewProfileToolbarMeBinding toolbarMeBinding;
    private ViewProfileToolbarOthersBinding toolbarOthersBinding;

    public ProfileFragment(){}

    public ProfileFragment(String userId){
        this.userId = userId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();

        if(userId == null){
            userId = mAuth.getCurrentUser().getUid();
        }

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        binding.profileTab.bringToFront();

        if(mAuth.getCurrentUser().getUid().equals(userId)){
            binding.profileToolbar.setOnInflateListener(new ViewStub.OnInflateListener() {
                @Override
                public void onInflate(ViewStub stub, View inflated) {
                    toolbarMeBinding = DataBindingUtil.bind(inflated);
                    toolbarMeBinding.setOnpeopleclicked(new OnPeopleClicked());
                    toolbarMeBinding.setOnsettingsclicked(new OnSettingsClicked());
                }
            });

            binding.profileToolbar.getViewStub().setLayoutResource(R.layout.view_profile_toolbar_me);
        }else {
            binding.profileToolbar.setOnInflateListener(new ViewStub.OnInflateListener() {
                @Override
                public void onInflate(ViewStub stub, View inflated) {
                    toolbarOthersBinding = DataBindingUtil.bind(inflated);
                    toolbarOthersBinding.setOnfollclicked(new OnFollClicked());
                    toolbarOthersBinding.setOnmenuclicked(new OnMenuClicked());
                }
            });

            binding.profileToolbar.getViewStub().setLayoutResource(R.layout.view_profile_toolbar_others);
        }

        if (!binding.profileToolbar.isInflated()) {
            binding.profileToolbar.getViewStub().inflate();
        }


        ProfileFragmentPagerAdapter adapter = new ProfileFragmentPagerAdapter(getChildFragmentManager());
        adapter.AddFragment(new ProfileListingsFragment(userId),"Listings");
        adapter.AddFragment(new ProfilePostsFragment(userId),"Posts");
        adapter.AddFragment(new DashboardFragment(userId),"Dashboard");

        // Adapter setup
        binding.profileViewpager.setAdapter(adapter);
        binding.profileTab.setupWithViewPager(binding.profileViewpager);

        viewModel = ViewModelProviders.of(this).get(ProfileFragmentViewModel.class);
        viewModel.getUserObservable(userId).observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                binding.setUser(user);
                Picasso.get().load(user.getProfile_url()).into(binding.profilePicture);

                if(!mAuth.getCurrentUser().getUid().equals(userId)){
                    viewModel.getUserObservable(mAuth.getCurrentUser().getUid()).observe(getViewLifecycleOwner(), new Observer<User>() {
                        @Override
                        public void onChanged(User me) {
                            if(me.getFollowing().contains(user.getUid())){
                                toolbarOthersBinding.follButton.setText("Following");
                            }else{
                                toolbarOthersBinding.follButton.setText("Follow");
                            }
                        }
                    });
                }
            }
        });

        viewModel.getNumUserListings(userId).observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer numListings) {
                binding.setNumlistings(numListings);
            }
        });

        viewModel.getNumUserPosts(userId).observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer numPosts) {
                binding.setNumposts(numPosts);
            }
        });

        return binding.getRoot();
    }

    public class OnPeopleClicked{
        public void onPeopleClicked(){
            Intent intent = new Intent(getContext(), PeopleActivity.class);
            startActivity(intent);
        }
    }

    public class OnSettingsClicked{
        public void onSettingsClicked(){
            Intent intent = new Intent(getContext(), SettingsActivity.class);
            startActivity(intent);
        }
    }

    public class OnMenuClicked{
        public void onMenuClicked(){

        }
    }

    public class OnFollClicked{
        public void onFollClicked(){
            if(toolbarOthersBinding.follButton.getText().toString().equals("Following")){
                toolbarOthersBinding.follButton.setText("Follow");

                viewModel.removeUserFollowing(mAuth.getCurrentUser().getUid(), userId);
            }else{
                toolbarOthersBinding.follButton.setText("Following");

                viewModel.addUserFollowing(mAuth.getCurrentUser().getUid(), userId);
            }
        }
    }
}
