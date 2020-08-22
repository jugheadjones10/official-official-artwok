package com.example.artwokmabel.profile.uploadlisting;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.FragmentUploadListingBinding;
import com.example.artwokmabel.homepage.callbacks.ImagePickerCallback;
import com.example.artwokmabel.profile.people.PeoplePagerAdapter;
import com.example.artwokmabel.profile.uploadpost.UploadPostViewModel;
import com.example.artwokmabel.repos.FirestoreRepo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.example.artwokmabel.profile.utils.ImagePickerActivity.SHOW_ALL_OPTIONS;
import static com.example.artwokmabel.profile.utils.ImagePickerActivity.SHOW_IMAGE_OPTIONS_ONLY;

public class UploadListingFragment extends Fragment {

    public static final int REQUEST_IMAGE = 100;

    private FragmentUploadListingBinding binding;
    private UploadListingPagerAdapter adapter;
    private UploadListingViewModel viewModel;
    private ArrayList<String> uploadImageUri = new ArrayList<>();
    private NavController navController;

    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_upload_listing, container, false);
        viewModel = ViewModelProviders.of(this).get(UploadListingViewModel.class);

        binding.uploadProgressL.setVisibility(View.GONE);
        binding.carouselView.setVisibility(View.GONE);
        binding.uploadPicL.setVisibility(View.VISIBLE);
        binding.setUploadListingFragment(this);


        ((AppCompatActivity)requireActivity()).setSupportActionBar(binding.indivToolbar);
        ((AppCompatActivity)requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)requireActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        setHasOptionsMenu(true);

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_container);

        adapter = new UploadListingPagerAdapter(requireActivity());
        binding.pager.setAdapter(adapter);

        viewModel.getUploadSuccess().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean == true){
                    binding.uploadProgressL.setVisibility(View.GONE);
                    navController.navigateUp();
                }
            }
        });

        viewModel.getListingImageUri().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String imageUri) {
                if(imageUri != null){
                    if(uploadImageUri.size() == 0){
                        uploadImageUri.add(imageUri);
                    }
                    binding.carouselView.setVisibility(View.VISIBLE);
                    binding.uploadPicL.setVisibility(View.GONE);

                    binding.carouselView.setImageListener((position, imageView) -> {
                        Picasso.get().load(uploadImageUri.get(0)).into(imageView);
                    });
                    binding.carouselView.setPageCount(1);
                }
            }
        });

        new TabLayoutMediator(binding.tabLayout, binding.pager,
            new TabLayoutMediator.TabConfigurationStrategy() {
                @Override
                public void onConfigureTab(TabLayout.Tab tab, int position) {
                    if(position == 0){
                        tab.setText("Description");
                    }else if(position == 1){
                        tab.setText("Details");
                    }else if(position == 2){
                        tab.setText("Delivery/Refund");
                    }else{
                        tab.setText("FAQ");
                    }
                }
            }
        ).attach();
    }

    private void setCarouselViewImage(){

    }

    public void onUploadImageClicked(){
        new ImagePickerCallback(requireActivity(), REQUEST_IMAGE, viewModel, SHOW_IMAGE_OPTIONS_ONLY).onImagePickerClicked();
        viewModel.getImagePath().observe(getViewLifecycleOwner(), new Observer<Uri>() {
            @Override
            public void onChanged(Uri uri) {
                if(uri != null){
                    Log.d("urivalue", uri.toString());
                    Uri fileUri = uri;
                    String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    int randomNumber = new Random().nextInt();
                    String fileName = Integer.toString(randomNumber);

                    StorageReference fileToUpload = storageReference.child("Images").child(currentUserId).child(fileName); // randomize name

                    fileToUpload.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageReference.child("Images").child(currentUserId).child(fileName).getDownloadUrl().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {

                                    String imageUri = task.getResult().toString();
                                    Log.d("imageurl", imageUri);
                                    viewModel.setListingImageUri(imageUri);
//                                    uploadImageUri.add(imageUri);
//
//                                    binding.carouselView.setVisibility(View.VISIBLE);
//                                    binding.uploadPicL.setVisibility(View.GONE);
//
//                                    binding.carouselView.setImageListener((position, imageView) -> {
//                                        Picasso.get().load(imageUri).into(imageView);
//                                    });
//                                    binding.carouselView.setPageCount(1);
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(requireActivity(), "Upload image failed", Toast.LENGTH_LONG).show();
                        }
                    });

                    viewModel.setResultOk(null);
                }
            }
        });
    }

    public void onUploadListingClicked(){

        String postDesc = UploadListingDescFragment.getInstance().getDesc();
        String postTitle = UploadListingDescFragment.getInstance().getTitle();

        ArrayList<String> categories;
        long budget;
        if(UploadListingDetailsFragment.getInstance() == null){
            categories = null;
            budget = -1;
        }else{
            categories = UploadListingDetailsFragment.getInstance().getCategories();
            budget = UploadListingDetailsFragment.getInstance().getBudget();
        }

        String deliveryDetails;
        String refundPolicy;
        if(UploadListingDeliveryFragment.getInstance() == null){
            deliveryDetails = "";
            refundPolicy = "";
        }else{
            deliveryDetails = UploadListingDeliveryFragment.getInstance().getDelivery();
            refundPolicy = UploadListingDeliveryFragment.getInstance().getRefund();
        }

        if(postDesc.isEmpty()){
            Toast.makeText(requireContext(), "Please fill the description for your listing.", Toast.LENGTH_LONG).show();
        } else if(postTitle.isEmpty()){
            Toast.makeText(requireContext(), "Please fill the title for your listing.", Toast.LENGTH_LONG).show();
        }else if(deliveryDetails.isEmpty()){
            Toast.makeText(requireContext(), "Please enter delivery details.", Toast.LENGTH_LONG).show();
        } else if(refundPolicy.isEmpty()){
            Toast.makeText(requireContext(), "Please enter your refund policy", Toast.LENGTH_LONG).show();
        } else if(categories == null || categories.size() == 0){
            Toast.makeText(requireContext(), "Please select the category for your request.", Toast.LENGTH_LONG).show();
        } else if(budget == -1){
            Toast.makeText(requireContext(), "Please enter the price.", Toast.LENGTH_LONG).show();
        } else if(uploadImageUri.isEmpty() || uploadImageUri == null){
            Toast.makeText(requireContext(), "Please add at least one photo of your listing.", Toast.LENGTH_LONG).show();
        }else{
            binding.uploadProgressL.setVisibility(View.VISIBLE);
            viewModel.uploadNewListing(postTitle, postDesc, categories, budget, deliveryDetails, refundPolicy, FirebaseAuth.getInstance().getCurrentUser().getUid(), uploadImageUri);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_upload_listing, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.upload:
                onUploadListingClicked();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
