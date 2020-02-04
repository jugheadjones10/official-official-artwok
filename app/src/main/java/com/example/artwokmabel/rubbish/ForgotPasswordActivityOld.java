package com.example.artwokmabel.rubbish;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.artwokmabel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivityOld extends AppCompatActivity {

    private EditText emailPass;
    private Button resetPass;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.z_activity_forgotpassword);

        emailPass = findViewById((R.id.etEmailPass));
        resetPass = findViewById(R.id.btnResetPass);
        firebaseAuth = FirebaseAuth.getInstance();

        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String useremail = emailPass.getText().toString().trim();

                if(useremail.equals("")) {
                    AlertDialog alertDialog = new AlertDialog.Builder(ForgotPasswordActivityOld.this).create();
                    alertDialog.setTitle("Unsuccessful");
                    alertDialog.setMessage("Please enter a valid email address.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                } else {
                    firebaseAuth.sendPasswordResetEmail(useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                AlertDialog alertDialog = new AlertDialog.Builder(ForgotPasswordActivityOld.this).create();
                                alertDialog.setTitle("Successful");
                                alertDialog.setMessage("A link has been sent to your email! You can now reset your password.");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();
//                                finish();
//                                startActivity(new Intent(ForgotPasswordActivityOld.this, LaunchPageActivity.class));

                            } else {
                                AlertDialog alertDialog = new AlertDialog.Builder(ForgotPasswordActivityOld.this).create();
                                alertDialog.setTitle("Unsuccessful");
                                alertDialog.setMessage("Please enter a valid email address.");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();
                            }
                        }
                    });
                }
            }
        });

    }
    // sliding animation effect when back key pressed
    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.fromleft, R.anim.toright);
    }
}