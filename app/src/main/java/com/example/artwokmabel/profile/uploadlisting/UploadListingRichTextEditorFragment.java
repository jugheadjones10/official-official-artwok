package com.example.artwokmabel.profile.uploadlisting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.artwokmabel.R;
import com.example.artwokmabel.Utils.TimeWrangler;
import com.example.artwokmabel.databinding.FragmentUploadPostBinding;
import com.example.artwokmabel.homepage.callbacks.ImagePickerCallback;
import com.example.artwokmabel.models.User;
import com.example.artwokmabel.profile.uploadpost.RichEditor;
import com.example.artwokmabel.profile.user.ProfileFragmentViewModel;
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog;
import com.github.dhaval2404.colorpicker.listener.ColorListener;
import com.github.dhaval2404.colorpicker.model.ColorShape;
import com.github.dhaval2404.colorpicker.model.ColorSwatch;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.example.artwokmabel.profile.utils.ImagePickerActivity.SHOW_ALL_OPTIONS;

public class UploadListingRichTextEditorFragment extends Fragment {

    public static final List<String> palette_colors = Arrays.asList("#000000", "#ffffff", "#FC1D1D", "#F5A525", "#FFF614", "#686868",
            "#2638AA", "#009B4D", "#01BBD4", "#9BDAF6", "#F9F9F9",
            "#FF7978", "#FEBE76", "#F6E58D", "#ADEE5F", "#95AFC0",
            "#E572FD", "#FF98E8", "#85E7FF", "#DFF9FA", "#686EE0");

    private NavController navController;
    private FragmentUploadPostBinding binding;
    private FirebaseAuth mAuth;
    private int originalMode;
    private RichEditor mEditor;
    private String currentTextColor = "#000000";
    private String currentBgColor = "#000000";
    private ArrayList<View> headingViews = new ArrayList<>();
    private ArrayList<String> selectedViews = new ArrayList<>();
    public static final int REQUEST_IMAGE = 100;
    public CheckIfStartObject checkIfStartObject;

    public UploadListingDescViewModel viewModel;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private ProfileFragmentViewModel profileFragmentViewModel;

    public UploadListingRichTextEditorFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_upload_post, container, false);
        mAuth = FirebaseAuth.getInstance();

        viewModel = new ViewModelProvider(requireActivity()).get(UploadListingDescViewModel.class);
        profileFragmentViewModel = new ViewModelProvider(this).get(ProfileFragmentViewModel.class);
        profileFragmentViewModel.getUserObservable(mAuth.getCurrentUser().getUid()).observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user != null){
                    binding.setUser(user);
                    Picasso
                            .get()
                            .load(user.getProfile_url())
                            .placeholder(R.drawable.loading_image)
                            .error(R.drawable.loading_image)
                            .into(binding.profilePicture);
                    binding.setTime(TimeWrangler.changeNanopastToReadableDate(System.currentTimeMillis()));
                }
            }
        });

        binding.progressBar.setVisibility(View.GONE);
        originalMode = getActivity().getWindow().getAttributes().softInputMode;
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        headingViews.add(binding.actionHeading1);
        headingViews.add(binding.actionHeading2);
        headingViews.add(binding.actionHeading3);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d("uploadpostoncreateview", "ON VIEW CREATED IT WAS CALLED");
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.toolbar);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        setHasOptionsMenu(true);

        viewModel.getHtmlContent().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s != null){
                    mEditor.setHtml(s);
                }
            }
        });

        initView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_upload_listing_rich_text, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                viewModel.setHtmlContent(mEditor.getHtml());
                navController.navigateUp();
                return true;
            case android.R.id.home:
                //intercept back button
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Set grey bg if not, set not if grey bg
    private void setBackground(View view){
        if(view.getBackground() == null){
            view.setBackgroundResource(R.drawable.rounded_corners);
        }else{
            view.setBackground(null);
        }
    }

    private void onHeadingClicked(View view, int heading){
        if(view.getBackground() == null){
            view.setBackgroundResource(R.drawable.rounded_corners);

            for(View headingView : headingViews){
                if(!headingView.equals(view)){
                    headingView.setBackground(null);
                }
            }
            mEditor.setHeading(heading);
        }else{
            view.setBackground(null);
            mEditor.unsetHeading();
        }
    }

    public void clearAllBackgrounds(){
        Log.d("isstart", "THe fk says : " + checkIfStartObject.fk);
        Log.d("isstart", mEditor.getHtml());
        if(checkIfStartObject.isStart()){
            for(String viewName : selectedViews){
                switch (viewName) {
                    case "bold":
                        binding.actionBold.performClick();
                        break;
                    case "italic":
                        binding.actionItalic.performClick();
                        break;
                    case "strike":
                        binding.actionStrikethrough.performClick();
                        break;
                    case "underline":
                        binding.actionUnderline.performClick();
                        break;
                }
            }
            selectedViews.clear();
            Log.d("isstart", "It's the start");
        }else{
            Log.d("isstart", "It's not the start");
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().getWindow().setSoftInputMode(originalMode);
    }

    ////////////////////
    public class CheckIfStartObject {
        private boolean isStart;
        public String fk;
        public boolean isStart() { return isStart; }

        @JavascriptInterface
        public void setStart(boolean isStart){this.isStart = isStart;}

        @JavascriptInterface
        public void setFk(String fr){this.fk = fr;}

    }

    public interface OnIsStartFound{
        void clearBackgrounds(String s);
    }
    /////////////////////

    private  void initView(){

//        image_view_done_rich=(ImageView) findViewById(R.id.image_view_done_rich);
//        relativeLayout_rich_box=(RelativeLayout) findViewById(R.id.relativeLayout_rich_box);
//        pd = new ProgressDialog(editor.this);
//        pd.setMessage("Uploading Post");
//        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        pd.setCancelable(false);
//        this.edit_text_upload_description =  (RichEditor) findViewById(R.id.editor);

        mEditor = (RichEditor) binding.editor;

        //////////////////////////
        checkIfStartObject = new UploadListingRichTextEditorFragment.CheckIfStartObject();
        mEditor.addJavascriptInterface(checkIfStartObject, "checkIfStartObject");
        ////////////////////////

        //mEditor.setEditorBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundResource(R.drawable.bg);
        mEditor.setPadding(10, 10, 10, 10);
        //mEditor.setBackground("https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg");
        mEditor.setPlaceholder("Insert text here...");
        //mEditor.setInputEnabled(false);


        binding.actionUndo.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.undo();
            }
        });

        binding.actionRedo.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.redo();
            }
        });

        binding.actionBold.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                setBackground(binding.actionBold);
                mEditor.setBold();

                if(!selectedViews.contains("bold")){
                    selectedViews.add("bold");
                }
            }
        });

        binding.actionItalic.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                setBackground(binding.actionItalic);
                mEditor.setItalic();

                if(!selectedViews.contains("italic")){
                    selectedViews.add("italic");
                }

            }
        });

