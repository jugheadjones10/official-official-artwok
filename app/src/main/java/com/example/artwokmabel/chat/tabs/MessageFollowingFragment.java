package com.example.artwokmabel.chat.tabs;

import android.os.Bundle;
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

import com.example.artwokmabel.ChatGraphDirections;
import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.MessageFollowingFragmentBinding;
import com.example.artwokmabel.models.User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageFollowingFragment extends Fragment {

    private MessageFollowingAdapter adapter;

    public MessageFollowingFragmentBinding binding;
    private MessageFollowingViewModel viewModel;
    private NavController navController;

    private static MessageFollowingFragment instance;

    public static MessageFollowingFragment getInstance(){
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.message_following_fragment, container, false);

        instance = this;
        binding.setOnprofileclicked(new OnProfileClicked());

        viewModel = ViewModelProviders.of(this).get(MessageFollowingViewModel.class);
        observeViewModel(viewModel);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_container);

        adapter = new MessageFollowingAdapter(getActivity(), navController);
        binding.friendsFragmentRecyclerview.setAdapter(adapter);
    }

    private void observeViewModel(MessageFollowingViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getFollowingUsersObservable().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> users) {
                if (users != null) {
                    adapter.setUsersList(users);
                }
            }
        });

        viewModel.getUserObservable().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                binding.setUser(user);
                Picasso.get().load(user.getProfile_url()).into(binding.contactPicture);
            }
        });
    }

    public class OnProfileClicked{
        public void onProfileClicked(User user){
            ChatGraphDirections.ActionGlobalProfileFragment3 action =
                    ChatGraphDirections.actionGlobalProfileFragment3(user.getUid());
            navController.navigate(action);
        }
    }
}
