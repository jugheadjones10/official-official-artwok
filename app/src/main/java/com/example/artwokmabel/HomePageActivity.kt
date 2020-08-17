package com.example.artwokmabel

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import com.example.artwokmabel.chat.MessageFragmentDirections
import com.example.artwokmabel.chat.tabs.MessageChatsViewModel
import com.example.artwokmabel.chat.tabs.MessageOrdersViewModel
import com.example.artwokmabel.models.Listing
import com.example.artwokmabel.repos.FirestoreRepo
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_home_page.*



class HomePageActivity : AppCompatActivity() {

    private var currentNavController: LiveData<NavController>? = null
    private var instance: HomePageActivity? = null
    private var mAuth: FirebaseAuth? = null
    private var chatsViewModel: MessageChatsViewModel? = null
    private var ordersViewModel: MessageOrdersViewModel? = null

    companion object{
        var bottomNavBar: BottomNavigationView? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        bottomNavBar = nav_view
        instance = this
        mAuth = FirebaseAuth.getInstance()

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("westchester", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result!!.token
                Log.d("westchester", token)
                FirestoreRepo.getInstance().addTokenToFirestore(token)
            })

        chatsViewModel = ViewModelProviders.of(this).get(MessageChatsViewModel::class.java)
        ordersViewModel = ViewModelProviders.of(this).get(MessageOrdersViewModel::class.java)

        observeViewModel(chatsViewModel!!)

        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        } // Else, need to wait for onRestoreInstanceState

        val type = intent.getStringExtra("type")
        if(type == "message"){
            Log.d("fuckering", "Intent message works")
            val userId = intent.getStringExtra("fromId")
            val userUsername = intent.getStringExtra("fromUsername")
            val userProfile = intent.getStringExtra("fromProfileUrl")

            val action = MessageFragmentDirections.actionChatGraphToChatFragment(userId, userUsername, userProfile);
            nav_view.selectedItemId = R.id.chat_graph
            currentNavController?.value?.navigate(action)
        }else if (type == "listing"){
            val listing = intent.getSerializableExtra("listing")

            if(listing is Listing){
                val action = HomeGraphDirections.actionGlobalListingFragment(listing);
                nav_view.selectedItemId = R.id.home_graph
                currentNavController?.value?.navigate(action)
            }

        }

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        // Now that BottomNavigationBar has restored its instance state
        // and its selectedItemId, we can proceed with setting up the
        // BottomNavigationBar with Navigation
        setupBottomNavigationBar()
    }

    /**
     * Called on first creation and when restoring state.
     */
    private fun setupBottomNavigationBar() {

        val navGraphIds = listOf(R.navigation.home_graph, R.navigation.chat_graph, R.navigation.notif_graph, R.navigation.profile_graph)

        // Setup the bottom navigation view with a list of navigation graphs
        val controller = nav_view.setupWithNavController(
                navGraphIds = navGraphIds,
                fragmentManager = supportFragmentManager,
                containerId = R.id.nav_host_container,
                intent = intent
        )

        // Whenever the selected controller changes, setup the action bar.
//        controller.observe(this, Observer { navController ->
//            setupActionBarWithNavController(navController)
//        })
        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    private fun observeViewModel(chatsViewModel: MessageChatsViewModel) {
        chatsViewModel.numOfUnreadInChatsTab.observe(this, Observer { numChats ->
            ordersViewModel!!.numOfUnreadInOffersTab.observe(this@HomePageActivity, Observer { numOffers ->
                val badge: BadgeDrawable = nav_view.getOrCreateBadge(R.id.chat_graph)
                badge.verticalOffset = 15
                badge.badgeTextColor = Color.WHITE
                val total = numChats + numOffers
                if (total > 0) {
                    badge.isVisible = true
                    badge.number = total
                } else {
                    badge.isVisible = false
                }
            })
        })
    }

    fun getInstance(): HomePageActivity? {
        return instance
    }
}
