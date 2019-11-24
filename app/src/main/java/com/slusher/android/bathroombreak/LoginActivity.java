package com.slusher.android.bathroombreak;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.util.Arrays;


public class LoginActivity extends AppCompatActivity {
    private final int REQUEST_CODE_CREATE_ACCOUNT = 1;
    private static final String EMAIL = "email";
    private Button accountCreationButton;
    private Button signInButton;
    private LoginButton facebookLoginButton;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeFonts();

        // initialize the buttons
        callbackManager = CallbackManager.Factory.create();

        initializeRegularSigninButton();
        initializeFacebookLoginButton();
        initializeGoogleLoginButton();
        initializeAccountCreationButton();

        // set listeners

    }

    private void initializeGoogleLoginButton() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
    }

    private void initializeRegularSigninButton() {
        signInButton = findViewById(R.id.button_sign_in);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initializeAccountCreationButton() {
        accountCreationButton = findViewById(R.id.button_create_account);

        accountCreationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, AccountCreationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initializeFacebookLoginButton() {
        facebookLoginButton = findViewById(R.id.login_button);
        facebookLoginButton.setPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
                startActivity(intent);
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }

    private void initializeFonts() {
        // initialize the font manager
        Typeface iconFont = FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(findViewById(R.id.linear_layout_enter_email), iconFont);
        FontManager.markAsIconContainer(findViewById(R.id.linear_layout_enter_password), iconFont);
    }

    public void onClickFacebookLoginButton(View view) {
        // TODO: Login with facebook
    }


    public void onClickGoogleLoginButton(View view) {
        // TODO: Login with google
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
