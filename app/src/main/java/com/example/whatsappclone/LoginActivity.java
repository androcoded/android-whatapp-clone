package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtLoginEmail,edtLoginPassword;
    private Button btnLogin,btnSignUpActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtLoginEmail = findViewById(R.id.edtLoginEmail);
        edtLoginPassword = findViewById(R.id.edtLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUpActivity = findViewById(R.id.btnSignUpActivity);
        btnLogin.setOnClickListener(this);
        btnSignUpActivity.setOnClickListener(this);
        if (ParseUser.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),WhatsappUser.class));
            finish();
        }else {
            ParseUser.logOut();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin:
                loginInUserInParse();
                break;
            case R.id.btnSignUpActivity:
                startActivity(new Intent(this,SignUpActivity.class));
                break;
        }
    }

    private void loginInUserInParse() {
        if (edtLoginEmail.getText().toString().equals("")|| edtLoginPassword.getText().toString().equals("")){
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
        }else {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Signing...");
            progressDialog.show();
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo("email",edtLoginEmail.getText().toString());
            query.getFirstInBackground(new GetCallback<ParseUser>() {
                @Override
                public void done(final ParseUser object, ParseException e) {
                    if (object!=null && e == null){
                        ParseUser.logInInBackground(object.getUsername(), edtLoginPassword.getText().toString(), new LogInCallback() {
                            @Override
                            public void done(ParseUser user, ParseException e) {
                                if (user!=null && e == null){
                                    Toast.makeText(LoginActivity.this, user.getUsername()+" successfully logged in", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(),WhatsappUser.class);
                                    intent.putExtra("currentUser",object.getUsername().toString());
                                    startActivity(intent);
                                    finish();
                                }else{
                                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else{
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                }
            });    
        }
        
    }

}
