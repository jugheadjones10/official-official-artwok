package com.example.artwokmabel.homepage.post;

import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.repos.FirestoreRepo;

public class CommentsViewModel extends ViewModel {

    public CommentsViewModel() {
    }

    public void deleteComment(String commentId, String postId, String postPosterId){
        FirestoreRepo.getInstance().deleteUserComment(commentId, postId, postPosterId);
    }
}
