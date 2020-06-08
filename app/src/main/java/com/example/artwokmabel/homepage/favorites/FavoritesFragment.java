package com.example.artwokmabel.homepage.favorites;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.FragmentFavoritesBinding;

public class FavoritesFragment extends Fragment {

    private FragmentFavoritesBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorites, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((AppCompatActivity)getActivity()).setSupportActionBar(binding.favToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FavoritesPagerAdapter adapter = new FavoritesPagerAdapter(requireActivity().getSupportFragmentManager());

        // Adding Fragments
        adapter.addFragment(new FavoriteListingsFragment(),"Listings");
        adapter.addFragment(new FavoritePostsFragment(),"Posts");
        adapter.addFragment(new FavoriteRequestsFragment(),"Requests");

        // Adapter Setup
        binding.favViewpager.setAdapter(adapter);
        binding.favTabs.setupWithViewPager(binding.favViewpager);
    }
}