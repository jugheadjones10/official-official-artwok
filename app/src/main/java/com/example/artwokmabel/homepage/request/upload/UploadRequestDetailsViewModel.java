package com.example.artwokmabel.homepage.request.upload;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.repos.FirestoreRepo;
import com.example.artwokmabel.models.Category;

import java.util.List;

public class UploadRequestDetailsViewModel extends ViewModel {

    private final LiveData<List<Category>> allCategoriesObservable;

    public UploadRequestDetailsViewModel() {
        allCategoriesObservable = FirestoreRepo.getInstance().getAllCategories();
    }

    public LiveData<List<Category>> getAllCategoriesObservable() {
        return allCategoriesObservable;
    }
}
