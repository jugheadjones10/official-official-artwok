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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.paging.PagedList;

import com.example.artwokmabel.databinding.FragmentFavoritePostsBinding;
import com.example.artwokmabel.homepage.postsfeed.FirestorePagingAdapterProfileImpl;
import com.example.artwokmabel.models.MainPost;
import com.example.artwokmabel.R;
import com.example.artwokmabel.models.User;
import com.example.artwokmabel.repos.FirestoreRepo;
import com.firebase.ui.firestore.SnapshotParser;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

public class FavoritePostsFragment extends Fragment {
    private FirestorePagingAdapterProfileImpl postsAdapter;
    private FragmentFavoritePostsBinding binding;
    private FavoritePostsViewModel viewModel;
    private NavController navController;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite_posts, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_container);
        viewModel = new ViewModelProvider(this).get(FavoritePostsViewModel.class);


        //Just removed swipe refresh since I don't know how to update the adapter query at the moment.
        //Maybe next time
//        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {

                //This is so that a new list of user fav post Ids will be passed to the query and a new query used for the adapter
//                viewModel.getUserOnce(mAuth.getCurrentUser().getUid()).removeObservers(getViewLifecycleOwner());
//                setUpPostsAdapter();

                //postsAdapter.refresh();
//            }
//        });
        binding.swipeRefreshLayout.setVisibility(View.GONE);

        setUpPostsAdapter();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(postsAdapter != null) {
            postsAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(postsAdapter != null) {
            postsAdapter.stopListening();
        }
    }

    private void setUpPostsAdapter(){
        viewModel.getUserOnce(mAuth.getCurrentUser().getUid()).observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user != null && user.getFav_posts().size() != 0){
                    Log.d("numtimes", "I've been loaded!");
                    PagedList.Config config = new PagedList.Config.Builder()
                            .setEnablePlaceholders(false)
                            .setInitialLoadSizeHint(4)
                            .setPrefetchDistance(2)
                            .setPageSize(3)
                            .build();

                    FirestorePagingOptions<MainPost> options = new FirestorePagingOptions.Builder<MainPost>()
                            .setQuery(viewModel.getSortedUserFavPostsQuery(user.getFav_posts()), config, new SnapshotParser<MainPost>() {
                                @NonNull
                                @Override
                                public MainPost parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                                    return FirestoreRepo.getInstance().changeDocToMainPostModel(snapshot);
                                }
                            })
                            .build();

                    postsAdapter = new FirestorePagingAdapterProfileImpl(options, user, getContext(), navController, binding.swipeRefreshLayout);
                    binding.catRecyclerView.setAdapter(postsAdapter);
                    postsAdapter.startListening();
                }
            }
        });
    }

}
