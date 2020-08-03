package com.example.artwokmabel.profile.uploadlisting;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;

import com.example.artwokmabel.HomePageActivity;
import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.FragmentUploadListingDeliveryBinding;
import com.example.artwokmabel.databinding.FragmentUploadRequestDescBinding;
import com.example.artwokmabel.profile.uploadpost.UploadPostViewModel;

public class UploadListingDescFragment extends Fragment {

    private FragmentUploadRequestDescBinding binding;
    private static UploadListingDescFragment instance;
    private UploadListingDescViewModel viewModel;
    private NavController navController;
    private String htmlContent;

    public static UploadListingDescFragment getInstance(){
        return instance;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_upload_request_desc, container, false);
        binding.setUploadListingDescFragment(this);
        viewModel = new ViewModelProvider(requireActivity()).get(UploadListingDescViewModel.class);

        instance = this;

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_container);

        viewModel.getHtmlContent().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.d("ithaschanged", "It has been changed");
                if(s != null){
                    htmlContent = s;
                    Log.d("framenthtml", s);
                    String encoded = Base64.encodeToString(htmlContent.getBytes(), Base64.DEFAULT);
                    binding.listingWebView.loadData(encoded, "text/html", "base64");
                }
            }
        });
    }

    public String getTitle(){
        return binding.newPostTitleL.getText().toString();
    }

    public String getDesc(){
        return htmlContent;
    }

    public void onGoToRichTextEditor(){
        navController.navigate(R.id.action_uploadListingFragment_to_uploadListingRichTextEditor);
    }

}