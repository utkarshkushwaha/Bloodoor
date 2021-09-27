package com.example.bloodoor;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import io.alterac.blurkit.BlurLayout;

public class SignUp_User extends AppCompatActivity {

    BlurLayout blurLayout;
    CardView signupcard,signincard;
    private EditText date,name,city,address,Email,phoneno;
    AutoCompleteTextView gender,bloodgrp;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1) //this annotation because dropdown menu code requires minimum android jelly bean...
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);     //removes title bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();    //removes action bar
        blurLayout = findViewById(R.id.blurLayout);         //for blurring background
        setContentView(R.layout.activity_sign_up__user);

        setContentView(R.layout.activity_sign_up__user);
        gender = findViewById(R.id.autoCompleteTextView1);
        String []option1 = {"Select", "Male", "Female"};
        ArrayAdapter arrayAdapter1 = new ArrayAdapter(this, R.layout.options_item, option1);
        gender.setText(arrayAdapter1.getItem(0).toString(), false); //to make default value...
        gender.setAdapter(arrayAdapter1);
        
        bloodgrp = findViewById(R.id.autoCompleteTextView);
        String []option = {"Select", "O+", "O-","A+", "A-", "B+", "B-", "AB+", "AB-"};
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.options_item, option);
        bloodgrp.setText(arrayAdapter.getItem(0).toString(), false); //to make default value...
        bloodgrp.setAdapter(arrayAdapter);
        name=findViewById(R.id.enterFullName);
        address=findViewById(R.id.homeAddress);
        phoneno=findViewById(R.id.mobileNumber);
        Email=findViewById(R.id.emailID);
        city=findViewById(R.id.city);


        signupcard = (CardView) findViewById(R.id.signupcard);
        signupcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = name.getText().toString();
                String homeAddress = address.getText().toString();
                String mobileNo = phoneno.getText().toString();
                String email = Email.getText().toString();
                String City=city.getText().toString();
                String Date=date.getText().toString();
                String Bloodgrp=bloodgrp.getText().toString();
                String Gender=gender.getText().toString();
                User user = new User(fullName,homeAddress,mobileNo,email,
                        City,Date,Bloodgrp,Gender);
                if (!phoneno.getText().toString().trim().isEmpty()) {
                    if ((phoneno.getText().toString().trim()).length() == 10) {


                                signupcard.setVisibility(View.INVISIBLE);

                                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                        "+91" + phoneno.getText().toString(),
                                        90,
                                        TimeUnit.SECONDS,
                                        SignUp_User.this,
                                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                            @Override
                                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                                                signupcard.setVisibility(View.INVISIBLE);
                                            }

                                            @Override
                                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                                signupcard.setVisibility(View.INVISIBLE);
                                                Toast.makeText(SignUp_User.this, "Network Error:(", Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onCodeSent(@NonNull String backendotp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                                                signupcard.setVisibility(View.INVISIBLE);
                                                Intent intent = new Intent(getApplicationContext(), verifyotp.class);
                                                User user = new User(fullName,homeAddress,mobileNo,email,
                                                        City,Date,Bloodgrp,Gender);
                                                intent.putExtra("mobile", phoneno.getText().toString());
                                                intent.putExtra("backendotp",backendotp);
                                                intent.putExtra("User",user);
                                                startActivity(intent);
                                            }
                                        }
                                );

//                        Intent intent = new Intent(getApplicationContext(),verifyotp.class);
//                        intent.putExtra("mobile",entermobilenumber.getText().toString());
//                        startActivity(intent);

                            } else {
                                Toast.makeText(SignUp_User.this, "Please enter correct number", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SignUp_User.this, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
                        }
            }
        });

        signincard = (CardView) findViewById(R.id.signincard);
        signincard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),enternumber.class);
                startActivity(intent);
            }
        });

        Calendar cal= Calendar.getInstance();
        date = (EditText) findViewById(R.id.dateOfBirth);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog=new DatePickerDialog(SignUp_User.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month=month+1;
                        String d=dayOfMonth+"-"+month+"-"+ year;
                        date.setText(d);
                    }
                },year,month,day);
                //Disables past date
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
                //Show date picker dialog
                datePickerDialog.show();
            }
        });
    }

    //FUnctions for making background blur
    @Override
    protected void onStart() {
        super.onStart();
        blurLayout = findViewById(R.id.blurLayout);
        blurLayout.startBlur();
    }

    //FUnctions for making background blur
    @Override
    protected void onStop() {
        blurLayout.pauseBlur();
        super.onStop();
    }
}
