package com.example.artwokmabel.homepage.Activities;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.example.artwokmabel.databinding.ActivityIndivListingNewBinding;
import com.example.artwokmabel.homepage.adapters.IndivListViewPagerAdapter;
import com.example.artwokmabel.homepage.fragments.Indivlistings.DeliveryFragment;
import com.example.artwokmabel.homepage.fragments.Indivlistings.DescFragment;
import com.example.artwokmabel.homepage.fragments.Indivlistings.FaqFragment;
import com.example.artwokmabel.homepage.fragments.Indivlistings.ReviewFragment;
import com.example.artwokmabel.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class IndivListingActivity extends AppCompatActivity {

    private int[] mImages = new int[] {
            R.drawable.tiger, R.drawable.handmade_jewelry, R.drawable.chocolate_cake,
            R.drawable.bags, R.drawable.paintings, R.drawable.toys
    };


    private TabLayout tabLayout;
    private ViewPager viewPager;

    private ActivityIndivListingNewBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_indiv_listing_new);

        binding.indivListingCarouselview.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(mImages[position]);
            }
        });

        tabLayout = findViewById(R.id.indiv_listing_tablayout);
        viewPager = findViewById(R.id.indiv_listing_viewpager);

        IndivListViewPagerAdapter adapter = new IndivListViewPagerAdapter(getSupportFragmentManager());
        adapter.AddFragment(new DescFragment(), "Description");
        adapter.AddFragment(new ReviewFragment(), "Reviews");
        adapter.AddFragment(new DeliveryFragment(), "Delivery/Refund");
        adapter.AddFragment(new FaqFragment(), "FAQ");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    //    TextView post_desc;
