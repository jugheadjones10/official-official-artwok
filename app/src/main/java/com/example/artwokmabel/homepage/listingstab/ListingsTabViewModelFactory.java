package com.example.artwokmabel.homepage.listingstab;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.artwokmabel.repos.FirestoreRepo;
import com.example.artwokmabel.models.Listing;

import java.util.List;

class ListingsTabViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final String cat;
    public ListingsTabViewModelFactory(String cat) {
        this.cat = cat;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ListingsTabViewModel(cat);
    }
}
