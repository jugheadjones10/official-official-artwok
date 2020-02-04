package com.example.artwokmabel.homepage.request;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.FragmentAddRequestDescBinding;

public class AddRequestDescFragment extends Fragment {

    private FragmentAddRequestDescBinding binding;
    private static AddRequestDescFragment instance;

    public static AddRequestDescFragment getInstance(){
        return instance;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_request_desc, container, false);
        binding.newPostDescL.bringToFront();

        instance = this;

        return binding.getRoot();
    }

    public String getTitle(){
        return binding.newPostTitleL.getText().toString();
    }

    public String getDesc(){
        return binding.newPostDescL.getText().toString();
    }
}
