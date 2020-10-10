package com.example.artwokmabel.homepage.listingstab;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

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
