package com.example.artwokmabel.homepage.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.algolia.instantsearch.guides.gettingstarted.ProductFragment;
import com.example.artwokmabel.homepage.homepagestuff.HomeTabsManagerFragment;
import com.example.artwokmabel.R;

public class TemporarySearchFragment extends Fragment {

    private EditText searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_temp_search, container, false);

        Toolbar toolbar = view.findViewById(R.id.temp_search_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        searchView = view.findViewById(R.id.temp_search_edit);

//        searchView.setInputType(InputType.TYPE_NULL);
//        searchView.onActionViewExpanded();
        searchView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                HomeTabsManagerFragment.getInstance().loadFragment(new ProductFragment());
            }
        });

        return view;
    }
}
