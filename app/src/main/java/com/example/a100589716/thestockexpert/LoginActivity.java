package com.example.a100589716.thestockexpert;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View view){
        EditText editText_email = (EditText) findViewById(R.id.email_input);
        EditText editText_password = (EditText) findViewById(R.id.password_input);

        String email = editText_email.getText().toString();
        String password = editText_password.getText().toString();

        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success,
                            //FirebaseUser user = mAuth.getCurrentUser();
                            Intent MainIntent = new Intent(LoginActivity.this, MainActivity.class );
                            startActivity(MainIntent);
                        }
                        if (!task.isSuccessful()) {
                            // If sign in fails

                            try {
                                throw task.getException();
                            }

                            // if user enters wrong email
                            catch (FirebaseAuthInvalidUserException invalidEmail) {
                                Log.d("Login Failed", "onComplete: invalid_email");
                                Toast.makeText(LoginActivity.this, "Email Doesn't Exist", Toast.LENGTH_SHORT).show();


                            }
                            // if user enters wrong password
                            catch (FirebaseAuthInvalidCredentialsException wrongPassword) {
                                Log.d("Login Failed", "onComplete: wrong_password");
                                Toast.makeText(LoginActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();

                            }
                            catch (Exception e) {
                                Log.d("Login Failed", "onComplete: " + e.getMessage());

                            }


                        }
                    }
                });

    }


    public void signup(View view){
        Intent getSignupIntent = new Intent(
                LoginActivity.this,
                SignupActivity.class
        );

        startActivity(getSignupIntent);
    }






}

