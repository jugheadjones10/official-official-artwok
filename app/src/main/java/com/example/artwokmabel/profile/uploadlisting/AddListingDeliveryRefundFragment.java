package com.example.artwokmabel.profile.uploadlisting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.FragmentUploadListingDeliveryBinding;

public class AddListingDeliveryRefundFragment  extends Fragment {

    private FragmentUploadListingDeliveryBinding binding;
    private static AddListingDeliveryRefundFragment instance;

    public static AddListingDeliveryRefundFragment getInstance(){
        return instance;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_upload_listing_delivery, container, false);

        instance = this;

        return binding.getRoot();
    }

    public String getDelivery(){
        return binding.deliveryDetails.getText().toString();
    }

    public String getRefund(){
        return binding.refundPolicy.getText().toString();
    }
}
