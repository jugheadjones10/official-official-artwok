package com.example.artwokmabel.homepage.fragments;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.artwokmabel.homepage.models.Category;
import com.example.artwokmabel.Repositories.FirestoreRepo;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class EditCategoriesViewModel extends AndroidViewModel {

    //Todo: change view models to the format in the Android docs
    private FirebaseAuth mAuth;
    private final LiveData<List<Category>> categoriesObservable;

    public EditCategoriesViewModel(Application application) {
        super(application);

        mAuth = FirebaseAuth.getInstance();
        categoriesObservable = FirestoreRepo.getInstance().getCategoriesList(mAuth.getCurrentUser().getUid());
    }

    public void setUserCategories(List<Category> categories){
        FirestoreRepo.getInstance().setUserTabsList(categories, mAuth.getCurrentUser().getUid());
    }

    public LiveData<List<Category>> getCategoriesObservable() {
        return categoriesObservable;
    }

}
