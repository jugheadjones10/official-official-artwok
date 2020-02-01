package com.example.artwokmabel.profile.followingfollowers;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.ActivityFollowingFollowersBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class FollowingFollowersActivity extends AppCompatActivity {

    private FollowingFollowersTabsPagerAdapter adapter;
    private ActivityFollowingFollowersBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_following_followers);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new FollowingFollowersTabsPagerAdapter(this);
        binding.viewPager.setAdapter(adapter);

        new TabLayoutMediator(binding.tabs, binding.viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(TabLayout.Tab tab, int position) {
                        if(position == 0){
                            tab.setText("Following");
                        }else{
                            tab.setText("Followers");
                        }
                    }
                }
        ).attach();
        binding.tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

//    @Override
//    public void onResume(){
//        super.onResume();
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        final HomeTabsViewModel viewModel =
//                ViewModelProviders.of(this).get(HomeTabsViewModel.class);
//
//        observeViewModel(viewModel);
//        //binding.tabs.setupWithViewPager(binding.viewPager);
//        //        //binding.tabs.getTabAt(mSectionsPagerAdapter.getCount() - 1).setIcon(R.drawable.add);
//    }
//
//    private void observeViewModel(HomeTabsViewModel viewModel) {
//        // Update the list when the data changes
//        viewModel.getCategoryListObservable().observe(this, new Observer<ArrayList<String>>() {
//            @Override
//            public void onChanged(@Nullable ArrayList<String> categories) {
//                if (categories != null) {
//
//                    categories.add(0, "feed");
//                    categories.add("edit");
//
//                    tabCategories = categories;
//                    adapter.setCategoriesList(categories);
//
//                    binding.viewPager.setCurrentItem(0);
//                }
//            }
//        });
//    }

//    public void loadFragment(Fragment fragment) {
//        // load fragment
//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.replace(R.id.container, fragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }
//
//    private void setUpClickListeners(){
//        binding.favBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getActivity(), FavouritesActivity.class);
//                startActivity(i);
//            }
//        });
//
//        binding.mainsearchbar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loadFragment(new TemporarySearchFragment());
//            }
//        });
//    }
}
