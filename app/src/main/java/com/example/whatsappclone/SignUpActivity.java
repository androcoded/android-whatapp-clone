package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtSignUpEmail,edtSignUpUsername,edtSignUpPassword;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        edtSignUpEmail = findViewById(R.id.edtSignUpEmail);
        edtSignUpUsername = findViewById(R.id.edtSignUpUsername);
        edtSignUpPassword = findViewById(R.id.edtSignUpPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        ParseUser user = new ParseUser();
        if (edtSignUpEmail.getText().toString().equals("")
                ||edtSignUpUsername.getText().toString().equals("")
                ||edtSignUpPassword.getText().toString().equals("")){
            Toast.makeText(this, "All fields must be required!", Toast.LENGTH_SHORT).show();
        }else {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Signing up...");
            progressDialog.show();
            user.setEmail(edtSignUpEmail.getText().toString().toLowerCase());
            user.setUsername(edtSignUpUsername.getText().toString());
            user.setPassword(edtSignUpPassword.getText().toString());
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e ==null){
                        Toast.makeText(SignUpActivity.this, edtSignUpUsername.getText().toString()
                                +" is successfully signed up", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
                        finish();
                    }else{
                        Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                }
            });
        }
    }
}
