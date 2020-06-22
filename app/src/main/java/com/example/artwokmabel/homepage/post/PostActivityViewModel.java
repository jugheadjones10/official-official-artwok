package com.example.artwokmabel.homepage.post;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.models.Comment;
import com.example.artwokmabel.models.Report;
import com.example.artwokmabel.models.User;
import com.example.artwokmabel.repos.FirestoreRepo;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class PostActivityViewModel extends ViewModel {

    private LiveData<User> userObservable;
    private LiveData<List<Comment>> commentsObservable;
    private FirebaseAuth mAuth;

    public PostActivityViewModel() {
        mAuth = FirebaseAuth.getInstance();
    }

    public LiveData<User> getUserObservable(String uid) {
        userObservable = FirestoreRepo.getInstance().getUser(uid);
        return userObservable;
    }

    public void addUserPostFavs(String postId){
        FirestoreRepo.getInstance().addUserPostFavs(postId, mAuth.getCurrentUser().getUid());
    }

    public void removeUserPostFavs(String postId){
        FirestoreRepo.getInstance().removeUserPostFavs(postId,  mAuth.getCurrentUser().getUid());
    }

    public void deleteUserPost(String postId){
        FirestoreRepo.getInstance().deleteUserPost(postId);
    }

    public void sendPostReport(String report, String postId){
        FirestoreRepo.getInstance().sendReport(report, postId, Report.POST);
    }

    public LiveData<List<Comment>> getCommentsObservable(String postId, String posterId){
        commentsObservable = FirestoreRepo.getInstance().getPostComments(postId, posterId);
        return commentsObservable;
    }

    public void addComment(String commentText, String commenterId, String posterUserId, String postId){
        FirestoreRepo.getInstance().addPostComment(commentText, commenterId, posterUserId, postId);
    }
}
