package com.example.artwokmabel.notifs;

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

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.FragmentNotifsBinding;
import com.example.artwokmabel.models.Notification;

import java.util.List;

public class NotifsFragment extends Fragment {

    private FragmentNotifsBinding binding;
    private NotifsAdapter adapter;
    private NotifsViewModel viewModel;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notifs, container, false);

        viewModel = ViewModelProviders.of(this).get(NotifsViewModel.class);
        viewModel.getUserNotificationsObservable().observe(getViewLifecycleOwner(), new Observer<List<Notification>>() {
            @Override
            public void onChanged(@Nullable List<Notification> notifs) {
                if (notifs != null) {
                    adapter.setNotifsList(notifs);
                }
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_container);

        adapter = new NotifsAdapter(getActivity(), navController);
        binding.notifsRecyclerview.setAdapter(adapter);
    }
}
