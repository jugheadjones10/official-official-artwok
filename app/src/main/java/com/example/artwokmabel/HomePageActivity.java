package com.example.artwokmabel;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.artwokmabel.chat.MessageFragment;
import com.example.artwokmabel.chat.tabs.MessageChatsViewModel;
import com.example.artwokmabel.chat.tabs.MessageOrdersViewModel;
import com.example.artwokmabel.homepage.homepagewrapper.HomeTabsFragment;
import com.example.artwokmabel.notifs.NotifsFragment;
import com.example.artwokmabel.profile.user.ProfileFragment;
import com.example.artwokmabel.repos.FirestoreRepo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firestore.v1.FirestoreGrpc;


public class HomePageActivity extends AppCompatActivity {

    private static HomePageActivity instance = null;
    public BottomNavigationView navView;
    private FirebaseAuth mAuth;
    private MessageChatsViewModel chatsViewModel;
    private MessageOrdersViewModel ordersViewModel;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        instance = this;
        mAuth = FirebaseAuth.getInstance();

        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.setSelectedItemId(R.id.main_home_fragment);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("westchester", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        Log.d("westchester", token);
                        FirestoreRepo.getInstance().addTokenToFirestore(token);
                    }
                });

        if(savedInstanceState == null){
            loadFragment(new HomeTabsFragment());
        }

        chatsViewModel = ViewModelProviders.of(this).get(MessageChatsViewModel.class);
        ordersViewModel = ViewModelProviders.of(this).get(MessageOrdersViewModel.class);

        observeViewModel(chatsViewModel);
    }

    private void observeViewModel(MessageChatsViewModel chatsViewModel){

        chatsViewModel.getNumOfUnreadInChatsTab().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer numChats) {

//                BadgeDrawable badge = MessageFragment.getInstance().binding.messageTabs.getTabAt(1).getOrCreateBadge();
//                badge.setBadgeTextColor(Color.WHITE);
//                if(numChats > 0){
//                    badge.setVisible(true);
//                    badge.setNumber(numChats);
//                }else{
//                    badge.setVisible(false);
//                }

                ordersViewModel.getNumOfUnreadInOffersTab().observe(HomePageActivity.this, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer numOffers) {

//                        BadgeDrawable offersTabBadge = MessageFragment.getInstance().binding.messageTabs.getTabAt(2).getOrCreateBadge();
//                        offersTabBadge.setBadgeTextColor(Color.WHITE);
//                        if(numChats > 0){
//                            offersTabBadge.setVisible(true);
//                            offersTabBadge.setNumber(numChats);
//                        }else{
//                            offersTabBadge.setVisible(false);
//                        }

                        BadgeDrawable badge = navView.getOrCreateBadge(R.id.main_chat_fragment);
                        badge.setVerticalOffset(15);
                        badge.setBadgeTextColor(Color.WHITE);

                        int total = numChats + numOffers;
                        if(total > 0){
                            badge.setVisible(true);
                            badge.setNumber(total);
                        }else{
                            badge.setVisible(false);
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(getIntent().getStringExtra("fromWho") != null) {
            if (getIntent().getStringExtra("fromWho").equals("editProfile")){
                navView.setSelectedItemId(R.id.main_profile_fragment);
            }
        }
    }

    public static HomePageActivity getInstance(){
        return instance;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.main_chat_fragment:
                        fragment=new MessageFragment();
                        loadFragment(fragment);
                        return true;
                    case R.id.main_home_fragment:
                        fragment=new HomeTabsFragment();
                        loadFragment(fragment);
                        return true;
                    case R.id.main_notifs_fragment:
                        fragment=new NotifsFragment();
                        loadFragment(fragment);
                        return true;
                    case R.id.main_profile_fragment:
                        fragment = new ProfileFragment(mAuth.getCurrentUser().getUid());
                        loadFragment(fragment);
                        return true;
                }
                return false;
            };

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}