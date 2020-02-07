package com.example.artwokmabel.chat.activities;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.artwokmabel.R;
import com.example.artwokmabel.chat.models.UserUserModel;
import com.example.artwokmabel.databinding.ZActivityChatprofileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ChatProfileActivity extends AppCompatActivity {

    final String TAG = "rarara";
    private ImageButton btnCreatechat;
    private ImageView profileImg;
    private TextView chatUsername;
    private String otherUserPic;
    private String otherUserName;
    public ArrayList<String> myChatRooms;
    public ArrayList<String> otherChatRooms;
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    private String destinationUid;
    public Context context;

    private ChatProfileViewModel viewModel;
    private ZActivityChatprofileBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        UserUserModel intentUser = new UserUserModel(
                getIntent().getStringExtra("destinationUsername"),
                getIntent().getStringExtra("destinationProfileUrl"),
                getIntent().getStringExtra("destinationUid"),
                getIntent().getStringArrayListExtra("destinationChatrooms")
        );

        destinationUid = intentUser.getUid();
        otherChatRooms = intentUser.getChatrooms();

        binding = DataBindingUtil.setContentView(this, R.layout.z_activity_chatprofile);
        binding.setUser(intentUser);

        Picasso.get().load(intentUser.getProfileImageUrl()).into(binding.chatprofileImageview);

        viewModel = ViewModelProviders.of(this).get(ChatProfileViewModel.class);
        observeViewModel(viewModel);
    }

    public class OnChatClicked{
        public void onChatClicked(){
            ArrayList<String> tempList = new ArrayList<>();
            if(myChatRooms == null || otherChatRooms == null){

            }else{
                for (String temp : myChatRooms) {
                    if (otherChatRooms.contains(temp)) {
                        tempList.add(temp);
                    }
                }
            }

//            Intent intent = new Intent(context, NewChatFragment.class);
//            intent.putExtra("destinationUid", destinationUid);
//            intent.putExtra("otherUserPic", otherUserPic);
            Bundle args = new Bundle();
            args.putString("destinationUid", destinationUid);
            args.putString("otherUserPic", otherUserPic);

            if(tempList.size() == 0){
                String newChatRoom = viewModel.setNewChatRoom(destinationUid);
                //intent.putExtra("chatRoomId", newChatRoom);
                args.putString("chatRoomId", newChatRoom);
            }else{
                //intent.putExtra("chatRoomId", tempList.get(0));
                args.putString("chatRoomId", tempList.get(0));
            }

            NewChatFragment frag = new NewChatFragment();
            frag.setArguments(args);
            loadFragment(frag);
            // create an animation effect sliding from left to right
//            ActivityOptions activityOptions = null;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                activityOptions = ActivityOptions.makeCustomAnimation(context, R.anim.fromright,R.anim.toleft);
//                startActivity(intent,activityOptions.toBundle());
//            } else {
//                startActivity(intent);
//            }
        }
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        androidx.fragment.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_linear, fragment);
        fragmentTransaction.commit();
    }


    private void observeViewModel(ChatProfileViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getChatRoomsObservable().observe(this, new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(@Nullable ArrayList<String> chatrooms) {
                if (chatrooms != null) {
                    myChatRooms = chatrooms;
                    binding.setMessagecallback(new OnChatClicked());
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.frombottom, R.anim.totop);
    }
}