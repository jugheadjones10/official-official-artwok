//package com.example.artwokmabel.homepage.request.upload;
//
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.graphics.Color;
//import android.net.Uri;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.databinding.DataBindingUtil;
//import androidx.fragment.app.Fragment;
//import androidx.lifecycle.Observer;
//import androidx.lifecycle.ViewModelProviders;
//import androidx.navigation.Navigation;
//
//import com.example.artwokmabel.R;
//import com.example.artwokmabel.databinding.FragmentUploadRequestDescBinding;
//import com.example.artwokmabel.homepage.callbacks.ImagePickerCallback;
//import com.example.artwokmabel.profile.uploadlisting.UploadListingDescViewModel;
//import com.example.artwokmabel.profile.uploadlisting.UploadListingViewModel;
//import com.example.artwokmabel.profile.uploadpost.RichEditor;
//import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog;
//import com.github.dhaval2404.colorpicker.listener.ColorListener;
//import com.github.dhaval2404.colorpicker.model.ColorShape;
//import com.github.dhaval2404.colorpicker.model.ColorSwatch;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;
//
//import org.jetbrains.annotations.NotNull;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Random;
//
//import static com.example.artwokmabel.profile.utils.ImagePickerActivity.SHOW_ALL_OPTIONS;
//
//public class UploadRequestDescFragment extends Fragment {
//
//    private FragmentUploadRequestDescBinding binding;
//    private static UploadRequestDescFragment instance;
//    public static final int REQUEST_IMAGE = 100;
//    private UploadListingDescViewModel viewModel;
//
//    private int originalMode;
//    private RichEditor mEditor;
//    private String currentTextColor = "#000000";
//    private String currentBgColor = "#000000";
//    private ArrayList<View> headingViews = new ArrayList<>();
//
//    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
//
//    public static UploadRequestDescFragment getInstance(){
//        return instance;
//    }
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_upload_request_desc, container, false);
//        viewModel = ViewModelProviders.of(this).get(UploadListingDescViewModel.class);
//
//        originalMode = getActivity().getWindow().getAttributes().softInputMode;
//        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//
////        binding.newPostDescL.bringToFront();
//        headingViews.add(binding.actionHeading1);
//        headingViews.add(binding.actionHeading2);
//        headingViews.add(binding.actionHeading3);
//
//        instance = this;
//
//        return binding.getRoot();
//    }
//
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        initView();
//    }
//
//    public String getTitle(){
//        return binding.newPostTitleL.getText().toString();
//    }
//
//    public String getDesc(){
//        String htmlContent = binding.editor.getHtml();
//        return htmlContent;
//    }
//
//    //Set grey bg if not, set not if grey bg
//    private void setBackground(View view){
//        if(view.getBackground() == null){
//            view.setBackgroundResource(R.drawable.rounded_corners);
//        }else{
//            view.setBackground(null);
//        }
//    }
//
//    private void onHeadingClicked(View view, int heading){
//        if(view.getBackground() == null){
//            view.setBackgroundResource(R.drawable.rounded_corners);
//
//            for(View headingView : headingViews){
//                if(!headingView.equals(view)){
//                    headingView.setBackground(null);
//                }
//            }
//            mEditor.setHeading(heading);
//        }else{
//            view.setBackground(null);
//            mEditor.unsetHeading();
//        }
//    }
//
//
//    private  void initView(){
//        mEditor = (RichEditor) binding.editor;
//
//        mEditor.setPadding(10, 10, 10, 10);
//        mEditor.setPlaceholder("Insert text here...");
//
//        binding.actionUndo.setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                mEditor.undo();
//            }
//        });
//
//        binding.actionRedo.setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                mEditor.redo();
//            }
//        });
//
//        binding.actionBold.setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                setBackground(binding.actionBold);
//                mEditor.setBold();
//            }
//        });
//
//        binding.actionItalic.setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                setBackground(binding.actionItalic);
//                mEditor.setItalic();
//            }
//        });
//
//        binding.actionStrikethrough.setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                setBackground(binding.actionStrikethrough);
//                mEditor.setStrikeThrough();
//            }
//        });
//
//        binding.actionUnderline.setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                setBackground(binding.actionUnderline);
//                mEditor.setUnderline();
//            }
//        });
//
//        binding.actionHeading1.setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                onHeadingClicked(v, 1);
//            }
//        });
//
//        binding.actionHeading2.setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                onHeadingClicked(v, 2);
//            }
//        });
//
//        binding.actionHeading3.setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                onHeadingClicked(v, 3);
//            }
//        });
//
//        //Curious as to why set on click listener doesn't work on the parent wrapper relative layout
//        binding.actionTxtColor.bringToFront();
//        binding.actionTxtColor.setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                new MaterialColorPickerDialog.Builder(
//                        requireActivity(),
//                        "Choose color",
//                        "Ok",
//                        "Cancel",
//                        new ColorListener() {
//                            @Override
//                            public void onColorSelected(int i, @NotNull String s) {
//                                mEditor.setTextColor(Color.parseColor(s));
//                                binding.actionTxtColorIndicator.setColorFilter(Color.parseColor(s));
//                                currentTextColor = s;
//                            }
//                        },
//                        currentTextColor,
//                        ColorSwatch._300,
//                        ColorShape.SQAURE,
//                        new ArrayList<String>(Arrays.asList("#000000", "#f6e58d", "#ffbe76", "#ff7979", "#badc58", "#dff9fb",
//                                "#7ed6df", "#e056fd", "#686de0", "#30336b", "#95afc0"))
//                )
//                        .build()
//                        .show();
//            }
//        });
//
//        binding.actionBgColor.bringToFront();
//        binding.actionBgColor.setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                new MaterialColorPickerDialog.Builder(
//                        requireActivity(),
//                        "Choose color",
//                        "Ok",
//                        "Cancel",
//                        new ColorListener() {
//                            @Override
//                            public void onColorSelected(int i, @NotNull String s) {
//                                mEditor.setTextBackgroundColor(Color.parseColor(s));
//                                binding.actionBgColorIndicator.setColorFilter(Color.parseColor(s));
//                                currentBgColor = s;
//                            }
//                        },
//                        currentBgColor,
//                        ColorSwatch._300,
//                        ColorShape.SQAURE,
//                        new ArrayList<String>(Arrays.asList("#000000", "#f6e58d", "#ffbe76", "#ff7979", "#badc58", "#dff9fb",
//                                "#7ed6df", "#e056fd", "#686de0", "#30336b", "#95afc0"))
//                )
//                        .build()
//                        .show();
//            }
//        });
//
//        binding.actionIndent.setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                mEditor.setIndent();
//            }
//        });
//
//        binding.actionOutdent.setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                mEditor.setOutdent();
//            }
//        });
//
//        binding.actionAlignLeft.setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                mEditor.setAlignLeft();
//            }
//        });
//
//        binding.actionAlignCenter.setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                mEditor.setAlignCenter();
//            }
//        });
//
//        binding.actionAlignRight.setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                mEditor.setAlignRight();
//            }
//        });
//
//        binding.actionInsertBullets.setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                mEditor.setBullets();
//            }
//        });
//
//        binding.actionInsertNumbers.setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                mEditor.setNumbers();
//            }
//        });
//
//        binding.actionInsertImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new ImagePickerCallback(requireActivity(), REQUEST_IMAGE, viewModel, SHOW_ALL_OPTIONS).onImagePickerClicked();
//                viewModel.getImagePath().observe(getViewLifecycleOwner(), new Observer<Uri>() {
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
//                                            mEditor.insertImage(task.getResult().toString(), task.getResult().toString());
//                                            Log.d("newmethod", task.getResult().toString());
//                                        }
//                                    });
//
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Toast.makeText(requireActivity(), "Upload image failed", Toast.LENGTH_LONG).show();
//                                }
//                            });
//                        }
//                    }
//                });
//            }
//        });
//
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
//
//        binding.actionInsertLink.setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
//                //you should edit this to fit your needs
//                builder.setTitle("Insert Link");
//
//                final EditText one = new EditText(requireContext());
//                final EditText two = new EditText(requireContext());
//
//                one.setHint("Text");
//                two.setHint("URL");
//
//                LinearLayout lay = new LinearLayout(requireContext());
//                lay.setOrientation(LinearLayout.VERTICAL);
//                lay.addView(one);
//                lay.addView(two);
//                builder.setView(lay);
//
//                // Set up the buttons
//                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        //get the two inputs
//                        String i = one.getText().toString();
//                        String j = two.getText().toString();
//                        mEditor.insertLink(j,i);
//
//                    }
//                });
//
//                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        dialog.cancel();
//                    }
//                });
//                builder.show();
//
//
//            }
//        });
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        getActivity().getWindow().setSoftInputMode(originalMode);
//    }
//}
