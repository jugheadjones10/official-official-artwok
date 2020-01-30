package com.example.artwokmabel.profile.uploadlisting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.FragmentAddListingDetailsBinding;
import com.example.artwokmabel.homepage.fragments.requestspagestuff.RequestDetailsFragmentViewModel;
import com.example.artwokmabel.homepage.models.Category;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddListingDetailsFragment extends Fragment {

    private FragmentAddListingDetailsBinding binding;
    private static AddListingDetailsFragment instance;
    private RequestDetailsFragmentViewModel viewModel;
    private String[] categories;
    private boolean[] checkedCategoriesArray;
    private ArrayList<String> finalizedCategories = new ArrayList<>();

    public static AddListingDetailsFragment getInstance(){
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_listing_details, container, false);
        binding.setOncategoryclicked(new AddListingDetailsFragment.OnSelectCateogry());

        instance = this;
        viewModel = ViewModelProviders.of(this).get(RequestDetailsFragmentViewModel.class);
        observeViewModel(viewModel);

        return binding.getRoot();
    }

    private void observeViewModel(RequestDetailsFragmentViewModel viewModel) {
        viewModel.getAllCategoriesObservable().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable List<Category> categories) {
                if (categories != null) {
                    ArrayList<String> allCategories = new ArrayList<>();
                    for(Category cat : categories){
                        allCategories.add(cat.getName());
                    }
                    AddListingDetailsFragment.this.categories = allCategories.toArray(new String[allCategories.size()]);

                    boolean checkedArray[] = new boolean[allCategories.size()];
                    Arrays.fill(checkedArray, false);
                    AddListingDetailsFragment.this.checkedCategoriesArray = checkedArray;

                    //Todo: add another false to checkedCategoriesArray instead of remaking a whole new array with all false.
                    //need to maintain the trues
                }
            }
        });
    }

    public long getBudget(){
        return 1;
//                (long)Double.parseDouble(binding.priceEditText.getText().toString());
    }

    public ArrayList<String> getCategories(){
        return finalizedCategories;
    }

    public class OnSelectCateogry{
        public void onSelectCategory(){

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Select Categories");
            builder.setMultiChoiceItems(categories, checkedCategoriesArray, new DialogInterface.OnMultiChoiceClickListener(){
                public void onClick(DialogInterface dialogInterface, int which, boolean isChecked){

                    checkedCategoriesArray[which] = isChecked;
                }
            });

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    for(int i=0; i < checkedCategoriesArray.length; i++){
                        if(checkedCategoriesArray[i]){
                            finalizedCategories.add(categories[i]);
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