//    TextView listing_name;
//    TextView price;
//    TextView username;
//    String refundPolicy;
//    String delivery;
//
//    CarouselView carouselView;
//
//    ExpandableListView expandList;
//    CustomExpandableListAdapter adapter;
//    HashMap<String, List<String>> expandableListDetail;
//    List<String> expandableListTitle;
//
//    public NestedScrollView nestedScroll;
//
//    TextInputLayout commentBar;
//    TextInputEditText commentEdit;
//
//    private static IndivListingActivity instance = null;
//
//    int phoneHeight;
//    Boolean heightSet;
//
//    public static IndivListingActivity getInstance(){
//        return instance;
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_indiv_listing);
//        instance = this;
//        post_desc = findViewById(R.id.item_desc);
//        listing_name = findViewById(R.id.item_name);
//        price = findViewById(R.id.item_price);
//        username = findViewById(R.id.listing_username);
//
//        carouselView = findViewById(R.id.post_image_me);
//        nestedScroll = findViewById(R.id.nested_scroll);
//        expandList = findViewById(R.id.listing_expandables);
//        commentBar = findViewById(R.id.comment_bar);
//        commentEdit = findViewById(R.id.edit_comment);
//        getIncomingIntent();
//
//
//        //Expandable lists
//        expandableListDetail = initListData();
//
//        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
//        adapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
//        expandList.setAdapter(adapter);
//
//        heightSet = false;
//
//        DisplayMetrics metrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        phoneHeight = metrics.heightPixels;
//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.indiv_toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        //commentBar.bringToFront();
//        //SET POSITION OF COMMENT BAR AND BRING TO FRONT
//
////        ViewTreeObserver vto = commentBar.getViewTreeObserver();
////        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
////            @Override
////            public void onGlobalLayout() {
////                if(heightSet == false){
////                    //SET POSITION OF COMMENT BAR AND BRING TO FRONT
////                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) commentBar.getLayoutParams();
////                    Log.d("FUCK", "THIS IS SCREEN HEIGHT" + phoneHeight);
////                    params.topMargin = phoneHeight - commentBar.getHeight();
////                    Log.d("FUCK", "Height of comment bar in pixels" + commentBar.getHeight());
////                    heightSet = true;
////                }
////            }
////        });
//
//        //Reviews Fragment
////        ViewCommentsFragment fragment  = new ViewCommentsFragment();
////        Bundle args = new Bundle();
////        args.putString("postid", getIntent().getStringExtra("postid"));
////        args.putString("userid", getIntent().getStringExtra("userid"));
////        fragment.setArguments(args);
//
////        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
////        transaction.replace(R.id.indiv_post_container, fragment);
//        //transaction.addToBackStack("View Comments");
////        transaction.commit();
//
//        //Add on click listener to send icon
////        commentBar.setEndIconOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                if(!commentEdit.getText().toString().equals("")){
////                    Log.d("FUCK", "onClick: attempting to submit new comment.");
////                    fragment.addNewComment(commentEdit.getText().toString());
////
////                    commentEdit.setText("");
////
////                    closeKeyboard();
////                }else{
////                    Toast.makeText(getApplicationContext(), "you can't post a blank comment", Toast.LENGTH_SHORT).show();
////                }
////            }
////        });
//    }
//
//    private HashMap<String, List<String>> initListData() {
//
//        HashMap<String, List<String>> expandableListDetail = new HashMap<>();
//
//        List<String> refund = new ArrayList<String>();
//        refund.add(refundPolicy);
//
//        List<String> listDelivery = new ArrayList<String>();
//        listDelivery.add(delivery);
//
//        expandableListDetail.put("Refund and exchange policy", refund);
//        expandableListDetail.put("Delivery", listDelivery);
//        return expandableListDetail;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//    private void closeKeyboard(){
//        View view = getCurrentFocus();
//        if(view != null){
//            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//        }
//    }
//
//    private void getIncomingIntent(){
//
//            Log.d("TAG", "getIncomingIntent: found intent extras.");
//
//            String descText = getIntent().getStringExtra("itemDesc");
//            String itemName = getIntent().getStringExtra("itemname");
//            String userName = getIntent().getStringExtra("username");
//            String intentPrice = getIntent().getStringExtra("price");
//            refundPolicy = getIntent().getStringExtra("refund_exchange");
//            delivery = getIntent().getStringExtra("delivery");
//
//            ArrayList<String> images = getIntent().getStringArrayListExtra("photos");
//            ImageListener imageListener = new ImageListener() {
//                @Override
//                public void setImageForPosition(int position, ImageView imageView) {
//                    //imageView.setImageResource(sampleImages[position]);
//                    Picasso.get()
//                            .load(images.get(position))
//                            .placeholder(R.drawable.user)
//                            .error(R.drawable.rick_and_morty)
//                            .into(imageView);
//                }
//            };
//
//            if(images != null){
//                carouselView.setPageCount(images.size());
//
//                carouselView.setImageListener(imageListener);
//            }
//
//            post_desc.setText(descText);
//            listing_name.setText(itemName);
//            price.setText(intentPrice);
//            username.setText(userName);
//
//            //NOTICE :  natively set icons, title, and subtitle on toolbar next time
//    }
//
//    public class CustomExpandableListAdapter extends BaseExpandableListAdapter {
//
//        private Context context;
//        private List<String> expandableListTitle;
//        private HashMap<String, List<String>> expandableListDetail;
//
//        public CustomExpandableListAdapter(Context context, List<String> expandableListTitle,
//                                           HashMap<String, List<String>> expandableListDetail) {
//            this.context = context;
//            this.expandableListTitle = expandableListTitle;
//            this.expandableListDetail = expandableListDetail;
//        }
//
//        @Override
//        public Object getChild(int listPosition, int expandedListPosition) {
//            return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
//                    .get(expandedListPosition);
//        }
//
//        @Override
//        public long getChildId(int listPosition, int expandedListPosition) {
//            return expandedListPosition;
//        }
//
//        @Override
//        public View getChildView(int listPosition, final int expandedListPosition,
//                                 boolean isLastChild, View convertView, ViewGroup parent) {
//            final String expandedListText = (String) getChild(listPosition, expandedListPosition);
//            if (convertView == null) {
//                LayoutInflater layoutInflater = (LayoutInflater) this.context
//                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                convertView = layoutInflater.inflate(R.layout.expandable_list_item, null);
//            }
//            TextView expandedListTextView = (TextView) convertView
//                    .findViewById(R.id.expandedListItem);
//            expandedListTextView.setText(expandedListText);
//            return convertView;
//        }
//
//        @Override
//        public int getChildrenCount(int listPosition) {
//            return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
//                    .size();
//        }
//
//        @Override
//        public Object getGroup(int listPosition) {
//            return this.expandableListTitle.get(listPosition);
//        }
//
//        @Override
//        public int getGroupCount() {
//            return this.expandableListTitle.size();
//        }
//
//        @Override
//        public long getGroupId(int listPosition) {
//            return listPosition;
//        }
//
//        @Override
//        public View getGroupView(int listPosition, boolean isExpanded,
//                                 View convertView, ViewGroup parent) {
//            String listTitle = (String) getGroup(listPosition);
//            if (convertView == null) {
//                LayoutInflater layoutInflater = (LayoutInflater) this.context.
//                        getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                convertView = layoutInflater.inflate(R.layout.expandable_list_header, null);
//            }
//            TextView listTitleTextView = (TextView) convertView
//                    .findViewById(R.id.listTitle);
//            listTitleTextView.setTypeface(null, Typeface.BOLD);
//            listTitleTextView.setText(listTitle);
//            return convertView;
//        }
//
//        @Override
//        public boolean hasStableIds() {
//            return false;
//        }
//
//        @Override
//        public boolean isChildSelectable(int listPosition, int expandedListPosition) {
//            return true;
//        }
//    }
}
