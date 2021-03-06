package com.example.artwokmabel.Repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.artwokmabel.chat.models.Comment;
import com.example.artwokmabel.chat.models.UserUserModel;
import com.example.artwokmabel.homepage.models.Category;
import com.example.artwokmabel.homepage.models.Listing;
import com.example.artwokmabel.homepage.models.MainPost;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirestoreRepo {
    private final String TAG = "FirestoreRepo";
    private FirebaseFirestore db;
    private static FirestoreRepo firestoreRepo;

    public FirestoreRepo() {
        db = FirebaseFirestore.getInstance();
    }

    public synchronized static FirestoreRepo getInstance() {
        //TODO No need to implement this singleton in Part #2 since Dagger will handle it ...
        if (firestoreRepo == null) {
            firestoreRepo = new FirestoreRepo();
        }
        return firestoreRepo;
    }



    public LiveData<List<Listing>> getUserListings(String uid){
        final MutableLiveData<List<Listing>> data = new MutableLiveData<>();
        List<Listing> tempData = new ArrayList<>();

        db.collection("Users")
            .document(uid)
            .collection("Listings")
            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("TAG", "Listen failed.", e);
                            return;
                        }

                        tempData.clear();
                        for(DocumentSnapshot single: queryDocumentSnapshots){
                            Listing listdata = changeDocToListingModel(single);
                            tempData.add(listdata);
                        }

                        Collections.sort(tempData, new SortListings());
                        data.setValue(tempData);
                    }
                });

        return data;
    }

    public LiveData<List<Listing>> getSingleCategoryListings(String category){
        final MutableLiveData<List<Listing>> data = new MutableLiveData<>();
        List<Listing> tempData = new ArrayList<>();

        db.collectionGroup("Listings")
            .whereArrayContains("categories", category)
            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w("TAG", "Listen failed.", e);
                        return;
                    }

                    tempData.clear();
                    for(DocumentSnapshot single: queryDocumentSnapshots){
                        Listing listdata = changeDocToListingModel(single);
                        tempData.add(listdata);
                    }

                    Collections.sort(tempData, new SortListings());
                    data.setValue(tempData);
                }
            });

