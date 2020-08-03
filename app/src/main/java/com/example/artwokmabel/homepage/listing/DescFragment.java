package com.example.artwokmabel.homepage.listing;

import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.artwokmabel.R;
import com.example.artwokmabel.homepage.post.PostFragmentArgs;

public class DescFragment extends Fragment {
    private View view;
    public WebView desc;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_listing_desc, container, false);

        desc = view.findViewById(R.id.listing_desc_web);

        String description = getArguments().getString("description");

        String encoded = Base64.encodeToString(description.getBytes(), Base64.DEFAULT);
        desc.loadData(encoded, "text/html", "base64");

        return view;
    }
}
