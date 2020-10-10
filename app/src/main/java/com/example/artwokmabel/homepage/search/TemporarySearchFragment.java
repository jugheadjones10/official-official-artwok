package com.example.artwokmabel.homepage.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.artwokmabel.databinding.FragmentTempSearchBinding;
import com.example.artwokmabel.R;

public class TemporarySearchFragment extends Fragment {

    private EditText searchView;
    private FragmentTempSearchBinding binding;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_temp_search, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        ((AppCompatActivity)getActivity()).setSupportActionBar(binding.tempSearchToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        searchView.setInputType(InputType.TYPE_NULL);
//        searchView.onActionViewExpanded();
        binding.tempSearchEdit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_temporarySearchFragment_to_searchFragment);
            }
        });

    }

}
