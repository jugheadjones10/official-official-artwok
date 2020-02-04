package com.example.artwokmabel.homepage.fragments.Indivlistings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.FragmentIndivListingFaqBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FaqFragment extends Fragment {
    private FragmentIndivListingFaqBinding binding;
    private HashMap<String, List<String>> expandableListDetail;
    private List<String> expandableListTitle;
    private ExpandableListAdapter expandableListAdapter;

    public FaqFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_indiv_listing_faq, container, false);

        expandableListDetail = FaqFragmentListDataPump.getData();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new FaqExpandableListAdapter(getActivity(), expandableListTitle, expandableListDetail);

        binding.sellerExpandableFaq.setAdapter(expandableListAdapter);
        binding.buyerExpandableFaq.setAdapter(expandableListAdapter);

        return binding.getRoot();
    }
}


