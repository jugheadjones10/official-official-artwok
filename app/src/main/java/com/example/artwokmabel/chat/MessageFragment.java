package com.example.artwokmabel.chat;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;

import com.example.artwokmabel.HomePageActivity;
import com.example.artwokmabel.R;
import com.example.artwokmabel.chat.tabs.MessageChatsAdapter;
import com.example.artwokmabel.chat.tabs.MessageChatsFragment;
import com.example.artwokmabel.chat.tabs.MessageFollowingAdapter;
import com.example.artwokmabel.chat.tabs.MessageOrdersAdapter;
import com.example.artwokmabel.chat.tabs.MessageOrdersFragment;
import com.example.artwokmabel.chat.tabs.MessageFollowingFragment;
import com.example.artwokmabel.databinding.MainMessageFragmentBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.oshi.libsearchtoolbar.SearchAnimationToolbar;

public class MessageFragment extends Fragment implements SearchAnimationToolbar.OnSearchQueryChangedListener{

    public MainMessageFragmentBinding binding;
    private MessageFragmentPagerAdapter adapter;
    private NavController navController;

    private static MessageFragment instance;

    public static MessageFragment getInstance(){
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        instance = this;
        binding = DataBindingUtil.inflate(inflater, R.layout.main_message_fragment, container, false);

        setHasOptionsMenu(true);

        adapter = new MessageFragmentPagerAdapter(this);

        // Adapter Setup
        binding.messageViewpager.setAdapter(adapter);

        //Setting default tab as the message tab
        binding.messageViewpager.setCurrentItem(1);

        new TabLayoutMediator(binding.messageTabs, binding.messageViewpager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        if(position == 0){
                            tab.setText("Following");
                        }else if(position == 1){
                            tab.setText("Chats");
                        }else{
                            tab.setText("Orders");
                        }
                    }
                }
        ).attach();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller,
                                             @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if(destination.getId() == R.id.chatFragment ||
                        destination.getId() == R.id.offerFragment ||
                        destination.getId() == R.id.reviewFragment ||
                        destination.getId() == R.id.listingFragment3||
                        destination.getId() == R.id.profileFragment3) {
                    HomePageActivity.Companion.getBottomNavBar().setVisibility(View.GONE);
                } else {
                    HomePageActivity.Companion.getBottomNavBar().setVisibility(View.VISIBLE);
                }
            }
        });

        binding.toolbar.setSupportActionBar((AppCompatActivity) getActivity());
        binding.toolbar.setTitle("");
        binding.toolbar.setOnSearchQueryChangedListener(MessageFragment.this);
        binding.toolbar.setSearchTextColor(Color.BLACK);

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if(!binding.toolbar.onBackPressed()){
                    HomePageActivity.Companion.getBottomNavBar().setSelectedItemId(R.id.home_graph);
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_search) {
            binding.toolbar.onSearchIconClick();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSearchCollapsed() {
        binding.artwokIcon.setVisibility(View.VISIBLE);
        binding.messageTabs.setVisibility(View.VISIBLE);
        binding.messageViewpager.setUserInputEnabled(true);

        int i = binding.messageViewpager.getCurrentItem();
        if(i == 0){
            MessageFollowingAdapter.getInstance().getFilter().filter("");
            MessageFollowingFragment.getInstance().binding.myProfile.setVisibility(View.VISIBLE);
        }else if (i == 1){
            MessageChatsAdapter.getInstance().getFilter().filter("");
        }else{
            MessageOrdersAdapter.getInstance().getFilter().filter("");
        }
    }

    @Override
    public void onSearchQueryChanged(String query) {
        int i = binding.messageViewpager.getCurrentItem();
        if(i == 0){
            MessageFollowingAdapter.getInstance().getFilter().filter(query);
        }else if(i == 1){
            MessageChatsAdapter.getInstance().getFilter().filter(query);
        }else{
            MessageOrdersAdapter.getInstance().getFilter().filter(query);
        }
    }

    @Override
    public void onSearchExpanded() {
        binding.artwokIcon.setVisibility(View.GONE);
        binding.messageTabs.setVisibility(View.GONE);
        binding.messageViewpager.setUserInputEnabled(false);

        int i = binding.messageViewpager.getCurrentItem();
        if(i == 0){
            MessageFollowingFragment.getInstance().binding.myProfile.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSearchSubmitted(String query) {
        //Make keyboard go down
    }


}