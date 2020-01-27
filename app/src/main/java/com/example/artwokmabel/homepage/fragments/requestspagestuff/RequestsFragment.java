package com.example.artwokmabel.homepage.fragments.requestspagestuff;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.FragmentRequestsBinding;

import java.util.List;

public class RequestsFragment extends Fragment {

    private RequestsViewModel viewModel;
    private RequestsAdapter requestsAdapter;
    private FragmentRequestsBinding binding;
    private String cat;
    //Todo: add horizontal scrollable listings

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_requests, container, false);
        binding.addRequestButton.bringToFront();
        binding.setCallback(new OnAddRequestClick());

        binding.requestRecyclerview.setHasFixedSize(true);
        binding.requestRecyclerview.setLayoutManager(new GridLayoutManager(getContext(), 2));

        requestsAdapter = new RequestsAdapter(getContext());
        binding.requestRecyclerview.setAdapter(requestsAdapter);

        return binding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(RequestsViewModel.class);

        observeViewModel(viewModel);
    }

    private void observeViewModel(RequestsViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getRequestsObservable().observe(this, new Observer<List<Request>>() {
            @Override
            public void onChanged(@Nullable List<Request> requests) {
                if (requests != null) {
                    requestsAdapter.setRequestsList(requests);
                }
            }
        });
    }

    public class OnAddRequestClick{
        public void onAddRequestClick(){
            Intent intent = new Intent(getContext(), UploadRequestActivity.class);
            startActivity(intent);
        }
    }
}
