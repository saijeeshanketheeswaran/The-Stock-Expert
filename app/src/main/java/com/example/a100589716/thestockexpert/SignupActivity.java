package com.example.a100589716.thestockexpert;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class SignupActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Locale[] locales = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();

        for (Locale locale : locales) {
            String country = locale.getDisplayCountry();
            if (country.trim().length() > 0 && !countries.contains(country)) {
                countries.add(country);
            }
        }
        Collections.sort(countries);

        Spinner spinner = (Spinner) findViewById(R.id.country_spinner);
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, countries);

        spinner.setAdapter(countryAdapter);

        /*Spinner spinner1 = (Spinner) findViewById(R.id.stock_spinner);
        List<String> stocks = new ArrayList<String>();
        String file = "EURONEXT_metadata.csv";
        BufferedReader br = null;
        String line = " ";
        String sp = ",";

        String [] name;
        String [] code;
        try{
            br = new BufferedReader(new FileReader(file));
            while((line = br.readLine()) != null)
            {
                String [] temp = line.split(sp);
                stocks.add(temp[1]);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        ArrayAdapter<String> stockAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, stocks);

        spinner.setAdapter(stockAdapter);*/



    }

    public void signup(View view){
        EditText editText_email = (EditText) findViewById(R.id.email_input);
        EditText editText_password = (EditText) findViewById(R.id.password_input);

        final String email = editText_email.getText().toString();
        String password = editText_password.getText().toString();





        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            String country = ((Spinner)findViewById(R.id.country_spinner)).getSelectedItem().toString();
                            DatabaseReference mRef =  database.getReference().child("Users").child(encodeUserEmail(email));
                            mRef.child("Country").setValue(country);

                            Log.d("Sign up", "createUserWithEmail:success");
                            Toast.makeText(getApplicationContext(), "Created Account", Toast.LENGTH_SHORT).show();
                            Intent LoginIntent = new Intent(SignupActivity.this, LoginActivity.class );
                            startActivity(LoginIntent);
                        }

                        if (!task.isSuccessful()) {
                            try {
                                throw task.getException();
                            }
                            catch (FirebaseAuthUserCollisionException existEmail) {
                                Toast.makeText(SignupActivity.this, "Email Already Exists.",
                                        Toast.LENGTH_SHORT).show();
                                Log.d("Email", "onComplete: exist_email");


                            }
                            catch(FirebaseAuthWeakPasswordException weakPassword){
                                Toast.makeText(SignupActivity.this, "Password must be atleast 6 characters long",
                                        Toast.LENGTH_SHORT).show();
                            }

                            catch (Exception e) {
                                Log.d("Exception", "onComplete: " + e.getMessage());
                            }

                        }


                    }
                });


    }

    static String encodeUserEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }

    static String decodeUserEmail(String userEmail) {
        return userEmail.replace(",", ".");
    }
}
