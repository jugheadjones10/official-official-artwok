package com.example.artwokmabel;

import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.artwokmabel.chat.tabs.MessageChatsViewModel;
import com.example.artwokmabel.chat.tabs.MessageOrdersViewModel;
import com.example.artwokmabel.databinding.ActivityHomePageBinding;
import com.example.artwokmabel.repos.FirestoreRepo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;


public class HomePageActivityOld extends AppCompatActivity {

    private NavController navController;
    private ActivityHomePageBinding binding;
    private AppBarConfiguration appBarConfiguration;


    private static HomePageActivityOld instance = null;
    public BottomNavigationView navView;
    private FirebaseAuth mAuth;
    private MessageChatsViewModel chatsViewModel;
    private MessageOrdersViewModel ordersViewModel;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_page);
        if (savedInstanceState == null) {
            setUpBottomNavigationBar();
        }

        instance = this;
        mAuth = FirebaseAuth.getInstance();

//        navView = findViewById(R.id.nav_view);
//        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//        navView.setSelectedItemId(R.id.homeTabsFragment);

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
            //loadFragment(new HomeTabsFragment());
        }

        chatsViewModel = ViewModelProviders.of(this).get(MessageChatsViewModel.class);
        ordersViewModel = ViewModelProviders.of(this).get(MessageOrdersViewModel.class);

        observeViewModel(chatsViewModel);
    }

    @Override
    public void onRestoreInstanceState (Bundle savedInstanceState, PersistableBundle persistentState){
        setUpBottomNavigationBar();
    }

    private void observeViewModel(MessageChatsViewModel chatsViewModel){

        chatsViewModel.getNumOfUnreadInChatsTab().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer numChats) {
                ordersViewModel.getNumOfUnreadInOffersTab().observe(HomePageActivityOld.this, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer numOffers) {
                        BadgeDrawable badge = binding.navView.getOrCreateBadge(R.id.chat_graph);
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

    private void setUpBottomNavigationBar(){
//        navController = Navigation.findNavController(this, R.id.main_nav_host_fragment);
//        NavigationUI.setupWithNavController(binding.navView, navController);

        //appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(getIntent().getStringExtra("fromWho") != null) {
            if (getIntent().getStringExtra("fromWho").equals("editProfile")){
                navView.setSelectedItemId(R.id.profile_graph);
            }
        }
    }

    public static HomePageActivityOld getInstance(){
        return instance;
    }

//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = item -> {
//                Fragment fragment = null;
//                switch (item.getItemId()) {
//                    case R.id.messageFragment:
//                        fragment=new MessageFragment();
//                        loadFragment(fragment);
//                        return true;
//                    case R.id.homeTabsFragment:
//                        fragment=new HomeTabsFragment();
//                        loadFragment(fragment);
//                        return true;
//                    case R.id.notifsFragment:
//                        fragment=new NotifsFragment();
//                        loadFragment(fragment);
//                        return true;
//                    case R.id.profileFragment:
//                        fragment = new ProfileFragment(mAuth.getCurrentUser().getUid());
//                        loadFragment(fragment);
//                        return true;
//                }
//                return false;
//            };
//
//    public void loadFragment(Fragment fragment) {
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.container, fragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }
}