//        binding.actionSubscript.setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                mEditor.setSubscript();
//            }
//        });
//
//        binding.actionSuperscript.setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                mEditor.setSuperscript();
//            }
//        });

        binding.actionStrikethrough.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                setBackground(binding.actionStrikethrough);
                mEditor.setStrikeThrough();

                if(!selectedViews.contains("strike")) {
                    selectedViews.add("strike");
                }
            }
        });

        binding.actionUnderline.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                setBackground(binding.actionUnderline);
                mEditor.setUnderline();

                if(!selectedViews.contains("underline")) {
                    selectedViews.add("underline");
                }
            }
        });

        binding.actionHeading1.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onHeadingClicked(v, 1);
                //selectedViews.add(v);
            }
        });

        binding.actionHeading2.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onHeadingClicked(v, 2);
                //selectedViews.add(v);
            }
        });

        binding.actionHeading3.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onHeadingClicked(v, 3);
                //selectedViews.add(v);
            }
        });

        //Curious as to why set on click listener doesn't work on the parent wrapper relative layout
        binding.actionTxtColor.bringToFront();
        binding.actionTxtColor.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                new MaterialColorPickerDialog.Builder(
                        requireActivity(),
                        "Choose color",
                        "Ok",
                        "Cancel",
                        new ColorListener() {
                            @Override
                            public void onColorSelected(int i, @NotNull String s) {
                                mEditor.setTextColor(Color.parseColor(s));
                                binding.actionTxtColorIndicator.setColorFilter(Color.parseColor(s));
                                currentTextColor = s;
                            }
                        },
                        currentTextColor,
                        ColorSwatch._300,
                        ColorShape.SQAURE,
                        palette_colors
                )
                        .build()
                        .show();
            }
        });

        binding.actionBgColor.bringToFront();
        binding.actionBgColor.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                new MaterialColorPickerDialog.Builder(
                        requireActivity(),
                        "Choose color",
                        "Ok",
                        "Cancel",
                        new ColorListener() {
                            @Override
                            public void onColorSelected(int i, @NotNull String s) {
                                mEditor.setTextBackgroundColor(Color.parseColor(s));
                                binding.actionBgColorIndicator.setColorFilter(Color.parseColor(s));
                                currentBgColor = s;
                            }
                        },
                        currentBgColor,
                        ColorSwatch._300,
                        ColorShape.SQAURE,
                        palette_colors
                )
                        .build()
                        .show();
            }
        });

        binding.actionIndent.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setIndent();
            }
        });

        binding.actionOutdent.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setOutdent();
            }
        });

        binding.actionAlignLeft.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setAlignLeft();
            }
        });

        binding.actionAlignCenter.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setAlignCenter();
            }
        });

        binding.actionAlignRight.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setAlignRight();
            }
        });

        binding.actionInsertBullets.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setBullets();
            }
        });

        binding.actionInsertNumbers.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setNumbers();
            }
        });

        binding.actionInsertImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //getActivity().dispatchKeyEvent(KeyC);

