package com.example.artwokmabel.homepage.request.upload;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.FragmentUploadRequestDetailsBinding;
import com.example.artwokmabel.models.Category;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UploadRequestDetailsFragment extends Fragment {

    private FragmentUploadRequestDetailsBinding binding;
    private static UploadRequestDetailsFragment instance;
    private UploadRequestDetailsViewModel viewModel;
    private String[] categories;
    private boolean[] checkedCategoriesArray;
    private int count = 0;
    private String finalizedCategory;

    public static UploadRequestDetailsFragment getInstance(){
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_upload_request_details, container, false);
        binding.setOncategoryclicked(new OnSelectCategory());

        instance = this;
        viewModel = ViewModelProviders.of(this).get(UploadRequestDetailsViewModel.class);
        observeViewModel(viewModel);

        return binding.getRoot();
    }

    private void observeViewModel(UploadRequestDetailsViewModel viewModel) {
        viewModel.getAllCategoriesObservable().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable List<Category> categories) {
                if (categories != null) {
                    ArrayList<String> allCategories = new ArrayList<>();
                    for(Category cat : categories){
                        allCategories.add(cat.getName());
                    }
                    UploadRequestDetailsFragment.this.categories = allCategories.toArray(new String[allCategories.size()]);

//                    if(UploadRequestDetailsFragment.this.checkedCategoriesArray.length == 0){
                        boolean[] checkedArray = new boolean[allCategories.size()];
                        Arrays.fill(checkedArray, false);
                        UploadRequestDetailsFragment.this.checkedCategoriesArray = checkedArray;
//                    }
                    //Todo: add another false to checkedCategoriesArray instead of remaking a whole new array with all false.
                    //need to maintain the trues
                }
            }
        });
    }

    public long getBudget(){
        return (long)Double.parseDouble(binding.budgetDecimal.getText().toString());
    }

    public String getCategory(){
        return finalizedCategory;
    }

    public class OnSelectCategory{
        public void onSelectCategory(){

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Select Categories");
            builder.setMultiChoiceItems(categories, checkedCategoriesArray, new DialogInterface.OnMultiChoiceClickListener(){
                public void onClick(DialogInterface dialogInterface, int which, boolean isChecked){

                    count += isChecked ? 1 : -1;
                    checkedCategoriesArray[which] = isChecked;

                    if (count > 1) {
                        Toast.makeText(getActivity(), "You can only select one category for requests", Toast.LENGTH_SHORT).show();
                        checkedCategoriesArray[which] = false;
                        count--;
                        ((AlertDialog) dialogInterface).getListView().setItemChecked(which, false);
                    }
                }
            });

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    for(int i=0; i < checkedCategoriesArray.length; i++){
                        if(checkedCategoriesArray[i]){
                            finalizedCategory = categories[i];
                        }
                    }
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}
