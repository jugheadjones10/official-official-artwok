package com.example.artwokmabel.notifs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwokmabel.InternalStorage;
import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.FragmentNotifsBinding;
import com.example.artwokmabel.login.LoginOptionsActivity;
import com.example.artwokmabel.login.SplashActivity;
import com.example.artwokmabel.models.Notification;
import com.example.artwokmabel.models.User;
import com.example.artwokmabel.profile.people.PeopleViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NotifsFragment extends Fragment {

    private FragmentNotifsBinding binding;
    private NotifsAdapter adapter;
    private NotifsViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notifs, container, false);

        adapter = new NotifsAdapter(getActivity());
        binding.notifsRecyclerview.setAdapter(adapter);

        viewModel = ViewModelProviders.of(this).get(NotifsViewModel.class);
        viewModel.getUserNotificationsObservable().observe(this, new Observer<List<Notification>>() {
            @Override
            public void onChanged(@Nullable List<Notification> notifs) {
                if (notifs != null) {
                    adapter.setNotifsList(notifs);
                }
            }
        });

        return binding.getRoot();
    }
}
