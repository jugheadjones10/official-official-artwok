package com.example.artwokmabel.homepage.post;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.artwokmabel.HomeGraphDirections;
import com.example.artwokmabel.ProfileGraphDirections;
import com.example.artwokmabel.R;
import com.example.artwokmabel.Utils.TransactFragment;
import com.example.artwokmabel.databinding.ActivityPost2Binding;
import com.example.artwokmabel.databinding.FragmentListingBinding;
import com.example.artwokmabel.databinding.FragmentPostBinding;
import com.example.artwokmabel.homepage.listing.ListingActivityViewModel;
import com.example.artwokmabel.homepage.listing.ListingFragmentArgs;
import com.example.artwokmabel.homepage.listing.ListingPagerAdapter;
import com.example.artwokmabel.models.Comment;
import com.example.artwokmabel.models.Listing;
import com.example.artwokmabel.models.MainPost;
import com.example.artwokmabel.models.User;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.List;

public class PostFragment extends Fragment {

    private FragmentPostBinding binding;
    private PostActivityViewModel viewModel;
    private MainPost post;
    private CommentsAdapter commentsAdapter;
    private NavController navController;

    private FirebaseAuth mAuth;

    private String postId;
    private String posterUserId;

    private static PostFragment instance = null;

    public static PostFragment getInstance() {
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        instance = this;
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_post, container, false);
        binding.setFragmentPost(this);
        mAuth = FirebaseAuth.getInstance();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.indivToolbar);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_container);

        getIncomingIntent();
        setHasOptionsMenu(true);

        if(mAuth.getCurrentUser().getUid().equals(post.getUser_id())){
            binding.indivToolbar.inflateMenu(R.menu.indiv_listing_menu_mine);
        }else{
            binding.indivToolbar.inflateMenu(R.menu.indiv_listing_menu_yours);
        }
        binding.indivToolbar.inflateMenu(R.menu.indiv_listing_menu_mine);

        commentsAdapter = new CommentsAdapter(requireActivity(), postId, posterUserId, navController);
        binding.commentsRecyclerView.setAdapter(commentsAdapter);

        viewModel = ViewModelProviders.of(this).get(PostActivityViewModel.class);

        viewModel.getUserObservable(mAuth.getCurrentUser().getUid()).observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                if (user != null) {

                    if (posterUserId.equals(mAuth.getCurrentUser().getUid())) {
                        binding.favorite.setVisibility(View.GONE);
                    } else {
                        ArrayList<String> favs = user.getFav_posts();
                        Log.d("favfav", favs.toString());

                        if (favs != null && favs.contains(postId)) {
                            binding.favorite.setImageResource(R.drawable.like);
                        } else {
                            binding.favorite.setImageResource(R.drawable.favourite_post);
                        }

                        binding.favorite.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (favs != null && favs.contains(postId)) {
                                    viewModel.removeUserPostFavs(postId);
                                } else {
                                    viewModel.addUserPostFavs(postId);
                                }
                            }
                        });

                    }
                }
            }
        });

        viewModel.getUserObservable(posterUserId).observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                if (user != null) {
                    binding.setUser(user);
                    Picasso.get().load(user.getProfile_url()).into(binding.profilePicture);
                }
            }
        });

        viewModel.getCommentsObservable(postId, posterUserId).observe(getViewLifecycleOwner(), new Observer<List<Comment>>() {
            @Override
            public void onChanged(@Nullable List<Comment> commentsList) {
                if (commentsList != null) {
                    commentsAdapter.setCommentsList(commentsList);
                }
            }
        });

        binding.addComment.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.commentEditText.getText().toString().equals("")) {

                    viewModel.addComment(binding.commentEditText.getText().toString(), mAuth.getCurrentUser().getUid(), posterUserId, postId);

                    binding.commentEditText.setText("");
                    closeKeyboard();
                } else {
                    Toast.makeText(getContext(), "you can't post a blank comment", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onProfileClicked(User user){
        int currentGraph = navController.getGraph().getId();
        if(currentGraph == R.id.home_graph){
            HomeGraphDirections.ActionGlobalProfileFragment action =
                    HomeGraphDirections.actionGlobalProfileFragment(post.getUser_id());
            navController.navigate(action);
        }else if(currentGraph == R.id.profile_graph){
            ProfileGraphDirections.ActionProfileGraphSelf action =
                    ProfileGraphDirections.actionProfileGraphSelf(post.getUser_id());
            navController.navigate(action);
        }
    }

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

    private void closeKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void getIncomingIntent() {

        post = PostFragmentArgs.fromBundle(getArguments()).getPost();
        String encoded = Base64.encodeToString(post.getDesc().getBytes(), Base64.DEFAULT);
        binding.postWebView.loadData(encoded, "text/html", "base64");

        postId = post.getPostId();
        posterUserId = post.getUser_id();

        ArrayList<String> images = post.getPhotos();
        ImageListener imageListener = new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                Picasso.get()
                        .load(images.get(position))
                        .placeholder(R.drawable.user)
                        .error(R.drawable.rick_and_morty)
                        .into(imageView);
            }
        };
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(mAuth.getCurrentUser().getUid().equals(post.getUser_id())){
            inflater.inflate(R.menu.indiv_listing_menu_mine, menu);
        }else{
            inflater.inflate(R.menu.indiv_listing_menu_yours, menu);
        }
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.listing_delete:
                new MaterialAlertDialogBuilder(getContext())
                        .setTitle("Delete Post?")
                        .setMessage("This action cannot be reversed!")
                        .setNeutralButton("Cancel", null)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                viewModel.deleteUserPost(post.getPostId());
                                navController.navigateUp();
                            }
                        })
                        .show();
                return true;
            case R.id.listing_edit:
                return true;
            case R.id.listing_report:
                final EditText taskEditText = new EditText(getContext());
                new MaterialAlertDialogBuilder(getContext())
                        .setTitle("Report this post")
                        .setMessage("Describe why you feel this post inappropriate")
                        .setView(taskEditText)
                        .setPositiveButton("Report", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String report = String.valueOf(taskEditText.getText());
                                viewModel.sendPostReport(report, post.getPostId());
                            }
                        })
                        .setNeutralButton("Cancel", null)
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}