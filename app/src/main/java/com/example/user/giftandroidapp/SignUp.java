package com.example.user.giftandroidapp;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.user.giftandroidapp.Common.Common;
import com.example.user.giftandroidapp.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignUp extends AppCompatActivity {
    MaterialEditText edtPhone,edtName,edtPassword;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtName = (MaterialEditText)findViewById(R.id.edtName);
        edtPassword = (MaterialEditText)findViewById(R.id.edtPassword);
        edtPhone=(MaterialEditText)findViewById(R.id.edtPhone);

        btnSignUp = (Button)findViewById(R.id.btnSignUp);


        //init firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Common.isConnectedToInternet(getBaseContext())) {
                    final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                    mDialog.setMessage("please wait....");
                    mDialog.show();

                    table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            //check that phone number has 10 characters
                            if (!validate()) {

                                //check if already user phone exists
                                if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                                    mDialog.dismiss();
                                    Toast.makeText(SignUp.this, "Phone number already registered", Toast.LENGTH_SHORT).show();
                                }

                                else {
                                   
                                        mDialog.dismiss();
                                        User user = new User(edtName.getText().toString(), edtPassword.getText().toString());
                                        table_user.child(edtPhone.getText().toString()).setValue(user);
                                        Toast.makeText(SignUp.this, "Sign Up successfully", Toast.LENGTH_SHORT).show();
                                        finish();

                                }
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else{

                    Toast.makeText(SignUp.this,"Please check your internet connection !!!",Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });
    }

    private boolean validate() {
        if (edtPhone.getText().toString().trim().length() <= 0) {
            Toast.makeText(SignUp.this, "Please enter the phone number", Toast.LENGTH_SHORT).show();
            return true;
        }

       else if (edtPhone.getText().length() != 10) {
            Toast.makeText(SignUp.this, "Phone number should have 10 numbers", Toast.LENGTH_SHORT).show();
            return true;

        }
       else if (edtName.getText().toString().trim().length() <= 0) {
            Toast.makeText(SignUp.this, "Please enter the name", Toast.LENGTH_SHORT).show();
            return true;
        }

        else if (edtPassword.getText().toString().trim().length() <= 0) {
            Toast.makeText(SignUp.this, "Please enter the password", Toast.LENGTH_SHORT).show();
            return true;
        }






        return false;
    }

}
