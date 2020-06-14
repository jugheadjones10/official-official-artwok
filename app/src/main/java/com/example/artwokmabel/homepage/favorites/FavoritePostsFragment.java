package com.example.artwokmabel.homepage.favorites;

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
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwokmabel.HomePageActivity;
import com.example.artwokmabel.databinding.FragmentFavoritePostsBinding;
import com.example.artwokmabel.homepage.adapters.PostsAdapter;
import com.example.artwokmabel.homepage.postsfeed.HomeFeedViewModel;
import com.example.artwokmabel.models.MainPost;
import com.example.artwokmabel.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FavoritePostsFragment extends Fragment {
    private PostsAdapter postsAdapter;
    private FragmentFavoritePostsBinding binding;
    private FavoritePostsViewModel viewModel;
    private NavController navController;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite_posts, container, false);
        binding.catRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_container);
        postsAdapter = new PostsAdapter(getContext(), navController);
        binding.catRecyclerView.setAdapter(postsAdapter);

        viewModel = ViewModelProviders.of(this).get(FavoritePostsViewModel.class);
        viewModel.getUserFavPostsObservable().observe(getViewLifecycleOwner(), new Observer<List<MainPost>>() {
            @Override
            public void onChanged(List<MainPost> mainPosts) {
                if(mainPosts != null){
                    postsAdapter.setPostsList(mainPosts);
                }
            }
        });
    }
}
