package com.example.artwokmabel.homepage.postsfeed;

import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
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
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.artwokmabel.HomePageActivity;
import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.FragmentHomeFeedBinding;
import com.example.artwokmabel.homepage.adapters.PostsAdapter;
import com.example.artwokmabel.models.Listing;
import com.example.artwokmabel.models.ListingPost;
import com.example.artwokmabel.models.User;
import com.example.artwokmabel.repos.FirestoreRepo;
import com.firebase.ui.firestore.SnapshotParser;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.List;

public class HomeFeedFragment extends Fragment {

    private static final String KEY_RECYCLER_STATE = "123";
    private Bundle recyclerViewState;
    private Parcelable mListState;

    private Query query;
    private User user;
    FirestorePagingOptions<ListingPost> options;

    private FragmentHomeFeedBinding binding;
    private HomeFeedViewModel viewModel;
    private PostsAdapter postsAdapter;
    private ListingsHomeAdapter listingsAdapter;
    private NavController navController;
    private FirestorePagingAdapter<ListingPost, RecyclerView.ViewHolder> adapter;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private int pos;

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
                if(destination.getId() == R.id.postFragment || destination.getId() == R.id.listingFragment || destination.getId() == R.id.offerFragment) {
                    HomePageActivity.Companion.getBottomNavBar().setVisibility(View.GONE);
                } else {
                    HomePageActivity.Companion.getBottomNavBar().setVisibility(View.VISIBLE);
                }
            }
        });

        viewModel =  new ViewModelProvider(requireActivity()).get(HomeFeedViewModel.class);
        observeViewModel(viewModel);

        Log.d("tracking", "in on view created");
        //if(query == null || user == null){
            setUpPostsAdapter();
//        }else{
//            setUpNormal();
//        }

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //adapter.refresh();
                setUpPostsAdapter();
            }
        });

        //Todo: bring back listings recycler view
//        listingsAdapter = new ListingsHomeAdapter(getContext(), navController);

//        DiscreteScrollView horizontalRecyclerViewListings = HomeTabsFragment.getInstance().binding.horizontalRecyclerViewListings;
//        horizontalRecyclerViewListings.setAdapter(listingsAdapter);
//        horizontalRecyclerViewListings.setBackgroundColor(getResources().getColor(R.color.white));
//
//        horizontalRecyclerViewListings.setItemTransformer(new ScaleTransformer.Builder()
//                .setPivotX(Pivot.X.CENTER) // CENTER is a default one
//                .setPivotY(Pivot.Y.CENTER) // CENTER is a default one
//                .build());
//        horizontalRecyclerViewListings.setSlideOnFling(true);
//        horizontalRecyclerViewListings.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
//            @Override
//            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
//                int action = e.getAction();
//                switch (action) {
//                    case MotionEvent.ACTION_MOVE:
//                        rv.getParent().requestDisallowInterceptTouchEvent(true);
//                        break;
//                }
//                return false;
//            }
//
//            @Override
//            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
//
//            }
//
//            @Override
//            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
//
//            }
//        });
    }

    //Below is an attempt to save scroll state
    @Override
    public void onPause() {
        super.onPause();
        Log.d("tracking", "on Pause");
        recyclerViewState = new Bundle();
        mListState = binding.recyclerview.getLayoutManager().onSaveInstanceState();
        recyclerViewState.putParcelable(KEY_RECYCLER_STATE, mListState);
        //pos = binding.recyclerview.getScrollY();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("tracking", "on Resume");
//        if (recyclerViewState != null) {
//            new Handler().postDelayed(new Runnable() {
//
//                @Override
//                public void run() {
//                    mListState = recyclerViewState.getParcelable(KEY_RECYCLER_STATE);
//                    binding.recyclerview.getLayoutManager().onRestoreInstanceState(mListState);
//
//                }
//            }, 50);
//        }
//
//        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("tracking", "on Start");
        if(adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("tracking", "on Stop");

        if(adapter != null) {
            adapter.stopListening();
        }
    }

    private void observeViewModel(HomeFeedViewModel viewModel) {
//        viewModel.getFeedListingObservable().observe(getViewLifecycleOwner(), new Observer<List<Listing>>() {
//            @Override
//            public void onChanged(@Nullable List<Listing> listings) {
//                if (listings != null) {
//                    listingsAdapter.setListingsList(listings);
//                }
//            }
//        });
    }


    private void setUpNormal() {
        if (recyclerViewState != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mListState = recyclerViewState.getParcelable(KEY_RECYCLER_STATE);
                    binding.recyclerview.getLayoutManager().onRestoreInstanceState(mListState);
                }
            }, 50);
        }
       binding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));

        //binding.recyclerview.setScrollY(pos);
        binding.recyclerview.setAdapter(adapter);
    }

    private void setUpPostsAdapter(){

//        viewModel.getFeedPostsQuery().observe(getViewLifecycleOwner(), new Observer<Query>() {
//            @Override
//            public void onChanged(Query query) {
//                if(query != null){
//
//                    HomeFeedFragment.this.query = query;
//
//                    PagedList.Config config = new PagedList.Config.Builder()
//                            .setEnablePlaceholders(false)
//                            .setInitialLoadSizeHint(6)
//                            .setPrefetchDistance(10)
//                            .setPageSize(10)
//                            .build();
//
//                    options = new FirestorePagingOptions.Builder<ListingPost>()
//                            .setQuery(query, config, new SnapshotParser<ListingPost>() {
//                                @NonNull
//                                @Override
//                                public ListingPost parseSnapshot(@NonNull DocumentSnapshot snapshot) {
//                                    Log.d("checkpost", "Within parse snapshot" + snapshot.getString("username"));
//                                    return FirestoreRepo.getInstance().changeDocToListingPostModel(snapshot);
//                                }
//                            })
//                            .build();
//
//                    viewModel.getUser(FirebaseAuth.getInstance().getCurrentUser().getUid()).observe(getViewLifecycleOwner(), new Observer<User>() {
//                        @Override
//                        public void onChanged(User user) {
//                            if(user != null){
//                                HomeFeedFragment.this.user = user;
//                                adapter = new FirestorePagingAdapterImpl(options, user, getContext(), navController, binding.swipeRefreshLayout);
//
//                                binding.recyclerview.setAdapter(adapter);
//                                adapter.startListening();
//                            }
//                        }
//                    });
//                }
//            }
//        });

        viewModel.getUserOnce(FirebaseAuth.getInstance().getCurrentUser().getUid()).observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user != null){

                    Log.d("tracking", "got user once");
                    Log.d("numtimes", "I've been loaded!");

                    PagedList.Config config = new PagedList.Config.Builder()
                            .setEnablePlaceholders(false)
                            .setInitialLoadSizeHint(4)
                            .setPrefetchDistance(2)
                            .setPageSize(3)
                            .build();

                    FirestorePagingOptions<ListingPost> options = new FirestorePagingOptions.Builder<ListingPost>()
                            .setQuery(viewModel.getFeedPostsQuery(user.getFollowing()), config, new SnapshotParser<ListingPost>() {
                                @NonNull
                                @Override
                                public ListingPost parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                                    Log.d("checkpost", "Within parse snapshot" + snapshot.getString("username"));
                                    return FirestoreRepo.getInstance().changeDocToListingPostModel(snapshot);
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
