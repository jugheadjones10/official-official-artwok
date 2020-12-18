package com.example.artwokmabel.profile.uploadlisting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
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
import com.example.artwokmabel.databinding.FragmentUploadListingDetailsBinding;
import com.example.artwokmabel.models.Category;
import com.example.artwokmabel.homepage.request.upload.UploadRequestDetailsViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UploadListingDetailsFragment extends Fragment {

    private FragmentUploadListingDetailsBinding binding;
    private static UploadListingDetailsFragment instance;
    private UploadRequestDetailsViewModel viewModel;
    private String[] categories;
    private boolean[] checkedCategoriesArray;
    private ArrayList<String> finalizedCategories;

    public static UploadListingDetailsFragment getInstance(){
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_upload_listing_details, container, false);
        binding.setOncategoryclicked(new UploadListingDetailsFragment.OnSelectCateogry());
        binding.priceEditText.setFilters(new InputFilter[]{
                new DecimalDigitsInputFilter(2, 2)
        });

        finalizedCategories = new ArrayList<>();
        instance = this;
        viewModel = ViewModelProviders.of(this).get(UploadRequestDetailsViewModel.class);
        observeViewModel(viewModel);

        return binding.getRoot();
    }

    private void observeViewModel(UploadRequestDetailsViewModel viewModel) {
        viewModel.getAllCategoriesObservable().observe(getViewLifecycleOwner(), new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable List<Category> categories) {
                if (categories != null) {
                    ArrayList<String> allCategories = new ArrayList<>();
                    for(Category cat : categories){
                        allCategories.add(cat.getName());
                    }
                    UploadListingDetailsFragment.this.categories = allCategories.toArray(new String[allCategories.size()]);

                    boolean[] checkedArray = new boolean[allCategories.size()];
                    Arrays.fill(checkedArray, false);
                    UploadListingDetailsFragment.this.checkedCategoriesArray = checkedArray;

                    //Todo: add another false to checkedCategoriesArray instead of remaking a whole new array with all false.
                    //need to maintain the trues
                }
            }
        });
    }

    public long getBudget(){
        String finalBudget = binding.priceEditText.getText().toString();
        if(finalBudget.isEmpty()){
            return -1;
        }else{
            return (long)Double.parseDouble(binding.priceEditText.getText().toString());
        }
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

                    String finalCategoriesString = "";
                    for(int i=0; i < checkedCategoriesArray.length; i++){
                        if(checkedCategoriesArray[i]){
                            String capitalized = categories[i].substring(0, 1).toUpperCase() + categories[i].substring(1);
                            finalizedCategories.add(capitalized);
                            finalCategoriesString += capitalized + ",";
                        }
                    }

//                    if(finalCategoriesString.length() != 0){
//                        finalCategoriesString = finalCategoriesString.substring(0, finalCategoriesString.length()-1);
//                        finalizedCategories.add(finalCategoriesString);
//                    }

                    binding.chooseCategories.setText(finalCategoriesString);
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

    public class DecimalDigitsInputFilter implements InputFilter {
        private int mDigitsBeforeZero;
        private int mDigitsAfterZero;
        private Pattern mPattern;

        private static final int DIGITS_BEFORE_ZERO_DEFAULT = 100;
        private static final int DIGITS_AFTER_ZERO_DEFAULT = 100;

        public DecimalDigitsInputFilter(Integer digitsBeforeZero, Integer digitsAfterZero) {
            this.mDigitsBeforeZero = (digitsBeforeZero != null ? digitsBeforeZero : DIGITS_BEFORE_ZERO_DEFAULT);
            this.mDigitsAfterZero = (digitsAfterZero != null ? digitsAfterZero : DIGITS_AFTER_ZERO_DEFAULT);
            mPattern = Pattern.compile("-?[0-9]{0," + (mDigitsBeforeZero) + "}+((\\.[0-9]{0," + (mDigitsAfterZero)
                    + "})?)||(\\.)?");
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            String replacement = source.subSequence(start, end).toString();
            String newVal = dest.subSequence(0, dstart).toString() + replacement
                    + dest.subSequence(dend, dest.length()).toString();
            Matcher matcher = mPattern.matcher(newVal);
            if (matcher.matches())
                return null;

            if (TextUtils.isEmpty(source))
                return dest.subSequence(dstart, dend);
            else
                return "";
        }
    }
}