//                mEditor.checkIfStart((String s) -> {
//                    Log.d("isstart", mEditor.getHtml());
//                    if(s.equals("true")){
//                        for(String viewName : selectedViews){
//                            switch (viewName) {
//                                case "bold":
//                                    binding.actionBold.performClick();
//                                    break;
//                                case "italic":
//                                    binding.actionItalic.performClick();
//                                    break;
//                                case "strike":
//                                    binding.actionStrikethrough.performClick();
//                                    break;
//                                case "underline":
//                                    binding.actionUnderline.performClick();
//                                    break;
//                            }
//                        }
//                        selectedViews.clear();
//                        Log.d("isstart", "It's the start");
//                    }else{
//                        Log.d("isstart", "It's not the start");
//                    }

//                });
                //clearAllBackgrounds();
                new ImagePickerCallback(requireActivity(), REQUEST_IMAGE, viewModel, SHOW_ALL_OPTIONS).onImagePickerClicked();
                viewModel.getImagePath().observe(getViewLifecycleOwner(), new Observer<Uri>() {
                    @Override
                    public void onChanged(Uri uri) {
                        if(uri != null){
                            Log.d("urivalue", uri.toString());
                            Uri fileUri = uri;
                            String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                            int randomNumber = new Random().nextInt();
                            String fileName = Integer.toString(randomNumber);

                            StorageReference fileToUpload = storageReference.child("Images").child(currentUserId).child(fileName); // randomize name

                            fileToUpload.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    storageReference.child("Images").child(currentUserId).child(fileName).getDownloadUrl().addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            mEditor.insertImage(task.getResult().toString(), task.getResult().toString());


                                            Log.d("newmethod", task.getResult().toString());
                                        }
                                    });

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(requireActivity(), "Upload image failed", Toast.LENGTH_LONG).show();
                                }
                            });

                            viewModel.setResultOk(null);

                        }
                    }
                });

                viewModel.getVideoPath().observe(getViewLifecycleOwner(), new Observer<Uri>() {
                    @Override
                    public void onChanged(Uri uri) {
                        if(uri != null){
                            Log.d("urivalue", uri.toString());
                            Uri fileUri = uri;
                            String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                            int randomNumber = new Random().nextInt();
                            String fileName = Integer.toString(randomNumber);

                            StorageReference fileToUpload = storageReference.child("Images").child(currentUserId).child(fileName); // randomize name

                            fileToUpload.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    storageReference.child("Images").child(currentUserId).child(fileName).getDownloadUrl().addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            mEditor.insertVideo(task.getResult().toString(), task.getResult().toString());
//                                            clearAllBackgrounds();
                                            Log.d("newmethod", task.getResult().toString());
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(requireActivity(), "Upload image failed", Toast.LENGTH_LONG).show();
                                }
                            });

                            viewModel.setVideoResultOk(null);
                        }
                    }
                });
            }
        });

//        binding.actionInsertOnlineImage.setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
//                builder.setTitle("Insert Image");
//                final EditText input = new EditText(requireContext());
//                input.setHint("Image Link");
//                builder.setView(input);
//                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        String m_Text = input.getText().toString();
//                        mEditor.insertImage(m_Text,
//                                m_Text);
//                        clearAllBackgrounds();
//                    }
//                });
//                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//
//                builder.show();
//            }
//        });

//        binding.actionInsertVideo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new ImagePickerCallback(requireActivity(), REQUEST_IMAGE, viewModel).onImagePickerClicked();
//                viewModel.getVideoPath().observe(getViewLifecycleOwner(), new Observer<Uri>() {
//                    @Override
//                    public void onChanged(Uri uri) {
//                        if(uri != null){
//                            Log.d("urivalue", uri.toString());
//                            Uri fileUri = uri;
//                            String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//
//                            int randomNumber = new Random().nextInt();
//                            String fileName = Integer.toString(randomNumber);
//
//                            StorageReference fileToUpload = storageReference.child("Images").child(currentUserId).child(fileName); // randomize name
//
//                            fileToUpload.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                @Override
//                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                    storageReference.child("Images").child(currentUserId).child(fileName).getDownloadUrl().addOnCompleteListener(task -> {
//                                        if (task.isSuccessful()) {
//                                            mEditor.insertVideo(task.getResult().toString(), task.getResult().toString());
//                                            clearAllBackgrounds();
//                                            Log.d("newmethod", task.getResult().toString());
//                                        }
//                                    });
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Toast.makeText(requireActivity(), "Upload image failed", Toast.LENGTH_LONG).show();
//                                }
//                            });
//
//                            viewModel.setVideoResultOk(null);
//                        }
//                    }
//                });
//            }
//        });

        binding.actionInsertLink.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                //you should edit this to fit your needs
                builder.setTitle("Insert Link");

                final EditText one = new EditText(requireContext());
                final EditText two = new EditText(requireContext());

                one.setHint("Text");
                two.setHint("URL");

                LinearLayout lay = new LinearLayout(requireContext());
                lay.setOrientation(LinearLayout.VERTICAL);
                lay.addView(one);
                lay.addView(two);
                builder.setView(lay);

                // Set up the buttons
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //get the two inputs
                        String i = one.getText().toString();
                        String j = two.getText().toString();
                        mEditor.insertLink(j,i);
//                        clearAllBackgrounds();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });
                builder.show();

            }
        });
    }

}
