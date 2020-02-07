package com.example.artwokmabel.homepage.listing;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.artwokmabel.R;

public class DeliveryFragment extends Fragment {
    View view;
    private TextView delivery;
    private TextView refund;

    public DeliveryFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_listing_delivery, container, false);

        delivery = view.findViewById(R.id.delivery_text);
        refund = view.findViewById(R.id.refund_text);

        String deliveryText = getArguments().getString("delivery");
        String refundText = getArguments().getString("refund");

        Log.d("checkDelivery", deliveryText + "    " + refundText);

        delivery.setText(deliveryText);
        refund.setText(refundText);

        return view;
    }
}
