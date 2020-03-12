package com.example.artwokmabel.homepage.post;

import android.widget.ImageView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.models.Listing;
import com.example.artwokmabel.repos.FirestoreRepo;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class CommentsViewModel extends ViewModel {

    public CommentsViewModel() {
    }

    public void deleteComment(String commentId, String postId, String postPosterId){
        FirestoreRepo.getInstance().deleteUserComment(commentId, postId, postPosterId);
    }
}
