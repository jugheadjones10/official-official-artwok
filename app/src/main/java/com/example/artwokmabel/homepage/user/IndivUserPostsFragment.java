package com.example.artwokmabel.homepage.user;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.FragmentIndivUserPostsBinding;
import com.example.artwokmabel.homepage.adapters.PostsAdapter;
import com.example.artwokmabel.models.MainPost;

import java.util.List;

public class IndivUserPostsFragment extends Fragment {

    public String indivUserId;
    private String indivUsername;

    private FragmentIndivUserPostsBinding binding;
    private IndivUserPostsViewModel viewModel;
    private PostsAdapter adapter;
    private static IndivUserPostsFragment instance;

    public static IndivUserPostsFragment getInstance(){
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        instance = this;
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_indiv_user_posts, container, false);

        binding.recyclerview.setHasFixedSize(true);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            indivUserId = bundle.getString("indiv_user_id");
            indivUsername = bundle.getString("indiv_username");
            Log.d("crapadoodle", indivUserId);
        }

        //TODO edit the destinationId argument below
        adapter = new PostsAdapter(getContext(), Navigation.findNavController(requireActivity(), R.id.nav_host_container));
        binding.recyclerview.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(IndivUserPostsViewModel.class);

        observeViewModel(viewModel);
    }

    private void observeViewModel(IndivUserPostsViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getUserPostsObeservable().observe(getViewLifecycleOwner(), new Observer<List<MainPost>>() {
            @Override
            public void onChanged(@Nullable List<MainPost> categories) {
                if (categories != null) {
                    adapter.setPostsList(categories);
                }
            }
        });
    }
}
