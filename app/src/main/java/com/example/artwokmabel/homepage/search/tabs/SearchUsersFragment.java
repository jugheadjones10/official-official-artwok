package com.example.artwokmabel.homepage.search.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.artwokmabel.HomePageActivity;
import com.example.artwokmabel.R;
import com.example.artwokmabel.Utils.TransactFragment;
import com.example.artwokmabel.databinding.FragmentSearchUsersBinding;
import com.example.artwokmabel.homepage.search.SearchActivity;
import com.example.artwokmabel.homepage.search.SearchFragment;
import com.example.artwokmabel.profile.people.FollowersAdapter;
import com.example.artwokmabel.profile.people.ViewOtherUserActivity;

public class SearchUsersFragment extends Fragment {

    private FragmentSearchUsersBinding binding;
    public FollowersAdapter adapter;
    private static SearchUsersFragment instance;
    private NavController navController;

    public static SearchUsersFragment getInstance(){
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_users, container, false);
        instance = this;

        binding.usersList.setHasFixedSize(true);
        binding.usersList.setLayoutManager(new LinearLayoutManager(getContext()));

        SearchFragment.getInstance().callSearch("", 1);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Below is a tricky line - take note of it. Why won't it work with the single argument version of findNavController?
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_container);
        adapter = new FollowersAdapter(getContext(), "searchpage", navController);
        binding.usersList.setAdapter(adapter);
    }
}
