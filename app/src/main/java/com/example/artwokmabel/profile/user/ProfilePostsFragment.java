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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.FragmentProfilePostsBinding;
import com.example.artwokmabel.homepage.adapters.PostsAdapter;
import com.example.artwokmabel.models.MainPost;
import com.example.artwokmabel.profile.uploadpost.UploadPostActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ProfilePostsFragment extends Fragment {
    private FragmentProfilePostsBinding binding;
    private ProfilePostsViewModel viewModel;
    private PostsAdapter adapter;
    private String userId;
    private FirebaseAuth mAuth;
    private NavController navController;

    public ProfilePostsFragment(String userId){
        this.userId = userId;
        this.mAuth = FirebaseAuth.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_posts, container, false);

        if(mAuth.getCurrentUser().getUid().equals(userId)){
            binding.setCallback(new OnUploadButtonClicked());
        }else{
            binding.postUploadBut.setVisibility(View.GONE);
        }

        binding.recyclerview.setHasFixedSize(true);

        adapter = new PostsAdapter(getContext());
        binding.recyclerview.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(ProfilePostsViewModel.class);

        observeViewModel(viewModel);
    }

    private void observeViewModel(ProfilePostsViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getUserPostsObeservable(userId).observe(this, new Observer<List<MainPost>>() {
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
            navController.navigate(R.id.action_profile_graph_to_uploadPostFragment);
//            Intent intent = new Intent(getApplicationContext(), UploadPostActivity.class);
//            startActivity(intent);
        }
    }

}
