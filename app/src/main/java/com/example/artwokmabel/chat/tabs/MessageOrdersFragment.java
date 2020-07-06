package com.example.artwokmabel.chat.tabs;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import com.example.artwokmabel.chat.MessageFragment;
import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.MessageChatsFragmentBinding;
import com.example.artwokmabel.databinding.MessageOrdersFragmentBinding;
import com.example.artwokmabel.models.OrderChat;
import com.example.artwokmabel.models.User;
import com.google.android.material.badge.BadgeDrawable;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import java.util.List;

public class MessageOrdersFragment extends Fragment {

    private MessageOrdersFragmentBinding binding;
    private MessageOrdersAdapter adapter;
    private MessageOrdersViewModel viewModel;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.message_orders_fragment, container, false);

        viewModel = ViewModelProviders.of(this).get(MessageOrdersViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Below is a tricky line - take note of it. Why won't it work with the single argument version of findNavController?
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_container);
        adapter = new MessageOrdersAdapter(getActivity(), navController);
        binding.chatsFragmentRecyclerview.setAdapter(adapter);

        observeViewModel(viewModel);
    }

    private void observeViewModel(MessageOrdersViewModel viewModel) {
        viewModel.getOrdersAndSellsObservable().observe(getViewLifecycleOwner(), new Observer<List<OrderChat>>() {
            @Override
            public void onChanged(@Nullable List<OrderChat> orderChats) {
                Log.d("chattingwith", Integer.toString(orderChats.size()));

                if (orderChats != null) {
                    Log.d("ordersandstuff", "Number of active from observeViewModel : " + orderChats.size());
                    adapter.setOrderChatsList(orderChats);
                }

            }
        });

        viewModel.getNumOfUnreadInOffersTab().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                BadgeDrawable badge = MessageFragment.getInstance().binding.messageTabs.getTabAt(2).getOrCreateBadge();
                badge.setBadgeTextColor(Color.WHITE);
                if(integer > 0){
                    badge.setVisible(true);
                    badge.setNumber(integer);
                }else{
                    badge.setVisible(false);
                }
            }
        });
    }
}
