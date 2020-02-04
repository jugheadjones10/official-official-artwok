package com.example.artwokmabel.profile.activities;

import android.net.Uri;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class UploadGroupPostActivity extends AppCompatActivity {

    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private ImageView uploadPic; // change to ViewPager later
    private ImageView boldBtn, italicsBtn, underlineBtn, colorBtn, highlightBtn, leftBtn, centerBtn, rightBtn, justifiedBtn, fillBtn;
    private TextView newPostDesc, newPostHashtags;
    private Button uploadBtn, saveBtn, redBtn, greenBtn, blueBtn, yellowBtn, purpleBtn, blackBtn, whiteBtn;
    private ProgressBar uploadProgress;
    private Uri postImageUri = null;
    private ArrayList<String> postImageUris = new ArrayList<String>();
    private boolean more_than_one_img = false;
    private Spinner fontSizeSpinner, fontSelectSpinner;
    private ViewPager viewPager;
    private Button selectCategoryBtn;
//    private ArrayList<String> finCatList = new ArrayList<String>();
//    private HorizontalScrollView colorPalette;
//
//
//    public static String [] uris = new String [5];
//    int numPostArray[] = {0};
//    boolean displayViewPager = false;
//
//    int h_or_t = 0; // 0 is none, 1 is highlight, 2 is text
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//
//        postImageUris.clear();
//
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_upload_post);
//
//        // ini
//        storageReference = FirebaseStorage.getInstance().getReference();
//        firebaseFirestore = FirebaseFirestore.getInstance();
//
//        fontSelectSpinner = findViewById(R.id.fontSelectSpinner);
//        fontSizeSpinner = findViewById(R.id.fontSizeSpinner);
//
//        uploadProgress = findViewById(R.id.upload_progress);
//        uploadBtn = findViewById(R.id.upload_btn);
//        newPostDesc = findViewById(R.id.new_post_desc);
//        newPostHashtags = findViewById(R.id.new_post_hashtags);
//        uploadPic = findViewById(R.id.uploadPic);
//        boldBtn = findViewById(R.id.boldBtn);
//        italicsBtn = findViewById(R.id.italicsBtn);
//        underlineBtn = findViewById(R.id.underlineBtn);
//        highlightBtn = findViewById(R.id.highlightBtn);
//        saveBtn = findViewById(R.id.save_btn);
//        redBtn = findViewById(R.id.redBtn);
//        greenBtn = findViewById(R.id.greenBtn);
//        blueBtn = findViewById(R.id.blueBtn);
//        yellowBtn = findViewById(R.id.yellowBtn);
//        purpleBtn = findViewById(R.id.purpleBtn);
//        blackBtn = findViewById(R.id.blackBtn);
//        //whiteBtn = findViewById(R.id.whiteBtn);
//        uploadBtn = findViewById(R.id.upload_btn);
//        fillBtn = findViewById(R.id.fillBtn);
//        leftBtn = findViewById(R.id.alignLeft);
//        centerBtn = findViewById(R.id.alignCenter);
//        rightBtn = findViewById(R.id.alignRight);
//
//
//        newPostHashtags.addTextChangedListener(hashtagWatcher);
//
//        colorPalette = findViewById(R.id.color_parlette_view);
//        colorPalette.setVisibility(View.INVISIBLE);
//
//
//        //autoLinkTextView.addAutoLinkMode(MODE_HASHTAG);
//        //autoLinkTextView.hashTagModeColor = ContextCompat.getColor(this,);
//        //getUserPostNumPost();
//        //if(numPostArray[0] == "0") userPostNumInitialization();
//        Typeface oswald = Typeface.createFromAsset(getAssets(),"fonts/Oswald-Regular.ttf");
//        Typeface roboto = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Regular.ttf");
//        Typeface openSans = Typeface.createFromAsset(getAssets(),"fonts/OpenSans-Regular.ttf");
//        Typeface zcoolXiaoWei = Typeface.createFromAsset(getAssets(),"fonts/ZCOOLXiaoWei-Regular.ttf");
//
//
//        fontSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
//        {
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
//            {
//                String selectedItem = parent.getItemAtPosition(position).toString();
//                newPostDesc.setTextSize(Integer.parseInt(selectedItem));
//
//            } // to close the onItemSelected
//            public void onNothingSelected(AdapterView<?> parent)
//            {
//
//            }
//        });
//
//        fontSelectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
//        {
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
//            {
//                String selectedItem = parent.getItemAtPosition(position).toString();
//                if(selectedItem.equals("Oswald")){
//                    newPostDesc.setTypeface(oswald);
//                }
//                else if(selectedItem.equals("Roboto")){
//                    newPostDesc.setTypeface(roboto);
//                }
//                else if(selectedItem.equals("Open Sans")){
//                    newPostDesc.setTypeface(openSans);
//                }
//                else if(selectedItem.equals("ZCOOL XiaoWei")){
//                    newPostDesc.setTypeface(zcoolXiaoWei);
//                }
//            } // to close the onItemSelected
//            public void onNothingSelected(AdapterView<?> parent)
//            {
//
//            }
//        });
//
//        highlightBtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                if(colorPalette.getVisibility() == View.INVISIBLE) {
//                    colorPalette.setVisibility(View.VISIBLE);
//                    h_or_t = 1;
//                }
//                else if(colorPalette.getVisibility() == View.VISIBLE) {
//                    if(h_or_t==1){
//                        colorPalette.setVisibility(View.INVISIBLE);
//                        h_or_t = 0;
//                    }
//                    else if(h_or_t==2){
//                        h_or_t = 1;
//                    }
//                }
//            }
//        });
//
//        fillBtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                if(colorPalette.getVisibility() == View.INVISIBLE) {
//                    colorPalette.setVisibility(View.VISIBLE);
//                    h_or_t = 2;
//                }
//                else if(colorPalette.getVisibility() == View.VISIBLE) {
//                    if(h_or_t==2){
//                        colorPalette.setVisibility(View.INVISIBLE);
//                        h_or_t = 0;
//                    }
//                    else if(h_or_t==1){
//                        h_or_t = 2;
//                    }
//                }
//            }
//        });
//
//
//        redBtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                if(h_or_t==2) newPostDesc.setTextColor(Color.parseColor("#ed2423"));
//                else if(h_or_t==1) {
//                    newPostDesc.setBackgroundColor(Color.parseColor("#ed2423"));
//                }
//            }
//        });
//
//        greenBtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                if(h_or_t==2) newPostDesc.setTextColor(Color.parseColor("#2C5F2D"));
//                else if(h_or_t==1) {
//                    newPostDesc.setBackgroundColor(Color.parseColor("#2C5F2D"));
//                }
//            }
//        });
//
//        yellowBtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                if(h_or_t==2) newPostDesc.setTextColor(Color.parseColor("#F1CE01"));
//                else if(h_or_t==1) {
//                    newPostDesc.setBackgroundColor(Color.parseColor("#F1CE01"));
//                }
//            }
//        });
//
//        blueBtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                if(h_or_t==2) newPostDesc.setTextColor(Color.parseColor("#87CEEB"));
//                else if(h_or_t==1) {
//                    newPostDesc.setBackgroundColor(Color.parseColor("#87CEEB"));
//                }
//            }
//        });
//
//        purpleBtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                if(h_or_t==2) newPostDesc.setTextColor(Color.parseColor("#B19CD9"));
//                else if(h_or_t==1) {
//                    newPostDesc.setBackgroundColor(Color.parseColor("#B19CD9"));
//                }
//            }
//        });
//
//        blackBtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                if(h_or_t==2) newPostDesc.setTextColor(Color.BLACK);
//                else if(h_or_t==1) {
//                    newPostDesc.setBackgroundColor(Color.BLACK);
//                }
//            }
//        });
//
//
//        boldBtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                //newPostDesc.setTypeface(newPostDesc.getTypeface(), Typeface.BOLD);
//                if(newPostDesc.getTypeface() == null){
//                    newPostDesc.setTypeface(Typeface.create(newPostDesc.getTypeface(), Typeface.NORMAL));
//
//                }else{
//
//                    if(newPostDesc.getTypeface().getStyle() == Typeface.BOLD)  newPostDesc.setTypeface(Typeface.create(newPostDesc.getTypeface(), Typeface.NORMAL));
//                    else if(newPostDesc.getTypeface().getStyle() == Typeface.NORMAL) newPostDesc.setTypeface(newPostDesc.getTypeface(), Typeface.BOLD);
//                    else if(newPostDesc.getTypeface().getStyle() == (Typeface.ITALIC)) newPostDesc.setTypeface(newPostDesc.getTypeface(), Typeface.BOLD_ITALIC);
//                    else if(newPostDesc.getTypeface().getStyle() == (Typeface.BOLD_ITALIC)) newPostDesc.setTypeface(newPostDesc.getTypeface(), Typeface.ITALIC);
//                }
//            }
//        });
//
//
//        italicsBtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                //newPostDesc.setTypeface(newPostDesc.getTypeface(), Typeface.ITALIC);
//                if(newPostDesc.getTypeface() == null){
//                    newPostDesc.setTypeface(Typeface.create(newPostDesc.getTypeface(), Typeface.NORMAL));
//
//                }else{
//                    if(newPostDesc.getTypeface().getStyle() == Typeface.ITALIC)  newPostDesc.setTypeface(Typeface.create(newPostDesc.getTypeface(), Typeface.NORMAL));
//                    else if(newPostDesc.getTypeface().getStyle() == Typeface.NORMAL) newPostDesc.setTypeface(newPostDesc.getTypeface(), Typeface.ITALIC);
//                    else if(newPostDesc.getTypeface().getStyle() == (Typeface.BOLD)) newPostDesc.setTypeface(newPostDesc.getTypeface(), Typeface.BOLD_ITALIC);
//                    else if(newPostDesc.getTypeface().getStyle() == (Typeface.BOLD_ITALIC)) newPostDesc.setTypeface(newPostDesc.getTypeface(), Typeface.BOLD);
//                }
//            }
//        });
//
//
//        underlineBtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                if ((newPostDesc.getPaintFlags() & Paint.UNDERLINE_TEXT_FLAG) > 0){
//                    newPostDesc.setPaintFlags(newPostDesc.getPaintFlags() & (~ Paint.UNDERLINE_TEXT_FLAG));
//                }
//                else{
//                    newPostDesc.setPaintFlags(newPostDesc.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
//                }
//            }
//        });
//
//
//        leftBtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                newPostDesc.setGravity(Gravity.LEFT);
//            }
//        });
//
//
//
//        centerBtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                newPostDesc.setGravity(Gravity.CENTER_HORIZONTAL);
//
//            }
//        });
//
//
//
//        rightBtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                newPostDesc.setGravity(Gravity.RIGHT);
//            }
//        });
//
//
//        uploadPic.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                // opens gallery when button is clicked after checking if app has access to user files
//                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//                    if(ContextCompat.checkSelfPermission(UploadGroupPostActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
//                        ActivityCompat.requestPermissions(UploadGroupPostActivity.this, new String[] {android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
//                    }
//                    else{
//                        postImageUris.clear();
//                        openGallery();
//                    }
//                }
//            }
//        });
//
//
//        uploadBtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick (View view){
//                if(newPostDesc.getText().toString().isEmpty()){
//                    Toast.makeText(UploadGroupPostActivity.this, "Please fill the description for your listing.", Toast.LENGTH_LONG).show();
//    // CHANGE THIS PART TO MAKE IT SPECIFIC FOR GROUPS
//    // FROM HERE
//                } else if(postImageUris.isEmpty() && postImageUri == null) {
//                    // Toast.makeText(UploadGroupPostActivity.this, "Please add at least one photo of your listing.", Toast.LENGTH_LONG).show();
//                    // text (hashtags optional) has been chosen but no picture has been chosen; no empty or null value
//                    uploadProgress.setVisibility(View.VISIBLE);
//
//                    Date today = new Date();
//                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    String dateToStr = format.format(today);
//                    getUserPostNumPost();
//
//                    StorageReference filePath = storageReference.child("Images").child(LoginActivityOld.uid).child(dateToStr); // currently one post per user; will change numbering system according to current number of posts
//
//                    UploadTask objectUploadTask = filePath.putFile(postImageUri);
//                    objectUploadTask.continueWithTask(task -> {
//                        if(!task.isSuccessful()){
//                            throw task.getException();
//                        }
//                        return filePath.getDownloadUrl();
//                    }).addOnCompleteListener(task -> {
//                        if(task.isSuccessful()){
//                            Map<String, Object> objectMap = new HashMap<>();
//                            postImageUris.add(task.getResult().toString());
//                            objectMap.put("photos", postImageUris);
//                            objectMap.put("desc", newPostDesc.getText().toString());
//                            objectMap.put("hashtags", newPostHashtags.getText().toString());
//                            objectMap.put("user_id", LoginActivityOld.uid);
//
//                            firebaseFirestore.collection("Users").document(LoginActivityOld.uid).collection("Posts").document((numPostArray[0] + 1) + " " + dateToStr).set(objectMap)
//                                    .addOnSuccessListener(aVoid -> {
//                                        Toast.makeText(UploadGroupPostActivity.this, "Successfully uploaded ic_dm.", Toast.LENGTH_LONG).show();
//                                        Intent intent = new Intent(getApplicationContext(), ProfilePageActivity.class);
//                                        startActivity(intent);
//                                        uploadProgress.setVisibility(View.INVISIBLE);
//                                    })
//                                    .addOnFailureListener(e ->
//                                            Toast.makeText(UploadGroupPostActivity.this, "Failed to upload ic_dm.", Toast.LENGTH_LONG)
//                                                    .show());
//                        }
//                        else if(!task.isSuccessful()) {
//                            Toast.makeText(UploadGroupPostActivity.this, task.getException().toString(), Toast.LENGTH_LONG).show();
//                        }
//                    });
//
//                    firebaseFirestore
//                            .collection("Users")
//                            .document(LoginActivityOld.uid)
//                            .update("number_of_posts",  FieldValue.increment(1));
//    // TO HERE
//    // CHANGE THIS PART TO MAKE IT SPECIFIC FOR GROUPS
//                } else {
//                    // text (hashtags optional) and picture has been chosen; no empty or null value
//                    uploadProgress.setVisibility(View.VISIBLE);
//
//                    Date today = new Date();
//                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    String dateToStr = format.format(today);
//                    getUserPostNumPost();
//
//                    if(more_than_one_img){
//                        Map<String, Object> objectMap = new HashMap<>();
//                        objectMap.put("photos", postImageUris);
//                        objectMap.put("desc", newPostDesc.getText().toString());
//                        objectMap.put("hashtags", newPostHashtags.getText().toString());
//                        objectMap.put("user_id", LoginActivityOld.uid);
//                        ///////****************************************????????????//////////////
//                        ///////****************************************????????????//////////////
//                        //Above "ProfilePageActivity.class" should be changed to profileFramgent (next time)
//
//                        firebaseFirestore.collection("Users").document(LoginActivityOld.uid).collection("Posts").document((numPostArray[0] + 1) + " " + dateToStr).set(objectMap)
//                                .addOnSuccessListener(aVoid -> {
//                                    Toast.makeText(UploadGroupPostActivity.this, "Successfully uploaded ic_dm.", Toast.LENGTH_LONG).show();
//                                    Intent intent = new Intent(getApplicationContext(), ProfilePageActivity.class);
//                                    startActivity(intent);
//                                    uploadProgress.setVisibility(View.INVISIBLE);
//                                })
//                                .addOnFailureListener(e ->
//                                        Toast.makeText(UploadGroupPostActivity.this, "Failed to upload ic_dm.", Toast.LENGTH_LONG)
//                                                .show());
//
//                        firebaseFirestore
//                                .collection("Users")
//                                .document(LoginActivityOld.uid)
//                                .update("number_of_posts", FieldValue.increment(1));
//                    }
//                    else{
//                        StorageReference filePath = storageReference.child("Images").child(LoginActivityOld.uid).child(dateToStr); // currently one post per user; will change numbering system according to current number of posts
//
//                        UploadTask objectUploadTask = filePath.putFile(postImageUri);
//                        objectUploadTask.continueWithTask(task -> {
//                            if(!task.isSuccessful()){
//                                throw task.getException();
//                            }
//                            return filePath.getDownloadUrl();
//                        }).addOnCompleteListener(task -> {
//                            if(task.isSuccessful()){
//                                Map<String, Object> objectMap = new HashMap<>();
//                                postImageUris.add(task.getResult().toString());
//                                objectMap.put("photos", postImageUris);
//                                objectMap.put("desc", newPostDesc.getText().toString());
//                                objectMap.put("hashtags", newPostHashtags.getText().toString());
//                                objectMap.put("user_id", LoginActivityOld.uid);
//
//                                firebaseFirestore.collection("Users").document(LoginActivityOld.uid).collection("Posts").document((numPostArray[0] + 1) + " " + dateToStr).set(objectMap)
//                                        .addOnSuccessListener(aVoid -> {
//                                            Toast.makeText(UploadGroupPostActivity.this, "Successfully uploaded ic_dm.", Toast.LENGTH_LONG).show();
//                                            Intent intent = new Intent(getApplicationContext(), ProfilePageActivity.class);
//                                            startActivity(intent);
//                                            uploadProgress.setVisibility(View.INVISIBLE);
//                                        })
//                                        .addOnFailureListener(e ->
//                                                Toast.makeText(UploadGroupPostActivity.this, "Failed to upload ic_dm.", Toast.LENGTH_LONG)
//                                                        .show());
//                            }
//                            else if(!task.isSuccessful()) {
//                                Toast.makeText(UploadGroupPostActivity.this, task.getException().toString(), Toast.LENGTH_LONG).show();
//                            }
//                        });
//
//                        firebaseFirestore
//                                .collection("Users")
//                                .document(LoginActivityOld.uid)
//                                .update("number_of_posts",  FieldValue.increment(1));
//
//                    }
//                }
//            }
//        });
//    }
//
//    private void openGallery(){ // opens gallery intent and waits for user to pick an ic_dm
//        Intent intent = new Intent();
//        intent.setType("ic_dm/*");
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent,"Select Picture"), 1);
//    }
//
//    public void getUserPostNumPost(){
//        DocumentReference docRef = firebaseFirestore.collection("Users").document(LoginActivityOld.uid);
//
//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        Log.d("LOG: ", "DocumentSnapshot data: " + document.getData());
//                        String name = "number_of_posts";
//                        numPostArray[0] = ((Long) document.get(name)).intValue();
//
//                    } else {
//                        Log.d("LOG: ", "No such document");
//                        getUserPostNumPost();
//                    }
//                } else {
//                    Log.d("LOG: ", "get failed with ", task.getException());
//                }
//            }
//        });
//    }
//
//    @Override
//    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(data.getClipData() != null){
//            more_than_one_img = true;
//            int totalItemsSelected = data.getClipData().getItemCount();
//            if(totalItemsSelected > 5){
//                Toast.makeText(UploadGroupPostActivity.this, "You can only upload 5 images at a time. The first 5 have been automatically selected.", Toast.LENGTH_LONG).show();
//            }
//            Toast.makeText(UploadGroupPostActivity.this, "uid: " + LoginActivityOld.uid + "" , Toast.LENGTH_LONG).show();
//
//            for(int i = 0; i < (totalItemsSelected > 5 ? 5 : totalItemsSelected); i++){
//                Uri fileUri = data.getClipData().getItemAt(i).getUri();
//
//                String fileName = getFileName(fileUri);
//                //String number_of_posts = getUserPostNumPost();
//
//                StorageReference fileToUpload = storageReference.child("Images").child(LoginActivityOld.uid).child(fileName); // randomize name
//                fileToUpload.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        storageReference.child("Images").child(LoginActivityOld.uid).child(fileName).getDownloadUrl().addOnCompleteListener(task -> {
//                            if(task.isSuccessful()) {
//                                postImageUris.add(task.getResult().toString());
//                                Log.d("LOG: ", "postImageUris " + task.getResult().toString());
//                                for(int i = 0; i < postImageUris.size(); i++){
//                                    uris[i] = postImageUris.get(i);
//                                    Log.d("LOG: ", "Uris " + postImageUris.size());
//                                }
//                                displayViewPager = true;
//                            }
//                        });
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(UploadGroupPostActivity.this, "failed" , Toast.LENGTH_LONG).show();
//
//                    }
//                });
//                //Toast.makeText(UploadGroupPostActivity.this, postImageUris.size() + "" , Toast.LENGTH_LONG).show();
//
//            }
//
//        }
//        else{
//            if (data != null) {
//                postImageUri = data.getData();
//                uploadPic.setImageURI(postImageUri); // set this to viewpager
//                more_than_one_img = false;
//            }
//        }
//    }
//
//
//    private final TextWatcher hashtagWatcher = new TextWatcher() {
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
//        public void afterTextChanged(Editable s) { }
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            if(s.charAt(s.length() - 1) == ' '){ // if the last character is a space and , auto add hashtag after space
//                newPostHashtags.append("#");
//            }
///*
//            if(newPostHashtags.getSelectionStart() > 1 && s.charAt(newPostHashtags.getSelectionStart() - 1) == ' '){
//                String temp = s.toString().substring(0, newPostHashtags.getSelectionStart() - 1);
//                newPostHashtags.setText("");
//                newPostHashtags.append(temp);
//
//            }
//*/
//            /*
//            if(s.length() > 0 && s.charAt(s.length() - 1) == '#'){
//                newPostHashtags.setText(s.toString().substring(0, s.length() - 1));
//            }*/
//        }
//    };
//
//    public String getFileName(Uri uri) {
//        String result = null;
//        if (uri.getScheme().equals("content")) {
//            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
//            try {
//                if (cursor != null && cursor.moveToFirst()) {
//                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
//                }
//            } finally {
//                cursor.close();
//            }
//        }
//        if (result == null) {
//            result = uri.getPath();
//            int cut = result.lastIndexOf('/');
//            if (cut != -1) {
//                result = result.substring(cut + 1);
//            }
//        }
//        return result;
//    }
}