package com.example.artwokmabel.profile.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.artwokmabel.R;
import com.example.artwokmabel.chat.personalchat.MessageAdapter;
import com.example.artwokmabel.databinding.FragmentDashboardBinding;
import com.example.artwokmabel.models.Message;
import com.example.artwokmabel.models.OrderChat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {
    private String userId;
    private FragmentDashboardBinding binding;
    private DashboardFragmentViewModel viewModel;
    private MessageAdapter messageAdapter;
    private List<Message> messagesList = new ArrayList<>();
    private FirebaseAuth mAuth;

    public DashboardFragment(String userId) {
        this.userId = userId;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false);
        mAuth = FirebaseAuth.getInstance();
        messageAdapter = new MessageAdapter(messagesList, new OrderChat(), getContext());
        //binding.dashboardMessagesRecyclerView.setAdapter(messageAdapter);

        //This part might be a problem
        viewModel = ViewModelProviders.of(this).get(DashboardFragmentViewModel.class);
//        viewModel.getDashboardMessagesObservable(userId).observe(this, new Observer<List<Message>>() {
//            @Override
//            public void onChanged(List<Message> messages) {
//                if(messages != null){
//                    messagesList = messages;
//                    messageAdapter.notifyDataSetChanged();
//                }
//            }
//        });
        setUpMessageBar();

        return binding.getRoot();
    }

    private void setUpMessageBar() {
//        binding.sendMessageBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String messageText = binding.inputMessage.getText().toString();
//
//                if (TextUtils.isEmpty(messageText)) {
//                    Toast.makeText(getActivity(), "first write your message...", Toast.LENGTH_SHORT).show();
//                } else {
//                    viewModel.addDashboardMessage(mAuth.getCurrentUser().getUid(), userId, messageText);
//                }
//
//                binding.inputMessage.setText("");
//            }
//        });
    }

    @Override
    public void onStart() {
        super.onStart();

        DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.child("Dashboard").child(userId)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        Message message = dataSnapshot.getValue(Message.class);

                        messagesList.add(message);
                        messageAdapter.notifyDataSetChanged();
                        //binding.dashboardMessagesRecyclerView.scrollToPosition(binding.dashboardMessagesRecyclerView.getAdapter().getItemCount() - 1);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

}