//                .get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        tempData.clear();
//                        for(DocumentSnapshot single: queryDocumentSnapshots){
//                            Listing listdata = changeDocToListingModel(single);
//                            tempData.add(listdata);
//                            Log.d("mycokk", listdata.getName());
//                        }
//
//                        Collections.sort(tempData, new SortListings());
//                        data.setValue(tempData);
//                    }
//                });

        return data;
    }

    public LiveData<List<Category>> getCategoriesList(String userId){
        final MutableLiveData<List<Category>> data = new MutableLiveData<>();
        List<Category> tempData = new ArrayList<>();
        db.collection("Categories")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        db.collection("Users")
                                .document(userId)
                                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                                        if (e != null) {
                                            Log.w(TAG, "Listen failed.", e);
                                            return;
                                        }
                                        if (snapshot != null && snapshot.exists()) {
                                            ArrayList<String> tabs = (ArrayList<String>) snapshot.get("tab_categories");

                                            tempData.clear();
                                            for(DocumentSnapshot snap : queryDocumentSnapshots){
                                                Category newCategory = new Category(
                                                        snap.getString("name"),
                                                        snap.getString("photo")
                                                );
                                                tempData.add(newCategory);
                                                if(tabs.contains(newCategory.getName())){
                                                    newCategory.setChecked(true);
                                                }
                                            }
                                            data.setValue(tempData);
                                        } else {
                                            Log.d(TAG, "Current data: null");
                                            data.setValue(null);
                                            //Try and find a better way to handle errors
                                        }
                                    }
                                });
                    }
                });
        return data;
    }

    public void setUserTabsList(List<Category> categories, String userId){
        ArrayList<String> tabsList = new ArrayList<>();
        for(Category category : categories){
            tabsList.add(category.getName());
        }
        db.collection("Users")
                .document(userId)
                .update("tab_categories", tabsList)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }

    public String setNewChatroom(ArrayList<String> participants){
        //Adding chatroom document to chatrooms collection
        DocumentReference newChatRoom = db.collection("Chatrooms").document();

        Log.d("know", participants.toString());


        Map<String, Object> data = new HashMap<>();
        data.put("participants", participants);
        newChatRoom.set(data);

        //Adds new chatroom ID to array of chatrooms Ids in each participating user's document
        db.collection("Users")
                .document(participants.get(0))
                .update("chatrooms", FieldValue.arrayUnion(newChatRoom.getId()));

        db.collection("Users")
                .document(participants.get(1))
                .update("chatrooms", FieldValue.arrayUnion(newChatRoom.getId()));

        return newChatRoom.getId();
    }

    public LiveData<List<Comment>> getChatRoomMessages(String chatRoomId){
        final MutableLiveData<List<Comment>> data = new MutableLiveData<>();
        List<Comment> tempData = new ArrayList<>();

        db.collection("Chatrooms")
                .document(chatRoomId)
                .collection("Comments")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("TAG", "Listen failed.", e);
                            return;
                        }

                        tempData.clear();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            Comment comment = changeDocToCommentModel(doc);
                            Log.d("BUCK", comment.getMessage());
                            tempData.add(comment);
                        }

                        Collections.sort(tempData, new SortMessages());
                        data.setValue(tempData);
                    }
                });
        return data;
    }

    public void addNewMessage(Comment comment, String chatRoomId){

        Log.d("HUCK", "FIRESTORE REPO : " +  comment.getMessage());

        db.collection("Chatrooms")
            .document(chatRoomId)
            .collection("Comments")
            .add(comment)
            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error adding document", e);
                }
            });

    }

    public LiveData<ArrayList<String>> getUserChatrooms(String userId) {
        final MutableLiveData<ArrayList<String>> data = new MutableLiveData<>();
        db.collection("Users")
                .document(userId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }
                        if (snapshot != null && snapshot.exists()) {
                            Log.d(TAG, "The id" + userId);

                            Log.d(TAG, "Current data: " + snapshot.getData());
                            ArrayList<String> chatrooms = (ArrayList<String>) snapshot.get("chatrooms");
                            Log.d(TAG, "My tabs data: " + chatrooms.toString());

                            data.setValue(chatrooms);
                        } else {
                            Log.d(TAG, "Current data: null");
                            data.setValue(null);
                            //Try and find a better way to handle errors
                        }
                    }
                });
        return data;
    }

    //Todo: replace below with getting UserUserModel
    public LiveData<ArrayList<String>> getUserTabsList(String userId) {
        final MutableLiveData<ArrayList<String>> data = new MutableLiveData<>();
        db.collection("Users")
                .document(userId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }
                        if (snapshot != null && snapshot.exists()) {
                            Log.d(TAG, "The id" + userId);

                            Log.d(TAG, "Current data: " + snapshot.getData());
                            ArrayList<String> tabs = (ArrayList<String>) snapshot.get("tab_categories");
                            Log.d(TAG, "My tabs data: " + tabs.toString());

                            data.setValue(tabs);
                        } else {
                            Log.d(TAG, "Current data: null");
                            data.setValue(null);
                            //Try and find a better way to handle errors
                        }
                    }
                });
        return data;
    }

    public LiveData<List<MainPost>> getUserPosts(String userId){
        final MutableLiveData<List<MainPost>> data = new MutableLiveData<>();
        List<MainPost> tempData = new ArrayList<>();

        FirestoreRepo.getInstance().getUserPostsQuery(userId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("TAG", "Listen failed.", e);
                            return;
                        }

                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            MainPost listdata = changeDocToMainPostModel(doc);
                            tempData.add(listdata);
                        }

                        //Below sorts posts according to date posted
                        Collections.sort((ArrayList)tempData, new SortMain());
                        data.setValue(tempData);

                    }
                });
        return  data;
    }

    public LiveData<List<MainPost>> getFeedPosts(String userId){
        DocumentReference userRef = FirestoreRepo.getInstance().getUserRef(userId);

        final MutableLiveData<List<MainPost>> data = new MutableLiveData<>();
        List<MainPost> tempData = new ArrayList<>();

        userRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                ArrayList<String> following = (ArrayList<String>) snapshot.get("following");
                following.add(userId);
                ///////////////////////////////////////////////////////////////
                tempData.clear();
                for(int i = 0; i < following.size(); i++){
                    String oneFollowing = following.get(i);
                    FirestoreRepo.getInstance().getUserPostsQuery(oneFollowing)
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {

                                if (e != null) {
                                    Log.w("TAG", "Listen failed.", e);
                                    return;
                                }

                                for (QueryDocumentSnapshot doc : value) {
                                    MainPost listdata = changeDocToMainPostModel(doc);
                                    tempData.add(listdata);
                                }

                                //Below sorts posts according to date posted
                                Collections.sort((ArrayList)tempData, new SortMain());
                                data.setValue(tempData);

                            }
                        });
                }
            }
        });

        return data;
    }

    public LiveData<List<UserUserModel>> getFollowings(String userId){
        DocumentReference userRef = FirestoreRepo.getInstance().getUserRef(userId);

        final MutableLiveData<List<UserUserModel>> data = new MutableLiveData<>();
        List<UserUserModel> tempData = new ArrayList<>();

        userRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                ArrayList<String> following = (ArrayList<String>) snapshot.get("following");

                //Todo: find method of using diff util-like logic so that i don't need to rebuild the tempdata everytime the data changes
                tempData.clear();
                for(int i = 0; i < following.size(); i++){
                    String oneFollowing = following.get(i);

                    DocumentReference followingUserRef = FirestoreRepo.getInstance().getUserRef(oneFollowing);
                    followingUserRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                            UserUserModel user = changeDocToUserUserModel(snapshot);
                            Log.d("FOPE", user.getUserName());
                            tempData.add(user);
                        }
                    });
                }
                data.setValue(tempData);
            }
        });

        return data;
    }


    //Database call "HEADS"
    public DocumentReference getUserRef(String userId){
        DocumentReference followingsTask = db.collection("Users")
                .document(userId);
        return followingsTask;
    }

    public Query getUserPostsQuery(String userId){
        Query userPostsTask = db.collection("Users")
                .document(userId)
                .collection("Posts");
        return userPostsTask;
    }


    //Utils
    class SortMain implements Comparator<MainPost> {
        public int compare(MainPost a, MainPost b){
            return (int)b.getNanopast() - (int)a.getNanopast();
        }
    }

    class SortListings implements Comparator<Listing> {
        public int compare(Listing a, Listing b){
            return (int)b.getNanopast() - (int)a.getNanopast();
        }
    }

    class SortMessages implements Comparator<Comment> {
        public int compare(Comment a, Comment b){
            return Math.toIntExact(b.getTimestamp() - a.getTimestamp());
        }
    }

    private MainPost changeDocToMainPostModel(QueryDocumentSnapshot doc){
        MainPost post = new MainPost(
                doc.getString("user_id"),
                doc.getString("desc"),
                doc.getString("hashtags"),
                doc.getId(),
                doc.getString("username"),
                (ArrayList<String>) doc.get("photos"),
                doc.getString("timestamp"),
                (long)doc.get("nanopast")
        );
        return post;
    }

    private UserUserModel changeDocToUserUserModel(DocumentSnapshot doc){
        return new UserUserModel(
                doc.getString("username"),
                doc.getString("profile_url"),
                doc.getString("uid"),
                (ArrayList<String>) doc.get("chatrooms")
        );
    }

    private Comment changeDocToCommentModel(QueryDocumentSnapshot doc){
        return new Comment(
                doc.getString("senderId"),
                doc.getString("message"),
                doc.getLong("timestamp"),
                doc.getString("otherUserPic")
        );
    }

    private Listing changeDocToListingModel(DocumentSnapshot doc){
        return new Listing(
                doc.getString("userid"),
                doc.getString("return_exchange"),
                (long)doc.get("price"),
                (ArrayList<String>) doc.get("photos"),
                doc.getString("name"),
                doc.getString("hashtags"),
                doc.getString("desc"),
                doc.getString("delivery"),
                "temp",
                doc.getId(),
                (long)doc.get("nanopast"),
                (ArrayList<String>) doc.get("categories")
        );
    }
}
