package com.example.artwokmabel.profile.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.paging.PagedList;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.FragmentProfilePostsBinding;
import com.example.artwokmabel.homepage.adapters.PostsAdapter;
import com.example.artwokmabel.homepage.postsfeed.FirestorePagingAdapterImpl;
import com.example.artwokmabel.homepage.postsfeed.FirestorePagingAdapterProfileImpl;
import com.example.artwokmabel.models.MainPost;
import com.example.artwokmabel.models.User;
import com.example.artwokmabel.profile.uploadpost.UploadPostActivity;
import com.example.artwokmabel.repos.FirestoreRepo;
import com.firebase.ui.firestore.SnapshotParser;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ProfilePostsFragment extends Fragment {
    private FragmentProfilePostsBinding binding;
    private ProfilePostsViewModel viewModel;
    private FirestorePagingAdapterProfileImpl adapter;
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

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_container);
        viewModel = ViewModelProviders.of(this).get(ProfilePostsViewModel.class);
        setUpPostsAdapter();

        return binding.getRoot();
    }

    public class OnUploadButtonClicked{
        public void onUploadButtonClicked(){
            navController.navigate(R.id.action_profile_graph_to_uploadPostFragment);
        }
    }

    private void setUpPostsAdapter(){
        viewModel.getUserOnce(userId).observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user != null){
                    Log.d("numtimes", "I've been loaded!");
                    PagedList.Config config = new PagedList.Config.Builder()
                            .setEnablePlaceholders(false)
                            .setInitialLoadSizeHint(4)
                            .setPrefetchDistance(2)
                            .setPageSize(3)
                            .build();

                    FirestorePagingOptions<MainPost> options = new FirestorePagingOptions.Builder<MainPost>()
                            .setQuery(viewModel.getSortedUserPostsQuery(userId), config, new SnapshotParser<MainPost>() {
                                @NonNull
                                @Override
                                public MainPost parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                                    return FirestoreRepo.getInstance().changeDocToMainPostModel(snapshot);
                                }
                            })
                            .build();

                    adapter = new FirestorePagingAdapterProfileImpl(options, user, getContext(), navController, binding.swipeRefreshLayout);
                    binding.recyclerview.setAdapter(adapter);
                    adapter.startListening();
                }
            }
        });
    }

}
