package com.example.artwokmabel.repos;

import android.app.Activity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.algolia.search.saas.Client;
import com.algolia.search.saas.Index;
import com.example.artwokmabel.HomePageActivity;
import com.example.artwokmabel.R;
import com.example.artwokmabel.chat.personalchat.ChatActivity;
import com.example.artwokmabel.login.CreateAccountEmailActivity;
import com.example.artwokmabel.login.CreateAccountPasswordActivity;
import com.example.artwokmabel.login.CreateAccountUsernameActivity;
import com.example.artwokmabel.login.DuplicateUsernameChecker;
import com.example.artwokmabel.login.LoginActivity;
import com.example.artwokmabel.chat.models.UserUserModel;
import com.example.artwokmabel.login.LoginViewModel;
import com.example.artwokmabel.login.RegistrationViewModel;
import com.example.artwokmabel.login.callbacks.CheckDuplicateCallback;
import com.example.artwokmabel.models.Comment;
import com.example.artwokmabel.models.ImageMessage;
import com.example.artwokmabel.models.Message;
import com.example.artwokmabel.models.NormalChat;
import com.example.artwokmabel.models.Notification;
import com.example.artwokmabel.models.OfferMessage;
import com.example.artwokmabel.models.OrderChat;
import com.example.artwokmabel.models.Request;
import com.example.artwokmabel.homepage.request.upload.UploadRequestActivity;
import com.example.artwokmabel.models.Category;
import com.example.artwokmabel.models.Listing;
import com.example.artwokmabel.models.MainPost;
import com.example.artwokmabel.models.Review;
import com.example.artwokmabel.models.User;
import com.example.artwokmabel.profile.uploadlisting.UploadListingAcitvity;
import com.example.artwokmabel.profile.uploadpost.UploadPostActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

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


    private void PushUserToAlgolia(String username, String userid){
        Client client = new Client("CTIOUIUY3T", "7f3f4f1b7f3eab10acf7e980b2023a23");
        Index algoliaIndex = client.getIndex("Users");

        JSONObject newData = null;
        try {
            newData = new JSONObject()
                    .put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        algoliaIndex.saveObjectAsync(newData, userid, null);
    }

    public void addTokenToFirestore(String token){

        //This here might be a problem - check if the token field is created or not
        db.collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .update("token", token)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("notives", "Notifnotif went right");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("notives", "Notifnotif went wrong", e);
                    }
                });

    }


    public LiveData<List<Review>> getListingReviews(Listing listing){
        final MutableLiveData<List<Review>> data = new MutableLiveData<>();
        List<Review> tempData = new ArrayList<>();

        db.collection("Users")
                .document(listing.getUserid())
                .collection("Listings")
                .document(listing.getPostid())
                .collection("Reviews")
                .orderBy("nanopast", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        tempData.clear();
                        for(DocumentSnapshot snapshot : queryDocumentSnapshots){
                            tempData.add(snapshot.toObject(Review.class));
                        }
                        data.setValue(tempData);
                    }
                });
        return  data;
    }

    public void uploadNewReview(float rating, String listingId, String reviewerId, String publicReview, String privateReview){
        db.collectionGroup("Listings")
                .whereEqualTo("postid", listingId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(DocumentSnapshot snapshot : queryDocumentSnapshots){

                            Listing listing = snapshot.toObject(Listing.class);
                            DocumentReference newReviewRef = db.collection("Users")
                                    .document(listing.getUserid())
                                    .collection("Listings")
                                    .document(listingId)
                                    .collection("Reviews")
                                    .document();


                            db.collection("Users")
                                    .document(reviewerId)
                                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                                            if (e != null) {
                                                Log.w("TAG", "Listen failed.", e);
                                                return;
                                            }

                                            User user = snapshot.toObject(User.class);

                                            Review newReview =  new Review(
                                                publicReview,
                                                user.getUsername(),
                                                user.getUid(),
                                                user.getProfile_url(),
                                                listingId,
                                                privateReview,
                                                rating,
                                                newReviewRef.getId(),
                                                System.currentTimeMillis()
                                            );

                                            newReviewRef.set(newReview);
                                        }
                                    });


                        }
                    }
                });
    }

    public void uploadNewPost(String postText, String userId, Activity activity){
        DocumentReference newPostRef = db.collection("Users").document(userId).collection("Posts").document();

        ArrayList<String> photos = new ArrayList<>();
        photos.add("https://firebasestorage.googleapis.com/v0/b/artwok-database.appspot.com/o/Rick%20and%20Morty%20white.png?alt=media&token=dd2a8310-6a36-43e4-8372-ff6f2d465f95");
        MainPost newPost =  new MainPost(
            userId,
            postText,
            "placeholder hashtags",
            newPostRef.getId(),
            "placeholder username",
            photos,
            "bla",
            System.currentTimeMillis()
        );

        newPostRef.set(newPost)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(activity, "Successfully uploaded ic_dm.", Toast.LENGTH_LONG).show();
                    UploadPostActivity.getInstance().onPostUploaded();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(activity, "Failed to upload ic_dm. awd", Toast.LENGTH_LONG)
                                .show());
    }

    public void uploadNewListing(String postTitle, String postDesc, ArrayList<String> categories, Long price, String delivery, String refund, String currentUserId, ArrayList<String> postImageUris, Activity activity){
        postTitle = postTitle.substring(0, 1).toUpperCase() + postTitle.substring(1);

        DocumentReference newListingRef = db.collection("Users").document(currentUserId).collection("Listings").document();
        Listing newListing = new Listing(
                currentUserId,
                refund,
                price,
                postImageUris,
                postTitle,
                "placeholderhashtags",
                postDesc,
                delivery,
                "temporary username",
                newListingRef.getId(),
                System.currentTimeMillis(),
                categories);

        newListingRef.set(newListing)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(activity, "Successfully uploaded ic_dm.", Toast.LENGTH_LONG).show();
                    UploadListingAcitvity.getInstance().onUploaded();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(activity, "Failed to upload ic_dm. awd", Toast.LENGTH_LONG)
                                .show());

        FirebaseDatabase.getInstance().getReference()
                .child("Listings")
                .child(newListingRef.getId())
                .setValue(newListing);
    }

    public void uploadNewRequest(String postTitle, String postDesc, String category, Long budget, String currentUserId, ArrayList<String> postImageUris, Activity activity){

        DocumentReference newRequestRef = db.collection("Users").document(currentUserId).collection("Requests").document();
        Request newRequest = new Request(
                currentUserId,
                budget,
                postImageUris,
                postTitle,
                "placeholderhashtags",
                postDesc,
                "temporary username",
                newRequestRef.getId(),
                System.currentTimeMillis(),
                category);

        newRequestRef.set(newRequest)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(activity, "Successfully uploaded ic_dm.", Toast.LENGTH_LONG).show();
                    UploadRequestActivity.getInstance().onUploaded();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(activity, "Failed to upload ic_dm. awd", Toast.LENGTH_LONG)
                                .show());

        db.collection("Users")
            .document(currentUserId)
            .update("number_of_posts", FieldValue.increment(1));
    }

    public void logIntoAccount(String email, String password, LoginViewModel.LoginCallback callback){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            callback.loginCallback(true);
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            callback.loginCallback(false);
                        }
                    }
                });
    }

    public void createAccount(String email, String username, String password, RegistrationViewModel.CreateAccountCallback callback){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //FirebaseUser user = mAuth.getCurrentUser();
                            String uid = task.getResult().getUser().getUid();

                            //This extra layer outside is to
                            FirebaseInstanceId.getInstance().getInstanceId()
                                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                            if (!task.isSuccessful()) {
                                                Log.w("notives", "getInstanceId failed", task.getException());
                                                return;
                                            }

                                            // Get new Instance ID token
                                            String token = task.getResult().getToken();

                                            // Log and toast
                                            Log.d("notives", token);

                                            User userObject = new User(
                                                    username.toLowerCase(),
                                                    uid,
                                                    "https://firebasestorage.googleapis.com/v0/b/artwok-database.appspot.com/o/Default_images%2Faccount.png?alt=media&token=8c34c02a-4c2c-4708-a802-73af4978b7d0",
                                                    new ArrayList<String>(),
                                                    new ArrayList<String>(),
                                                    new ArrayList<String>(),
                                                    new ArrayList<String>(),
                                                    new ArrayList<String>(),
                                                    new ArrayList<String>(),
                                                    new ArrayList<String>(),
                                                    email,
                                                    "",
                                                    token
                                            );

                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("Users")
                                                    .child(uid)
                                                    .setValue(userObject);

                                            db.collection("Users")
                                                    .document(uid)
                                                    .set(userObject)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            callback.createAccountCallback(true);
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            callback.createAccountCallback(false);
                                                        }
                                                    });
                                        }
                                    });


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            callback.createAccountCallback(false);
                        }
                    }
                });

    }

    public void isEmailDuplicate(String email, CheckDuplicateCallback callback){
    db.collection("Users")
            .whereEqualTo("email", email)
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult().size() != 0) {
                            callback.collectDetailCallback(true);
                        }else{
                            callback.collectDetailCallback(false);
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });
    }

    public void isUsernameDuplicate(String username, CheckDuplicateCallback callback){
        db.collection("Users")
                .whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().size() != 0) {
                                callback.collectDetailCallback(true);
                            }else{
                                callback.collectDetailCallback(false);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public LiveData<Integer> getUserNumPosts(String userId){
        final MutableLiveData<Integer> data = new MutableLiveData<>();

        db.collection("Users")
                .document(userId)
                .collection("Posts")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("TAG", "Listen failed.", e);
                            return;
                        }
                        int count = 0;
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            count++;
                        }
                        data.setValue(count);
                    }
                });
        return data;
    }

    public LiveData<Integer> getUserNumListings(String userId){
        final MutableLiveData<Integer> data = new MutableLiveData<>();

        db.collection("Users")
                .document(userId)
                .collection("Listings")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("TAG", "Listen failed.", e);
                            return;
                        }
                        int count = 0;
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            count++;
                        }
                        data.setValue(count);
                    }
                });
        return data;
    }

    public void addUserRequestFavs(String requestId, String userId){
        db.collection("Users")
                .document(userId)
                .update("fav_requests", FieldValue.arrayUnion(requestId));
    }

    public void removeUserRequestFavs(String requestId, String userId){
        db.collection("Users")
                .document(userId)
                .update("fav_requests", FieldValue.arrayRemove(requestId));
    }

    public void addUserListingFavs(String listingId, String userId){
        db.collection("Users")
                .document(userId)
                .update("fav_listings", FieldValue.arrayUnion(listingId));
    }

    public void removeUserListingFavs(String listingId, String userId){
        db.collection("Users")
                .document(userId)
                .update("fav_listings", FieldValue.arrayRemove(listingId));
    }

    public void addUserPostFavs(String postId, String userId){
        db.collection("Users")
                .document(userId)
                .update("fav_posts", FieldValue.arrayUnion(postId));
    }

    public void removeUserPostFavs(String postId, String userId){
        db.collection("Users")
                .document(userId)
                .update("fav_posts", FieldValue.arrayRemove(postId));
    }

    public void addUserFollowing(String myId, String otherUserId){
        db.collection("Users")
                .document(myId)
                .update("following", FieldValue.arrayUnion(otherUserId));

        db.collection("Users")
                .document(otherUserId)
                .update("followers", FieldValue.arrayUnion(myId));
    }

    public void removeUserFollowing(String myId, String otherUserId){
        db.collection("Users")
                .document(myId)
                .update("following", FieldValue.arrayRemove(otherUserId));

        db.collection("Users")
                .document(otherUserId)
                .update("followers", FieldValue.arrayRemove(myId));
    }

    public LiveData<List<Listing>> getSearchedListings(String query) {
        final MutableLiveData<List<Listing>> data = new MutableLiveData<>();
        List<Listing> tempData = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference()
                .child("Listings")
                .orderByChild("name")
                .startAt(query.toUpperCase())
                .endAt(query.toUpperCase() + "\uf8ff")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(query.contains(" ")){
                            

                        }else{
                            tempData.clear();
                            for(DataSnapshot listing : dataSnapshot.getChildren()){
                                tempData.add(listing.getValue(Listing.class));
                            }
                            data.setValue(tempData);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

//                .addValueEventListener(new ValueEventListener() {
//
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        tempData.clear();
//                        for(DataSnapshot listing : dataSnapshot.getChildren()){
//                            tempData.add(listing.getValue(Listing.class));
//                        }
//                        data.setValue(tempData);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });

        return data;
    }

    public LiveData<List<User>> getSearchedUsers(String query) {
        final MutableLiveData<List<User>> data = new MutableLiveData<>();
        List<User> tempData = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .orderByChild("username")
                .startAt(query.toLowerCase())
                .endAt(query.toLowerCase() + "\uf8ff")
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        tempData.clear();
                        for(DataSnapshot user : dataSnapshot.getChildren()){
                            tempData.add(user.getValue(User.class));
                        }
                        data.setValue(tempData);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        return data;
    }

    public LiveData<List<User>> getUserFollowings(String userId){
        final MutableLiveData<List<User>> data = new MutableLiveData<>();
        List<User> tempData = new ArrayList<>();

        Log.d("thestuffreturnedREQUEST", "user id from b4 db call" + userId);

        db.collection("Users")
                .document(userId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        if (snapshot != null && snapshot.exists()) {
                            Log.d(TAG, " data: " + snapshot.getData());

                            ArrayList<String> followingIds = (ArrayList<String>) snapshot.getData().get("following");
                            //Get array list of user following
                            Log.d("thestuffreturnedREQUEST", "my user id  " + userId);
                            Log.d("thestuffreturnedREQUEST", "my listing_fav_ids  " + followingIds.toString());

                            tempData.clear();
                            List<Task> tasks = new ArrayList<>();
                            for(int i = 0; i < followingIds.size(); i++) {
                                Log.d("thestuffreturnedREQUEST", "I'm inside the for loop now  " + followingIds.get(0));

                                Task task =  db.collection("Users")
                                        .whereEqualTo("uid", followingIds.get(i))
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                Log.d("thestuffreturnedREQUEST", "I'm below the query snapshot  " + Integer.toString(queryDocumentSnapshots.size()));

                                                if(!queryDocumentSnapshots.isEmpty()){
                                                    for(DocumentSnapshot user: queryDocumentSnapshots){
                                                        User userObject = changeDocToUserModel(user);
                                                        Log.d("thestuffreturnedREQUEST", "middleman within the for loop" + userObject.getUid());
                                                        tempData.add(userObject);
                                                    }
                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d("thestuffreturnedREQUEST", "THE DB CALL FAILED");
                                            }
                                        });
                                tasks.add(task);
                            }

                            Tasks.whenAll(tasks.toArray(new Task[0]))
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("thestuffreturnedREQUEST", "final resulting temp data" + tempData.toString());
                                            data.setValue(tempData);
                                        }
                                    });
                        } else {
                            Log.d(TAG, " data: null");
                        }
                    }
                });

        return data;
    }

    public LiveData<List<User>> getUserFollowers(String userId){
        final MutableLiveData<List<User>> data = new MutableLiveData<>();
        List<User> tempData = new ArrayList<>();

        Log.d("thestuffreturnedREQUEST", "user id from b4 db call" + userId);

        db.collection("Users")
                .whereArrayContains("following", userId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if(!queryDocumentSnapshots.isEmpty()){

                            ArrayList<String> followersIds = new ArrayList<>();
                            for(DocumentSnapshot user: queryDocumentSnapshots){
                                String userId = user.getString("uid");
                                followersIds.add(userId);
                            }

                            tempData.clear();
                            List<Task> tasks = new ArrayList<>();
                            for(int i = 0; i < followersIds.size(); i++) {
                                Log.d("thestuffreturnedREQUEST", "I'm inside the for loop now  " + followersIds.get(0));

                                Task task =  db.collection("Users")
                                        .whereEqualTo("uid", followersIds.get(i))
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                Log.d("thestuffreturnedREQUEST", "I'm below the query snapshot  " + Integer.toString(queryDocumentSnapshots.size()));

                                                if(!queryDocumentSnapshots.isEmpty()){
                                                    for(DocumentSnapshot user: queryDocumentSnapshots){
                                                        User userObject = changeDocToUserModel(user);
                                                        Log.d("thestuffreturnedREQUEST", "middleman within the for loop" + userObject.getUid());
                                                        tempData.add(userObject);
                                                    }
                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d("thestuffreturnedREQUEST", "THE DB CALL FAILED");
                                            }
                                        });
                                tasks.add(task);
                            }

                            Tasks.whenAll(tasks.toArray(new Task[0]))
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("thestuffreturnedREQUEST", "final resulting temp data" + tempData.toString());
                                            data.setValue(tempData);
                                        }
                                    });

                        }
                    }
                });
        return data;
    }

    public LiveData<List<String>> getUserFavRequests(String userId){
        final MutableLiveData<List<String>> data = new MutableLiveData<>();

        db.collection("Users")
                .document(userId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("TAG", "Listen failed.", e);
                            return;
                        }
                        if(snapshot.exists()){
                            ArrayList<String> favs = (ArrayList<String>) snapshot.get("fav_requests");
                            data.setValue(favs);
                        }
                    }
                });
        return data;
    }


    public LiveData<List<String>> getUserFavPosts(String userId){
        final MutableLiveData<List<String>> data = new MutableLiveData<>();

        db.collection("Users")
                .document(userId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("TAG", "Listen failed.", e);
                            return;
                        }
                        if(snapshot.exists()){
                            ArrayList<String> favs = (ArrayList<String>) snapshot.get("fav_posts");
                            data.setValue(favs);
                        }
                    }
                });
        return data;
    }

    public LiveData<List<Request>> getUserFavRequestsObjects(String userId){
        final MutableLiveData<List<Request>> data = new MutableLiveData<>();
        List<Request> tempData = new ArrayList<>();

        Log.d("thestuffreturnedREQUEST", "user id from b4 db call" + userId);

        db.collection("Users")
                .document(userId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        if (snapshot != null && snapshot.exists()) {
                            Log.d(TAG, " data: " + snapshot.getData());

                            ArrayList<String> favRequestsIds = (ArrayList<String>) snapshot.getData().get("fav_requests");
                            //Get array list of fav posts
                            Log.d("thestuffreturnedREQUEST", "my user id  " + userId);
                            Log.d("thestuffreturnedREQUEST", "my listing_fav_ids  " + favRequestsIds.toString());

                            tempData.clear();
                            List<Task> tasks = new ArrayList<>();
                            for(int i = 0; i < favRequestsIds.size(); i++) {
                                Log.d("thestuffreturnedREQUEST", "I'm inside the for loop now  " + favRequestsIds.get(0));

                                Task task =  db.collectionGroup("Requests")
                                        .whereEqualTo("postid", favRequestsIds.get(i))
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                Log.d("thestuffreturnedREQUEST", "I'm below the query snapshot  " + Integer.toString(queryDocumentSnapshots.size()));

                                                if(!queryDocumentSnapshots.isEmpty()){
                                                    for(DocumentSnapshot request: queryDocumentSnapshots){
                                                        Request requestObject = changeDocToRequestModel(request);
                                                        Log.d("thestuffreturnedREQUEST", "middleman within the for loop" + requestObject.getPostid());
                                                        tempData.add(requestObject);
                                                    }
                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d("thestuffreturnedREQUEST", "THE DB CALL FAILED");
                                            }
                                        });
                                tasks.add(task);
                            }

                            Tasks.whenAll(tasks.toArray(new Task[0]))
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("thestuffreturnedREQUEST", "final resulting temp data" + tempData.toString());
                                            data.setValue(tempData);
                                        }
                                    });
                        } else {
                            Log.d(TAG, " data: null");
                        }
                    }
                });

        return data;
    }

    public LiveData<List<Listing>> getUserFavListingsObjects(String userId){
        final MutableLiveData<List<Listing>> data = new MutableLiveData<>();
        List<Listing> tempData = new ArrayList<>();

        Log.d("thestuffreturned", "user id from b4 db call" + userId);

        db.collection("Users")
                .document(userId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        if (snapshot != null && snapshot.exists()) {
                            Log.d(TAG, " data: " + snapshot.getData());

                            ArrayList<String> favListingsIds = (ArrayList<String>) snapshot.getData().get("fav_listings");
                            //Get array list of fav posts
                            Log.d("thestuffreturned", "my user id  " + userId);
                            Log.d("thestuffreturned", "my listing_fav_ids  " + favListingsIds.toString());

                            tempData.clear();
                            List<Task> tasks = new ArrayList<>();
                            for(int i = 0; i < favListingsIds.size(); i++) {
                                Task task =  db.collectionGroup("Listings")
                                    .whereEqualTo("postid", favListingsIds.get(i))
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            if(!queryDocumentSnapshots.isEmpty()){
                                                for(DocumentSnapshot listing: queryDocumentSnapshots){
                                                    Listing listingObject = changeDocToListingModel(listing);
                                                    //Fav favPost = new Fav(postid,  (ArrayList<String>) document.get("photos"));
                                                    Log.d("listingsDiagnostic", listingObject.getPostid());
                                                    tempData.add(listingObject);
                                                }
                                            }
                                        }
                                    });
                                tasks.add(task);
                            }

                            Tasks.whenAll(tasks.toArray(new Task[0]))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("thestuffreturned", "final resulting temp data" + tempData.toString());
                                    data.setValue(tempData);
                                }
                            });
                        } else {
                            Log.d(TAG, " data: null");
                        }
                    }
                });

        return data;
    }

    public LiveData<List<String>> getUserFavListings(String userId){
        final MutableLiveData<List<String>> data = new MutableLiveData<>();

        db.collection("Users")
                .document(userId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("TAG", "Listen failed.", e);
                            return;
                        }
                        if(snapshot.exists()){
                            ArrayList<String> favs = (ArrayList<String>) snapshot.get("fav_listings");
                            data.setValue(favs);
                        }
                    }
                });
        return data;
    }

    public void switchUserFavRequests(String userId, Request request, ImageView favorite){
        db.collection("Users")
                .document(userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot snapshot) {
                        if(snapshot.exists()){
                            ArrayList<String> favs = (ArrayList<String>) snapshot.get("fav_requests");
                            if (favs != null) {
                                if(favs.contains(request.getPostid())){
                                    favorite.setImageResource(R.drawable.favourite_post);
                                    Log.d("thisruns", request.getPostid());
                                    FirestoreRepo.getInstance().removeUserRequestFavs(request.getPostid(), userId);
                                }else{
                                    favorite.setImageResource(R.drawable.like);
                                    FirestoreRepo.getInstance().addUserRequestFavs(request.getPostid(), userId);
                                }
                            }
                        }
                    }
                });
    }

    public void switchUserFavPosts(String userId, MainPost post, ImageView favorite){
        db.collection("Users")
                .document(userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot snapshot) {
                        if(snapshot.exists()){
                            ArrayList<String> favs = (ArrayList<String>) snapshot.get("fav_posts");
                            if (favs != null) {
                                if(favs.contains(post.getPostId())){
                                    favorite.setImageResource(R.drawable.favourite_post);
                                    Log.d("thisruns", post.getPostId());
                                    FirestoreRepo.getInstance().removeUserPostFavs(post.getPostId(), userId);
                                }else{
                                    favorite.setImageResource(R.drawable.like);
                                    FirestoreRepo.getInstance().addUserPostFavs(post.getPostId(), userId);
                                }
                            }
                        }
                    }
                });
    }

    public void switchUserFavListings(String userId, Listing listing, ImageView favorite){
        db.collection("Users")
                .document(userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot snapshot) {
                        if(snapshot.exists()){
                            ArrayList<String> favs = (ArrayList<String>) snapshot.get("fav_listings");
                            if (favs != null) {
                                if(favs.contains(listing.getPostid())){
                                    favorite.setImageResource(R.drawable.favourite_post);
                                    Log.d("thisruns", listing.getPostid());
                                    FirestoreRepo.getInstance().removeUserListingFavs(listing.getPostid(), userId);
                                }else{
                                    favorite.setImageResource(R.drawable.like);
                                    FirestoreRepo.getInstance().addUserListingFavs(listing.getPostid(), userId);
                                }
                            }
                        }
                    }
                });
    }

    public void updateUserEmail(String email, String userId){
        db.collection("Users")
                .document(userId)
                .update("email", email);


        FirebaseAuth.getInstance().getCurrentUser()
            .updateEmail(email)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "User email address updated.");
                    }
                }
            });
    }

    public void sendResetPasswordEmail(String emailAddress){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.sendPasswordResetEmail(emailAddress.trim())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                        }
                    }
                });
    }

