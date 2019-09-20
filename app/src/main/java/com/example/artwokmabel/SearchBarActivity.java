package com.example.artwokmabel;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

public class SearchBarActivity extends AppCompatActivity
        implements SearchView.OnQueryTextListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuItem searchItem = menu.findItem(R.id.svsearchbar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(
                new ComponentName(this, SearchBarActivity.class)));
        searchView.setIconifiedByDefault(false);

        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(this, "Searching by: "+ query, Toast.LENGTH_SHORT).show();

        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            String uri = intent.getDataString();
            Toast.makeText(this, "Suggestion: "+ uri, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // User pressed the search button
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // User changed the text
        return false;
    }
}