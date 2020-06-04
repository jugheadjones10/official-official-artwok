package com.example.artwokmabel.profile.uploadpost;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.artwokmabel.HomePageActivity;
import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.FragmentUploadPostBinding;
import com.example.artwokmabel.repos.FirestoreRepo;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;

public class UploadPostFragment extends Fragment {

    private NavController navController;
    private FragmentUploadPostBinding binding;
    private FirebaseAuth mAuth;
    private int originalMode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_upload_post, container, false);
        binding.setUploadPostFragment(this);
        mAuth = FirebaseAuth.getInstance();

        binding.progressBar.setVisibility(View.GONE);
        originalMode = getActivity().getWindow().getAttributes().softInputMode;
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        initView();
    }

//    public void onPostUploaded(){
//        Intent intent = new Intent(requireContext(), HomePageActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
//    }
//

    public interface OnPostUploadFinished{
        void onPostUploadFinished(boolean isSuccessful);
    }

    public void onPostUpload(){
        binding.progressBar.setVisibility(View.VISIBLE);
        //TODO Should the below database call be allowed or should it be put in a view model?
        String htmlContent = binding.editor.getHtml();

        FirestoreRepo.getInstance().uploadNewPost(htmlContent, mAuth.getCurrentUser().getUid(), (isSuccessful) -> {
            binding.progressBar.setVisibility(View.GONE);
            if(isSuccessful){
                navController.navigateUp();
            }else{
                Toast.makeText(requireActivity(), "Failed to upload post", Toast.LENGTH_LONG)
                        .show();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().getWindow().setSoftInputMode(originalMode);
    }

    private  void initView(){

//        image_view_done_rich=(ImageView) findViewById(R.id.image_view_done_rich);
//        relativeLayout_rich_box=(RelativeLayout) findViewById(R.id.relativeLayout_rich_box);
//        pd = new ProgressDialog(editor.this);
//        pd.setMessage("Uploading Post");
//        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        pd.setCancelable(false);
//        this.edit_text_upload_description =  (RichEditor) findViewById(R.id.editor);

        final RichEditor mEditor = (RichEditor) binding.editor;

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
                mEditor.setBold();
            }
        });

        binding.actionItalic.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setItalic();
            }
        });

        binding.actionSubscript.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setSubscript();
            }
        });

        binding.actionSuperscript.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setSuperscript();
            }
        });

        binding.actionStrikethrough.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setStrikeThrough();
            }
        });

        binding.actionUnderline.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setUnderline();
            }
        });

        binding.actionHeading1.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(1);
            }
        });

        binding.actionHeading2.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(2);
            }
        });

        binding.actionHeading3.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(3);
            }
        });

        binding.actionHeading4.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(4);
            }
        });

        binding.actionHeading5.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(5);
            }
        });

        binding.actionHeading6.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(6);
            }
        });

        binding.actionTxtColor.setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override public void onClick(View v) {
                ColorPickerDialogBuilder
                        .with(requireContext())
                        .setTitle("Choose color")
                        .initialColor(Color.BLACK)
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {
                                mEditor.setTextColor(Color.parseColor("#"+Integer.toHexString(selectedColor)));
                            }
                        })
                        .setPositiveButton("ok", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                mEditor.setTextColor(Color.parseColor("#"+Integer.toHexString(selectedColor)));
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .build()
                        .show();
            }
        });

        binding.actionBgColor.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                ColorPickerDialogBuilder
                        .with(requireContext())
                        .setTitle("Choose color")
                        .initialColor(Color.BLACK)
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {
                                mEditor.setTextBackgroundColor(Color.parseColor("#"+Integer.toHexString(selectedColor)));
                            }
                        })
                        .setPositiveButton("ok", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                mEditor.setTextBackgroundColor(Color.parseColor("#"+Integer.toHexString(selectedColor)));
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
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

        binding.actionBlockquote.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setBlockquote();
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
            @Override public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Insert Image");
                final EditText input = new EditText(requireContext());
                input.setHint("Image Link");
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String m_Text = input.getText().toString();
                        mEditor.insertImage(m_Text,
                                m_Text);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

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