//    public void updateUserPassword(String password){
//
//        FirebaseAuth.getInstance().getCurrentUser()
//                .updatePassword(password)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Log.d(TAG, "User email address updated.");
//                        }
//                    }
//                });
//    }

//    public void updateUserIntro(String intro, String userId){
//        db.collection("Users")
//                .document(userId)
//                .update("intro", intro);
//    }

    public void updateUserUsername(String username, String userId){
        db.collection("Users")
                .document(userId)
                .update("username", username);

        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .orderByChild("uid")
                .equalTo(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(DataSnapshot user : dataSnapshot.getChildren()){
                            FirebaseDatabase.getInstance().getReference()
                                    .child("Users")
                                    .child(user.getKey())
                                    .child("username")
                                    .setValue(username);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }

    public void updateUserProfileUrl(String profile_url, String userId){
        db.collection("Users")
                .document(userId)
                .update("profile_url", profile_url);

        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .orderByChild("uid")
                .equalTo(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(DataSnapshot user : dataSnapshot.getChildren()){
                            FirebaseDatabase.getInstance().getReference()
                                    .child("Users")
                                    .child(user.getKey())
                                    .child("profile_url")
                                    .setValue(profile_url);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    public void deleteCurrentUser(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.delete()
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "User account deleted.");
                    }
                }
            });

        db.collection("Users")
                .document(user.getUid())
                .delete();

        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .orderByChild("uid")
                .equalTo(user.getUid())
                .getRef()
                .removeValue();
    }

