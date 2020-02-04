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

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.FragmentProfilePostsBinding;
import com.example.artwokmabel.homepage.adapters.PostsAdapter;
import com.example.artwokmabel.models.MainPost;
import com.example.artwokmabel.profile.uploadpost.UploadPostActivity;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ProfilePostsFragment extends Fragment {
    FragmentProfilePostsBinding binding;
    ProfilePostsViewModel viewModel;
    PostsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_posts, container, false);

        binding.setCallback(new OnUploadButtonClicked());
        binding.recyclerview.setHasFixedSize(true);

        adapter = new PostsAdapter(getContext());
        binding.recyclerview.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(ProfilePostsViewModel.class);

        observeViewModel(viewModel);
    }

    private void observeViewModel(ProfilePostsViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getUserPostsObeservable().observe(this, new Observer<List<MainPost>>() {
            @Override
            public void onChanged(@Nullable List<MainPost> categories) {
                if (categories != null) {
                    adapter.setPostsList(categories);
                }
            }
        });
    }

    public class OnUploadButtonClicked{
        public void onUploadButtonClicked(){
            Intent intent = new Intent(getApplicationContext(), UploadPostActivity.class);
            startActivity(intent);
        }
    }

}
