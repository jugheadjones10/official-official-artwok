package com.example.artwokmabel.rubbish;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.artwokmabel.R;

public class HomeSearchFragment extends Fragment {

    private EditText searchBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.z_main_search_fragment, container, false);

        searchBar = view.findViewById(R.id.et_mainsearchbar);
        searchBar.requestFocus();




        return view;
    }
}