//    public LiveData<User> getUserOnce(String uid){
//
//        final MutableLiveData<User> data = new MutableLiveData<>();
//
//        db.collection("Users")
//                .document(uid)
//                .get()
//                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot snapshot) {
//                        User user = snapshot.toObject(User.class);
//                        data.setValue(user);
//                    }
//                });
//
//        return data;
//    }


    public LiveData<User> getUser(String uid){

        final MutableLiveData<User> data = new MutableLiveData<>();

        db.collection("Users")
                .document(uid)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("TAG", "Listen failed.", e);
                            return;
                        }

                        User user = snapshot.toObject(User.class);
                        data.setValue(user);
                    }
                });

        return data;
    }

    public interface PostRetrieved {
        void onPostRetrieved(MainPost post);
    }

    //Todo: make all filed names like "postId" below into static final string variables
    public void getPost(String postId, PostRetrieved callback){
        db.collectionGroup("Posts")
            .whereEqualTo("postId", postId)
            .get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots){
                        MainPost post = snapshot.toObject(MainPost.class);
                        callback.onPostRetrieved(post);
                    }
                }
            });
    }

    public void getListing(String listingId, ListingRetrieved callback){
        db.collectionGroup("Listings")
                .whereEqualTo("postid", listingId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots){
                            Listing listing = snapshot.toObject(Listing.class);
                            callback.onListingRetrieved(listing);
                            //data.setValue(listing);
                            //some possible error here : snapshot.toObject may not work; postid may be different in spelling;
                        }
                    }
                });
    }

    public LiveData<List<Notification>> getUserNotifications(String uid) {
            final MutableLiveData<List<Notification>> data = new MutableLiveData<>();
            List<Notification> tempData = new ArrayList<>();

        db.collection("Users")
                .document(uid)
                .collection("Notifications")
                .orderBy("timeInMillis", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("TAG", "Listen failed.", e);
                            return;
                        }

                        tempData.clear();
                        for(DocumentSnapshot single: queryDocumentSnapshots){
                            Notification notifData = single.toObject(Notification.class);
                            tempData.add(notifData);
                        }

                        //Collections.sort(tempData, new SortNotifications());
                        data.setValue(tempData);
                    }
                });

        return data;
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

    public LiveData<List<Request>> getRequests(){
        final MutableLiveData<List<Request>> data = new MutableLiveData<>();
        List<Request> tempData = new ArrayList<>();

        db.collectionGroup("Requests")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("TAG", "Listen failed.", e);
                            return;
                        }

                        tempData.clear();
                        for(DocumentSnapshot single: queryDocumentSnapshots){
                            Request requestData= changeDocToRequestModel(single);
                            tempData.add(requestData);
                        }

                        Collections.sort(tempData, new SortRequests());
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

    public LiveData<List<Category>> getAllCategories(){
        final MutableLiveData<List<Category>> data = new MutableLiveData<>();
        List<Category> tempData = new ArrayList<>();

        db.collection("Categories")
            .get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    for(DocumentSnapshot snap : queryDocumentSnapshots){
                        Category newCategory = new Category(
                                snap.getString("name"),
                                snap.getString("photo")
                        );

                        tempData.add(newCategory);
                    }
                    data.setValue(tempData);
                }

            });
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

