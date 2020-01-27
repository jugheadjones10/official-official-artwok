package com.example.artwokmabel.Request;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwokmabel.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;

public class HomeRequestsFragment extends Fragment {

    ExtendedFloatingActionButton requestBtn;

    RecyclerView recyclerView;

    ArrayList<RequestsModel> requestsModels;
    RequestsAdapter requestsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_requests_fragment, container, false);

        recyclerView = view.findViewById(R.id.requests_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));

        requestBtn = view.findViewById(R.id.request_btn);
        requestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), RequestsActivity.class);
                startActivity(i);
            }
        });

        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), IndivRequestActivity.class);
                startActivity(i);
            }
        });

        // create Integer array
        Integer[] requestImage = {R.drawable.chocolate_cake, R.drawable.handmade_jewelry, R.drawable.tiger};

        // create String array
        String[] requestName = {"Chocolate Cake", "Handmade Jewelry", "Tiger"};
        String[] requestUsername = {"Jim Gordon", "Alexander King", "Young Jin Kim"};
        String[] requestPrice = {"$34.50", "$250", "$3.99"};

        // Initialize ArrayList
        requestsModels = new ArrayList<>();
        for(int i = 0; i < requestImage.length; i++) {
            RequestsModel model = new RequestsModel(requestImage[i], requestName[i], requestUsername[i], requestPrice[i]);
            requestsModels.add(model);
        }

        // Design Horizontal Layout
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                view.getContext(), LinearLayoutManager.VERTICAL, false
        );
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // Initialize RequestsAdapter
        requestsAdapter = new RequestsAdapter(view.getContext(), requestsModels);
        // Set RequestsAdapter to Recyclerview
        recyclerView.setAdapter(requestsAdapter);

        return view;
    }
}
