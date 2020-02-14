package com.example.artwokmabel.chat.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.example.artwokmabel.chat.activities.ChatOrderActivity;
import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.MessageChatsFragmentBinding;
import com.example.artwokmabel.databinding.MessageOrdersFragmentBinding;
import com.example.artwokmabel.models.OrderChat;
import com.example.artwokmabel.models.User;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import java.util.List;

public class MessageOrdersFragment extends Fragment {

    private MessageOrdersFragmentBinding binding;
    private MessageOrdersAdapter adapter;
    private MessageOrdersViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.message_orders_fragment, container, false);

        adapter = new MessageOrdersAdapter(getActivity());
        binding.chatsFragmentRecyclerview.setAdapter(adapter);

        viewModel = ViewModelProviders.of(this).get(MessageOrdersViewModel.class);
        observeViewModel(viewModel);

        return binding.getRoot();
    }

    private void observeViewModel(MessageOrdersViewModel viewModel) {
        viewModel.getOrdersAndSellsObservable().observe(this, new Observer<List<OrderChat>>() {
            @Override
            public void onChanged(@Nullable List<OrderChat> orderChats) {
                Log.d("chattingwith", Integer.toString(orderChats.size()));

                if (orderChats != null) {
                    Log.d("ordersandstuff", "Number of active from observeViewModel : " + orderChats.size());
                    adapter.setOrderChatsList(orderChats);
                }

            }
        });
    }

}
