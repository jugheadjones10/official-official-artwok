package com.example.artwokmabel.homepage.fragments;

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
import com.example.artwokmabel.databinding.FragmentEditCategoriesBinding;
import com.example.artwokmabel.homepage.adapters.CatCardsAdapter;
import com.example.artwokmabel.homepage.callbacks.EditCategorySaveCallback;
import com.example.artwokmabel.homepage.models.Category;

import java.util.ArrayList;
import java.util.List;

public class EditCategoriesFragment extends Fragment {

    private FragmentEditCategoriesBinding binding;
    private EditCategoriesViewModel viewModel;
    public CatCardsAdapter adapter;
    private static EditCategoriesFragment instance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_categories, container, false);
        instance = this;

        //Todo: find trigger and solution to weird "edit" turns into other tab bug
        binding.catRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        adapter = new CatCardsAdapter();
        binding.catRecyclerView.setAdapter(adapter);

        return binding.getRoot();
    }

    public static EditCategoriesFragment getInstance(){
        return instance;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(EditCategoriesViewModel.class);

        binding.setCallback(editCategorySaveCallback);
        observeViewModel(viewModel);
    }

    private void observeViewModel(EditCategoriesViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getCategoriesObservable().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable List<Category> categories) {
                if (categories != null) {
                    adapter.setCategoriesList(categories);
                }
            }
        });
    }

    private final EditCategorySaveCallback editCategorySaveCallback = new EditCategorySaveCallback() {
        @Override
        public void onClick() {
            List<Category> categories = adapter.getCategoriesList();

            List<Category> filtered = new ArrayList<Category>();
            for (Category category: categories) {
                if (category.isChecked()) {
                    filtered.add(category);
                }
            }
            viewModel.setUserCategories(filtered);
        }
    };

}
