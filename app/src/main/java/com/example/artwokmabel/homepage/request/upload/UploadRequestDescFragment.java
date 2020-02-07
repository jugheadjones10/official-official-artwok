package com.example.artwokmabel.homepage.request.upload;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.FragmentUploadRequestDescBinding;

public class UploadRequestDescFragment extends Fragment {

    private FragmentUploadRequestDescBinding binding;
    private static UploadRequestDescFragment instance;

    public static UploadRequestDescFragment getInstance(){
        return instance;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_upload_request_desc, container, false);
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
