package com.example.artwokmabel.profile.uploadlisting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
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
import com.google.android.play.core.internal.s;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        binding.priceEditText.setFilters(new InputFilter[]{new DigitsInputFilter(3, 2, Double.POSITIVE_INFINITY)});

        finalizedCategories = new ArrayList<>();
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
                    UploadListingDetailsFragment.this.categories = allCategories.toArray(new String[allCategories.size()]);

                    boolean checkedArray[] = new boolean[allCategories.size()];
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

    public class DigitsInputFilter implements InputFilter {

        private final String DOT = ".";

        private int mMaxIntegerDigitsLength;
        private int mMaxDigitsAfterLength;
        private double mMax;


        public DigitsInputFilter(int maxDigitsBeforeDot, int maxDigitsAfterDot, double maxValue) {
            mMaxIntegerDigitsLength = maxDigitsBeforeDot;
            mMaxDigitsAfterLength = maxDigitsAfterDot;
            mMax = maxValue;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            String allText = getAllText(source, dest, dstart);
            String onlyDigitsText = getOnlyDigitsPart(allText);

            if (allText.isEmpty()) {
                return null;
            } else {
                double enteredValue;
                try {
                    enteredValue = Double.parseDouble(onlyDigitsText);
                } catch (NumberFormatException e) {
                    return "";
                }
                return checkMaxValueRule(enteredValue, onlyDigitsText);
            }
        }


        private CharSequence checkMaxValueRule(double enteredValue, String onlyDigitsText) {
            if (enteredValue > mMax) {
                return "";
            } else {
                return handleInputRules(onlyDigitsText);
            }
        }

        private CharSequence handleInputRules(String onlyDigitsText) {
            if (isDecimalDigit(onlyDigitsText)) {
                return checkRuleForDecimalDigits(onlyDigitsText);
            } else {
                return checkRuleForIntegerDigits(onlyDigitsText.length());
            }
        }

        private boolean isDecimalDigit(String onlyDigitsText) {
            return onlyDigitsText.contains(DOT);
        }

        private CharSequence checkRuleForDecimalDigits(String onlyDigitsPart) {
            String afterDotPart = onlyDigitsPart.substring(onlyDigitsPart.indexOf(DOT), onlyDigitsPart.length() - 1);
            if (afterDotPart.length() > mMaxDigitsAfterLength) {
                return "";
            }
            return null;
        }

        private CharSequence checkRuleForIntegerDigits(int allTextLength) {
            if (allTextLength > mMaxIntegerDigitsLength) {
                return "";
            }
            return null;
        }

        private String getOnlyDigitsPart(String text) {
            return text.replaceAll("[^0-9?!\\.]", "");
        }

        private String getAllText(CharSequence source, Spanned dest, int dstart) {
            String allText = "";
            if (!dest.toString().isEmpty()) {
                if (source.toString().isEmpty()) {
                    allText = deleteCharAtIndex(dest, dstart);
                } else {
                    allText = new StringBuilder(dest).insert(dstart, source).toString();
                }
            }
            return allText;
        }

        private String deleteCharAtIndex(Spanned dest, int dstart) {
            StringBuilder builder = new StringBuilder(dest);
            builder.deleteCharAt(dstart);
            return builder.toString();
        }
    }
}
