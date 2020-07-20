package com.example.user.giftandroidapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.giftandroidapp.Common.Common;
import com.example.user.giftandroidapp.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignIn extends AppCompatActivity {

    EditText edtPhone,edtPassword;
    Button btnSignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        edtPassword = (MaterialEditText)findViewById(R.id.edtPassword);
        edtPhone = (MaterialEditText)findViewById(R.id.edtPhone);
        btnSignIn = (Button)findViewById(R.id.btnSignIn);


        //init firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Common.isConnectedToInternet(getBaseContext())) {

                    final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                    mDialog.setMessage("please wait....");
                    mDialog.show();

                    table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (!validate())
                            {
                                //Check if user not exist in database
                                if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {

                                    //get user information
                                    mDialog.dismiss();

                                    User user = dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class);
                                    user.setPhone(edtPhone.getText().toString());//set Phone
                                    if (user.getPassword().equals(edtPassword.getText().toString())) {
                                        Intent homeIntent = new Intent(SignIn.this, Home.class);
                                        Common.currentUser = user;
                                        startActivity(homeIntent);
                                        finish();

                                    } else {
                                        Toast.makeText(SignIn.this, "wrong password !!!", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    mDialog.dismiss();
                                    Toast.makeText(SignIn.this, "user not exists in the database", Toast.LENGTH_SHORT).show();
                                }
                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else
                {
                    Toast.makeText(SignIn.this,"Please check your internet connection !!!",Toast.LENGTH_SHORT).show();
                    return;
                }
            }

        });

    }

    private boolean validate() {
        if (edtPhone.getText().toString().trim().length() <= 0) {
            Toast.makeText(SignIn.this, "Please enter the phone number", Toast.LENGTH_SHORT).show();
            return true;
        } else if (edtPhone.getText().length() != 10) {
            Toast.makeText(SignIn.this, "Phone number should have 10 numbers", Toast.LENGTH_SHORT).show();
            return true;

        } else if (edtPassword.getText().toString().trim().length() <= 0) {
            Toast.makeText(SignIn.this, "Please enter the password", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}
