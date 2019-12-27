package com.example.artwokmabel.chat.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.artwokmabel.chat.activities.ChatOrderActivity;
import com.example.artwokmabel.R;
import com.weiwangcn.betterspinner.library.BetterSpinner;

public class MessageOrdersFragment extends Fragment {

    ViewPager viewPager;
    View view;
    Button testButton;

    // Dropdown for categories selection
    String[] filter = {"All", "Listings - Buying", "Listings - Selling", "Requests - Buying", "Requests - Selling"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.message_orders_fragment, container, false);

        testButton = view.findViewById(R.id.testbtn);

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ChatOrderActivity.class);
                startActivity(i);
            }
        });

        // Dropdown for categories selection
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_dropdown_item_1line, filter);
        BetterSpinner betterSpinner = view.findViewById(R.id.message_orders_betterspinner);
        betterSpinner.setAdapter(arrayAdapter);

        return view;

    }

}
