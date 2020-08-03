package com.example.artwokmabel.homepage.postsfeed;

import android.widget.ImageView;

import com.example.artwokmabel.models.Listing;

public class ListingCallback {

    public ListingCallback.OnListingClicked onListingClicked;
    public ListingCallback.OnProfileClicked onProfileClicked;
    public ListingCallback.OnShareClicked onShareClicked;
    public ListingCallback.OnFavClicked onFavClicked;

    public interface OnListingClicked{
        void onListingClicked(Listing listing);
    }

    public interface OnProfileClicked{
        void onProfileClicked(Listing listing);
    }

    public interface OnShareClicked{
        void onShareClicked(Listing listing);
    }

    public interface OnFavClicked{
        void onFavClicked(Listing listing, ImageView favorite);
    }

    public ListingCallback(ListingCallback.OnListingClicked onListingClicked, ListingCallback.OnProfileClicked onProfileClicked, ListingCallback.OnShareClicked onShareClicked, ListingCallback.OnFavClicked onFavClicked){
        this.onListingClicked = onListingClicked;
        this.onProfileClicked = onProfileClicked;
        this.onShareClicked = onShareClicked;
        this.onFavClicked = onFavClicked;
    }

}