//    public LiveData<List<Comment>> getChatRoomMessages(String chatRoomId){
//        final MutableLiveData<List<Comment>> data = new MutableLiveData<>();
//        List<Comment> tempData = new ArrayList<>();
//
//        db.collection("Chatrooms")
//                .document(chatRoomId)
//                .collection("Comments")
//                .orderBy("timestamp", Query.Direction.DESCENDING)
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//                        if (e != null) {
//                            Log.w("TAG", "Listen failed.", e);
//                            return;
//                        }
//
//                        tempData.clear();
//                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
//                            Comment comment = changeDocToCommentModel(doc);
//                            Log.d("BUCK", comment.getMessage());
//                            tempData.add(comment);
//                        }
//
//                        Collections.sort(tempData, new SortMessages());
//                        data.setValue(tempData);
//                    }
//                });
//        return data;
//    }

    public void addNewMessage(Comment comment, String chatRoomId){

        Log.d("HUCK", "FIRESTORE REPO : " +  comment.getComment());

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

    public void addPostComment(String commentText, String commenterId, String posterUserId, String postId){

        DocumentReference newCommentRef = db.collection("Users")
                .document(posterUserId)
                .collection("Posts")
                .document(postId)
                .collection("Comments")
                .document();

        db.collection("Users")
                .document(commenterId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot snapshot) {
                        Comment newComment = new Comment(
                                commentText,
                                commenterId,
                                System.currentTimeMillis(),
                                snapshot.getString("username"),
                                snapshot.getString("profile_url"),
                                newCommentRef.getId()
                        );

                        newCommentRef.set(newComment);
                    }
                });
    }

    public LiveData<List<Comment>> getPostComments(String postId, String posterId) {
        final MutableLiveData<List<Comment>> data = new MutableLiveData<>();
        ArrayList<Comment> tempData = new ArrayList<>();

        db.collection("Users")
                .document(posterId)
                .collection("Posts")
                .document(postId)
                .collection("Comments")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("TAG", "Listen failed.", e);
                            return;
                        }

                        tempData.clear();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            //HERE might be a bit of a probem
                            Comment comment = doc.toObject(Comment.class);
                            tempData.add(comment);
                        }

                        //Below sorts posts according to date posted
                        Collections.sort(tempData, new SortComments());

                        data.setValue(tempData);
                    }
                });

        return data;
    }


    public void deleteUserComment(String commentId, String postId, String userId){
        db.collection("Users")
                .document(userId)
                .collection("Posts")
                .document(postId)
                .collection("Comments")
                .document(commentId)
                .delete();
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

    public void addDashboardMessage(String from, String to, String message){

        DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference();

        String messageSenderRef = "Dashboard/" + to;

        DatabaseReference userMessageKeyRef = RootRef.child("Dashboard")
                .child(to).push();

        String messagePushID = userMessageKeyRef.getKey();

        Message messageObject = new Message(
            from,
            message,
            "dashboard",
            to,
            messagePushID,
            "",
            "",
            System.currentTimeMillis(),
            "false"
        );

        Map messageBodyDetails = new HashMap();
        messageBodyDetails.put(messageSenderRef + "/" + messagePushID, messageObject);

        RootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task)
            {
                if(!task.isSuccessful()) {
                    //Toast.makeText(ChatActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

     public LiveData<List<Message>> getUserDashboardMessages(String userId) {
         final MutableLiveData<List<Message>> data = new MutableLiveData<>();
         List<Message> tempData = new ArrayList<>();

         FirebaseDatabase.getInstance().getReference()
             .child("Dashboards")
             .child(userId)
             .addValueEventListener(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     tempData.clear();
                     for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                         tempData.add(snapshot.getValue(Message.class));
                     }
                     data.setValue(tempData);
                 }

                 @Override
                 public void onCancelled(@NonNull DatabaseError databaseError) {

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
                            MainPost postData = changeDocToMainPostModel(doc);
                            tempData.add(postData);
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
                        .orderBy("nanopast", Query.Direction.DESCENDING)
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
                                //Collections.sort((ArrayList)tempData, new SortMain());
                                data.setValue(tempData);

                            }
                        });
                }
            }
        });

        return data;
    }

    public LiveData<List<Listing>> getFeedListings(String userId){
        DocumentReference userRef = FirestoreRepo.getInstance().getUserRef(userId);

        final MutableLiveData<List<Listing>> data = new MutableLiveData<>();
        List<Listing> tempData = new ArrayList<>();

        userRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                ArrayList<String> following = (ArrayList<String>) snapshot.get("following");
                following.add(userId);
                ///////////////////////////////////////////////////////////////
                tempData.clear();
                for(int i = 0; i < following.size(); i++){
                    String oneFollowing = following.get(i);
                    FirestoreRepo.getInstance().getUserListingsQuery(oneFollowing)
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {

                                    if (e != null) {
                                        Log.w("TAG", "Listen failed.", e);
                                        return;
                                    }

                                    for (QueryDocumentSnapshot doc : value) {
                                        Listing listdata = changeDocToListingModel(doc);
                                        tempData.add(listdata);
                                    }

                                    //Below sorts posts according to date posted
                                    Collections.sort(tempData, new SortListings());
                                    data.setValue(tempData);
                                }
                            });
                }
            }
        });

        return data;
    }

    public interface ListingRetrieved {
        void onListingRetrieved (Listing listing);
    }


    public LiveData<List<OrderChat>> getOrdersAndSells(String userId){
        final MutableLiveData<List<OrderChat>> data = new MutableLiveData<>();
        List<OrderChat> tempData = new ArrayList<>();

        Log.d("seroi", "What is going on");

        FirebaseDatabase.getInstance().getReference()
            .child("Offers")
            .child(userId)
            .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshotHighLevel) {

                    Log.d("seroi", "Orders and Sells : on data change got called on listener to single user path");

                    List<Task> tasks = new ArrayList<>();
                    List<OrderChat> orderChats = new ArrayList<>();

                    for (DataSnapshot childUserSnapshot: dataSnapshotHighLevel.getChildren()) {

                        String otherUserId = childUserSnapshot.getKey();

                        for(DataSnapshot listingSnapshot : childUserSnapshot.getChildren()){

                            String listingId = listingSnapshot.getKey();
                            ArrayList<Message> messages = new ArrayList<>();
                            for(DataSnapshot messageSnapshot : listingSnapshot.getChildren()){
                                //Log.d("hellplz", messageSnapshot.getValue(Message.class).toString());
                                if(messageSnapshot.child("type").equals("null")){
                                    messages.add(messageSnapshot.getValue(OfferMessage.class));
                                }else{
                                    messages.add(messageSnapshot.getValue(Message.class));
                                }
                            }

                            Collections.sort(messages, new SortMessages());
                            Message lastMessage = messages.get(messages.size() - 1);

                            Log.d("seroi", "Orders and Sells : This is the current last message" + lastMessage.getMessage());

                            Task task = db.collectionGroup("Listings")
                                .whereEqualTo("postid", listingId)
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                        for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots){
                                            Listing listing = snapshot.toObject(Listing.class);

                                            if(listing.getUserid().equals(userId)){
                                                orderChats.add(changeListingToMeSell(listing, otherUserId, lastMessage));
                                            }else{
                                                orderChats.add(changeListingToMeBuy(listing, lastMessage));
                                            }
                                        }
                                    }
                                });
                            tasks.add(task);

                        }

                    }
                    Tasks.whenAll(tasks.toArray(new Task[0])).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            tempData.clear();
                            for(int i = 0; i < orderChats.size(); i++) {
                                tempData.add(orderChats.get(i));
                            }

                            data.setValue(tempData);
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        return data;
    }


    public LiveData<Integer> getNumOfUnreadInOffersTab(String userId){
        final MutableLiveData<Integer> data = new MutableLiveData<>();

        //TODO: this is horrible - can you somehow clean this up?
        FirebaseDatabase.getInstance().getReference()
                .child("Offers")
                .child(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int totalUnread = 0;
                        for(DataSnapshot otherUsers : dataSnapshot.getChildren()){
                            for(DataSnapshot otherListings : otherUsers.getChildren()){
                                for(DataSnapshot otherUserMessages : otherListings.getChildren()){
                                    Message message = otherUserMessages.getValue(Message.class);
                                    if(message.getRead() != null){
                                        if(message.getRead().equals("false")){
                                            totalUnread++;
                                        }
                                    }
                                }
                            }
                        }
                        data.setValue(totalUnread);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        return data;
    }

    public LiveData<Integer> getNumOfUnreadInChatsTab(String userId){
        final MutableLiveData<Integer> data = new MutableLiveData<>();

        FirebaseDatabase.getInstance().getReference()
                .child("Messages")
                .child(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int totalUnread = 0;
                        for(DataSnapshot otherUsers : dataSnapshot.getChildren()){
                            for(DataSnapshot otherUserMessages : otherUsers.getChildren()){
                                Message message = otherUserMessages.getValue(Message.class);
                                if(message.getRead() != null){
                                    if(message.getRead().equals("false")){
                                        totalUnread++;
                                    }
                                }
                            }
                        }
                        data.setValue(totalUnread);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        return data;
    }

    public void getNumberOfUnreadOfferMessages(String myId, String otherUserId, String listingId, OnNumUnreadsGotten callback){
        FirebaseDatabase.getInstance().getReference()
                .child("Offers")
                .child(myId)
                .child(otherUserId)
                .child(listingId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int numOfUnreadMessages = 0;
                        for (DataSnapshot childMessageSnapshot : dataSnapshot.getChildren()) {
                            Message message = childMessageSnapshot.getValue(Message.class);
                            if(message.getRead() != null){
                                if(message.getRead().equals("false")){
                                    numOfUnreadMessages++;
                                }
                            }

                        }
                        callback.onNumUnreadsGotten(numOfUnreadMessages);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public interface OnNumUnreadsGotten{
        void onNumUnreadsGotten(int numUnreads);
    }

    public void getNumberOfUnreadMessages(String myId, String otherUserId, OnNumUnreadsGotten callback){
        FirebaseDatabase.getInstance().getReference()
                .child("Messages")
                .child(myId)
                .child(otherUserId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int numOfUnreadMessages = 0;
                        for (DataSnapshot childMessageSnapshot : dataSnapshot.getChildren()) {
                            Message message = childMessageSnapshot.getValue(Message.class);
                            if(message.getRead() != null){
                                if(message.getRead().equals("false")){
                                    numOfUnreadMessages++;
                                }
                            }

                        }
                        callback.onNumUnreadsGotten(numOfUnreadMessages);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    //Compare this solution with the getOrdersAndSells solution
    public LiveData<List<NormalChat>> getChattingWith(String userId){
        final MutableLiveData<List<NormalChat>> data = new MutableLiveData<>();
        List<NormalChat> tempData = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference()
                .child("Messages")
                .child(userId)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<Task> getUserTasks = new ArrayList<>();

                List<User> usersList = new ArrayList<>();

                //Looping through the people I am interacting with
                for (DataSnapshot childUserSnapshot: dataSnapshot.getChildren()) {

                    //Get the individual user model from Firestore
                    Task task = FirestoreRepo.getInstance()
                            .getUserRef(childUserSnapshot.getKey())
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        User user = changeDocToUserModel(snapshot);
                                        usersList.add(user);
                                    }
                                }
                            });
                    getUserTasks.add(task);
                }

                Tasks.whenAll(getUserTasks.toArray(new Task[0]))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                List<Task> messageTasks = new ArrayList<>();

                                for(User user : usersList){
                                    TaskCompletionSource<DataSnapshot> dbSource = new TaskCompletionSource<>();
                                    Task dbTask = dbSource.getTask();
                                    messageTasks.add(dbTask);

                                    //Get the latest Messaage that I had with this user
                                    FirebaseDatabase.getInstance().getReference()
                                        .child("Messages")
                                        .child(userId)
                                        .child(user.getUid())
                                        .orderByChild("nanopast")
                                        .limitToLast(1)
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                Log.d("messagestuckpoint", "Is there even a data snapshot? : " + dataSnapshot.toString());
                                                Log.d("messagestuckpoint", "Maybe it's my querying that's wrong? : " + dataSnapshot.getValue(Message.class).getMessage());
                                                dbSource.setResult(dataSnapshot);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                }

                                Log.d("messagestuckpoint", "The number of items in message tasks : " + messageTasks.size());
                                //When all latest messages have been gotten
                                Tasks.whenAll(messageTasks.toArray(new Task[0])).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        tempData.clear();
                                        for(int i = 0; i < messageTasks.size(); i++) {
                                            DataSnapshot messageSnapshot = (DataSnapshot) messageTasks.get(i).getResult();
                                            Log.d("messagestuckpoint", "This is what's in messagesnapshot, without tampering by getValue : " + messageSnapshot.toString());

                                            Message message = new Message();
                                            //WHAT kind of devilish data retrieval API is this? But actually it makes sense.
                                            //Even though you limited your query to one message, it still comes packaged in a "collection"
                                            //because at the end of the day it is the still the result of a query.
                                            for(DataSnapshot snapshot :  messageSnapshot.getChildren()){
                                                message = snapshot.getValue(Message.class);
                                            }
                                            Log.d("messagestuckpoint", "This is what's in messagesnapshot, AFTER tampering by getValue : " + message.getMessage());


                                            Log.d("messagestuckpoint", "The message nanopast : " + Long.toString(message.getNanopast()));
                                            Log.d("messagestuckpoint", "The message text : " + message.getMessage());

                                            User user = usersList.get(i);

                                            NormalChat normalChat = new NormalChat(
                                                    user,
                                                    message
                                            );

                                            Log.d("messagestuckpoint", "Normal chat object username : " + normalChat.getUser().getUsername());
                                            Log.d("messagestuckpoint", "Normal chat object last message : " + normalChat.getLastMessage());

                                            tempData.add(normalChat);
                                        }

                                        Collections.sort(tempData, new SortNormalChats());
                                        data.setValue(tempData);
                                    }
                                });
                            }
                        });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        return data;
    }

    public LiveData<List<User>> getFollowings(String userId){
        DocumentReference userRef = FirestoreRepo.getInstance().getUserRef(userId);

        final MutableLiveData<List<User>> data = new MutableLiveData<>();
        List<User> tempData = new ArrayList<>();

        userRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                ArrayList<String> following = (ArrayList<String>) snapshot.get("following");

                tempData.clear();
                List<Task> tasks = new ArrayList<>();
                for(int i = 0; i < following.size(); i++) {

                    Task task = FirestoreRepo.getInstance()
                            .getUserRef(following.get(i))
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        User user = changeDocToUserModel(snapshot);
                                        tempData.add(user);
                                    }
                                }
                            });
                    tasks.add(task);
                }

                Tasks.whenAll(tasks.toArray(new Task[0]))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("thestuffreturnedREQUEST", "final resulting temp data" + tempData.toString());
                                data.setValue(tempData);
                            }
                        });

                //Todo: find method of using diff util-like logic so that i don't need to rebuild the tempdata everytime the data changes
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

    public Query getUserListingsQuery(String userId){
        Query userListingsTask = db.collection("Users")
                .document(userId)
                .collection("Listings");
        return userListingsTask;
    }

    //Utils
    class SortNotifications implements Comparator<Notification> {
        public int compare(Notification a, Notification b){
            return (int)b.getTimeInMillis() - (int)a.getTimeInMillis();
        }
    }

    class SortNormalChats implements Comparator<NormalChat> {
        public int compare(NormalChat a, NormalChat b){
            return (int)b.getRawLastInteractionTime() - (int)a.getRawLastInteractionTime();
        }
    }

    class SortMessages implements Comparator<Message> {
        public int compare(Message a, Message b){
            return (int)b.getNanopast() - (int)a.getNanopast();
        }
    }

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

    class SortRequests implements Comparator<Request> {
        public int compare(Request a, Request b){
            return (int)b.getNanopast() - (int)a.getNanopast();
        }
    }

    class SortComments implements Comparator<Comment> {
        public int compare(Comment a, Comment b){
            return (int)a.getDate_created() - (int)b.getDate_created();
        }
    }

