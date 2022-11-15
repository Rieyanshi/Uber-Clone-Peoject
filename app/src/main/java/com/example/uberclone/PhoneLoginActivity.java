package com.example.uberclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.CredentialsApi;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.text.BreakIterator;
import java.util.concurrent.TimeUnit;

public class PhoneLoginActivity extends AppCompatActivity
{
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResentToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private FirebaseAuth mAuth;
    private PinView firstPinView;
    private static final int CREDENTIAL_PICKER_REQUEST =120 ;
    private CountryCodePicker ccp;
    private EditText phoneEditText;
    private String selected_country_code = "+91";
    private ProgressBar progressBar;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);
        button = (Button) findViewById(R.id.button3);
        firstPinView = (PinView) findViewById(R.id.firstPinView);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        phoneEditText = (EditText) findViewById(R.id.editTextPhone);
        mAuth = FirebaseAuth.getInstance();
        //-------------------COUNTRY CODE PICKER----------------
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                selected_country_code = ccp.getSelectedCountryCodeWithPlus();
            }
        });

        //----------------------PHONE TEXT WATCHER--------------------------------------
        phoneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().length() == 10)
                {
                    sendOtp();
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        //----------------------PHONE TEXT WATCHER-----------------------------------------
        //----------------------PIN VIEW TEXT WATCHER--------------------------------------
        firstPinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().length()==6)
                {
                    progressBar.setVisibility(View.VISIBLE);
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(mVerificationId, firstPinView.getText().toString().trim());
                    signInWithAuthCredenials(phoneAuthCredential);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        //----------------------PIN VIEW TEXT WATCHER--------------------------------------
        try {
            HintRequest hintRequest = new HintRequest.Builder()
                    .setPhoneNumberIdentifierSupported(true)
                    .build();
            PendingIntent intent = Credentials.getClient(PhoneLoginActivity.this).getHintPickerIntent(hintRequest);
            try {
                startIntentSenderForResult(intent.getIntentSender(), CREDENTIAL_PICKER_REQUEST, null, 0, 0, 0, new Bundle());
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        //----------otp callbacks--------------------------------
        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential)
            {
                String code = phoneAuthCredential.getSmsCode();
                if(code != null)
                {
                    firstPinView.setText(code);
                    signInWithAuthCredenials(phoneAuthCredential);

                }
            }
            @Override
            public void onVerificationFailed(@NonNull FirebaseException e)
            {
                Toast.makeText(PhoneLoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
                ccp.setVisibility(View.VISIBLE);
                phoneEditText.setVisibility(View.VISIBLE);
                firstPinView.setVisibility(View.VISIBLE);
            }
            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken)
            {
                super.onCodeSent(verificationId, forceResendingToken);
                mVerificationId = verificationId;
                mResentToken = forceResendingToken;
                Toast.makeText(PhoneLoginActivity.this, "6 digit OTP sent", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                ccp.setVisibility(View.GONE);
                phoneEditText.setVisibility(View.GONE);
                firstPinView.setVisibility(View.VISIBLE);
            }
        };
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(PhoneLoginActivity.this, WebView1.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void sendOtp()
    {
        progressBar.setVisibility(View.VISIBLE);
        String phoneNumber = selected_country_code + phoneEditText.getText().toString();
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setPhoneNumber(phoneNumber)
                .setActivity(PhoneLoginActivity.this)
                .setCallbacks(callbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data)
        {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == CREDENTIAL_PICKER_REQUEST && resultCode == RESULT_OK)
            {
                Credential credentials = data.getParcelableExtra(Credential.EXTRA_KEY);
                phoneEditText.setText(credentials.getId().substring(3));
            }
            else if (requestCode == CREDENTIAL_PICKER_REQUEST && resultCode == CredentialsApi.ACTIVITY_RESULT_NO_HINTS_AVAILABLE)
            {
                // *** No phone numbers available ***
                Toast.makeText(PhoneLoginActivity.this, "No phone numbers found", Toast.LENGTH_LONG).show();
            }
    }
    private void signInWithAuthCredenials(PhoneAuthCredential phoneAuthCredential)
    {
        mAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(PhoneLoginActivity.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(PhoneLoginActivity.this, WebView1.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(PhoneLoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(PhoneLoginActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }
}