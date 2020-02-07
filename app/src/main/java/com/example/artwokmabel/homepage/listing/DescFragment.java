package com.example.artwokmabel.homepage.listing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.artwokmabel.R;

public class DescFragment extends Fragment {
    View view;
    TextView desc;
    public DescFragment () {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_listing_desc, container, false);

        desc = view.findViewById(R.id.desc_text);

        String description = getArguments().getString("description");
        desc.setText(description);

        return view;
    }
}
