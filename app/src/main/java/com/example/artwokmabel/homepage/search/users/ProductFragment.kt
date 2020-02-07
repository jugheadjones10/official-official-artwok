package com.algolia.instantsearch.guides.gettingstarted

//import com.algolia.instantsearch.guides.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.example.artwokmabel.R
import com.example.artwokmabel.homepage.search.listings.ListingsSearchFragment
import com.example.artwokmabel.homepage.search.posts.PostsSearchFragment
import com.example.artwokmabel.homepage.search.users.UsersSearchFragment
import com.example.artwokmabel.models.TabDetailsModel
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_product.*
import java.util.*


class ProductFragment : Fragment(){

    private lateinit var mSectionsPagerAdapter: SectionsPagerAdapter
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private lateinit var mySearchView: SearchView

    private val connection = ConnectionHandler()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View =  inflater.inflate(R.layout.fragment_product, container, false)

        viewPager = view.findViewById(R.id.search_view_pager)
        tabLayout = view.findViewById(R.id.search_results_tabs)

        mySearchView = view.findViewById(R.id.searchView)
        mySearchView.onActionViewExpanded();
        //Bringing in tabs manager
        (activity as AppCompatActivity).setSupportActionBar(search_results_toolbar)
        mSectionsPagerAdapter = SectionsPagerAdapter(childFragmentManager)
        populateViewPager()
        /////////

        return view
    }

    private fun populateViewPager() {
        var tab: TabDetailsModel

        tab = TabDetailsModel("Users", UsersSearchFragment(mySearchView))
        mSectionsPagerAdapter.addFragment(tab)
        tab = TabDetailsModel("Posts", PostsSearchFragment(mySearchView))
        mSectionsPagerAdapter.addFragment(tab)
        tab = TabDetailsModel("Listings", ListingsSearchFragment(mySearchView))
        mSectionsPagerAdapter.addFragment(tab)

        viewPager.adapter = mSectionsPagerAdapter
        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        tabLayout.setupWithViewPager(viewPager)
    }


    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        private val tabs = ArrayList<TabDetailsModel>()

        override fun getItem(position: Int): Fragment {
            return tabs[position].fragment
        }

        override fun getCount(): Int {
            return tabs.size
        }

        fun addFragment(tab: TabDetailsModel) {
            tabs.add(tab)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return tabs[position].tabName
        }
    }


//    override fun onDestroyView() {
//        super.onDestroyView()
//        connection.disconnect()
//    }
}