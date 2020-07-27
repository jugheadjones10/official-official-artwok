package com.example.artwokmabel.homepage.postsfeed;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.artwokmabel.HomeGraphDirections;
import com.example.artwokmabel.HomePageActivity;
import com.example.artwokmabel.ProfileGraphDirections;
import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.FragmentHomeFeedBinding;
import com.example.artwokmabel.databinding.ItemPostBinding;
import com.example.artwokmabel.homepage.adapters.ListingsAdapter;
import com.example.artwokmabel.homepage.adapters.PostsAdapter;
import com.example.artwokmabel.homepage.callbacks.MainPostClickCallback;
import com.example.artwokmabel.homepage.homepagewrapper.HomeTabsFragment;
import com.example.artwokmabel.models.Listing;
import com.example.artwokmabel.models.MainPost;
import com.example.artwokmabel.models.User;
import com.example.artwokmabel.repos.FirestoreRepo;
import com.firebase.ui.firestore.SnapshotParser;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.ImageListener;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter;
import com.yarolegovich.discretescrollview.transform.Pivot;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HomeFeedFragment extends Fragment {

    private FragmentHomeFeedBinding binding;
    private HomeFeedViewModel viewModel;
    private PostsAdapter postsAdapter;
    private ListingsHomeAdapter listingsAdapter;
    private NavController navController;
    private FirestorePagingAdapter<MainPost, PostViewHolder> adapter;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

//    private List<MainPost> feedPosts;
    //Todo: add horizontal scrollable listings

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_feed, container, false);
//        binding.recyclerview.setHasFixedSize(false);
//        binding.recyclerview.setNestedScrollingEnabled(true);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Below is a tricky line - take note of it. Why won't it work with the single argument version of findNavController?
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_container);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller,
                                             @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if(destination.getId() == R.id.postFragment || destination.getId() == R.id.listingFragment) {
                    HomePageActivity.Companion.getBottomNavBar().setVisibility(View.GONE);
                } else {
                    HomePageActivity.Companion.getBottomNavBar().setVisibility(View.VISIBLE);
                }
            }
        });

        viewModel =  new ViewModelProvider(requireActivity()).get(HomeFeedViewModel.class);
        observeViewModel(viewModel);

        setUpPostsAdapter();

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.refresh();
            }
        });

        //Todo: bring back listings recycler view
        listingsAdapter = new ListingsHomeAdapter(getContext(), navController);

        DiscreteScrollView horizontalRecyclerViewListings = HomeTabsFragment.getInstance().binding.horizontalRecyclerViewListings;
        horizontalRecyclerViewListings.setAdapter(listingsAdapter);
        horizontalRecyclerViewListings.setBackgroundColor(getResources().getColor(R.color.white));

        horizontalRecyclerViewListings.setItemTransformer(new ScaleTransformer.Builder()
                .setPivotX(Pivot.X.CENTER) // CENTER is a default one
                .setPivotY(Pivot.Y.CENTER) // CENTER is a default one
                .build());
        horizontalRecyclerViewListings.setSlideOnFling(true);
        horizontalRecyclerViewListings.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                int action = e.getAction();
                switch (action) {
                    case MotionEvent.ACTION_MOVE:
                        rv.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void observeViewModel(HomeFeedViewModel viewModel) {
        // Update the list when the data changes
//        viewModel.getFeedPostsObeservable().observe(getViewLifecycleOwner(), new Observer<List<MainPost>>() {
//            @Override
//            public void onChanged(@Nullable List<MainPost> posts) {
//                if(posts != null){
//                    Log.d("ADDDD", posts.toString());
//                    postsAdapter.setPostsList(posts);
//                }
//            }
//        });

        viewModel.getFeedListingObservable().observe(getViewLifecycleOwner(), new Observer<List<Listing>>() {
            @Override
            public void onChanged(@Nullable List<Listing> listings) {
                if (listings != null) {
                    listingsAdapter.setListingsList(listings);
                }
            }
        });
    }

    private void setUpPostsAdapter(){
        viewModel.getUserOnce(FirebaseAuth.getInstance().getCurrentUser().getUid()).observe(getViewLifecycleOwner(), new Observer<User>() {
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
                            .setQuery(viewModel.getFeedPostsQuery(user.getFollowing()), config, new SnapshotParser<MainPost>() {
                                @NonNull
                                @Override
                                public MainPost parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                                    return FirestoreRepo.getInstance().changeDocToMainPostModel(snapshot);
                                }
                            })
                            .build();

                    adapter = new FirestorePagingAdapterImpl(options, user, getContext(), navController, binding.swipeRefreshLayout);
                    binding.recyclerview.setAdapter(adapter);
                    adapter.startListening();
                }
            }
        });
    }
}