//    class SortMessages implements Comparator<Comment> {
//        public int compare(Comment a, Comment b){
//            return Math.toIntExact(b.getTimestamp() - a.getTimestamp());
//        }
//    }

    private MainPost changeDocToMainPostModel(QueryDocumentSnapshot doc){
        MainPost post = new MainPost(
                doc.getString("user_id"),
                doc.getString("desc"),
                doc.getString("hashtags"),
                doc.getId(),
                doc.getString("username"),
                (ArrayList<String>) doc.get("photos"),
                doc.getString("timestamp"),
                (long)(doc.get("nanopast") == null ? 0L : doc.get("nanopast"))
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

    private User changeDocToUserModel(DocumentSnapshot doc){
        return new User(
            doc.getString("username"),
            doc.getString("uid"),
            doc.getString("profile_url"),
            (ArrayList<String>) doc.get("following"),
            (ArrayList<String>) doc.get("followers"),
            (ArrayList<String>) doc.get("chatrooms"),
            (ArrayList<String>) doc.get("fav_listings"),
            (ArrayList<String>) doc.get("fav_posts"),
            (ArrayList<String>) doc.get("fav_requests"),
            (ArrayList<String>) doc.get("tab_categories"),
            doc.getString("email"),
            doc.getString("introduction"),
            doc.getString("token")
        );
    }


//    private Comment changeDocToCommentModel(QueryDocumentSnapshot doc){
//        return new Comment(
//                doc.getString("senderId"),
//                doc.getString("message"),
//                doc.getLong("timestamp"),
//                doc.getString("otherUserPic")
//        );
//    }

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

    private Request changeDocToRequestModel(DocumentSnapshot doc){
        return new Request(
                doc.getString("userid"),
                (long)doc.get("price"),
                (ArrayList<String>) doc.get("photos"),
                doc.getString("name"),
                doc.getString("hashtags"),
                doc.getString("desc"),
                "temp",
                doc.getId(),
                (long)doc.get("nanopast"),
                doc.getString("categories")
        );
    }

    private OrderChat changeListingToMeSell(Listing listing, String buyerId, Message lastMessage){
        //TODO: find a more efficient way to do the below
        return new OrderChat(
//            listing.getUserid(),
//            listing.getReturn_exchange(),
//            listing.getPrice(),
//            listing.getPhotos(),
//            listing.getName(),
//            listing.getHashtags(),
//            listing.getDesc(),
//            listing.getDelivery(),
//            listing.getUsername(),
//            listing.getPostid(),
//            listing.getNanopast(),
//            listing.getCategories(),
            listing,
            lastMessage,
            buyerId
        );
    }

    public OrderChat changeListingToMeBuy(Listing listing, Message lastMessage){
        return new OrderChat(
//                listing.getUserid(),
//                listing.getReturn_exchange(),
//                listing.getPrice(),
//                listing.getPhotos(),
//                listing.getName(),
//                listing.getHashtags(),
//                listing.getDesc(),
//                listing.getDelivery(),
//                listing.getUsername(),
//                listing.getPostid(),
//                listing.getNanopast(),
//                listing.getCategories(),
                listing,
                lastMessage,
                listing.getUserid()
        );
    }
}
