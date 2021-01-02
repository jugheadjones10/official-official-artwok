package com.example.artwokmabel.homepage.listing;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.artwokmabel.R;

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
        desc.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                final Uri uri = request.getUrl();
                Log.d("uri", uri.toString());

                view.getContext().startActivity(
                        new Intent(Intent.ACTION_VIEW, uri));
                return true;

            }
        });
        desc.loadData(encoded, "text/html", "base64");

        return view;
    }
}
