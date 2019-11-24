package com.slusher.android.bathroombreak;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";
    private final int REQUEST_CODE_CREATE_ACCOUNT = 1;
    private static final int REQUEST_FACEBOOK_SIGN_IN = 2;
    private static final int REQUEST_GOOGLE_SIGN_IN = 3;
    private static final String EMAIL = "email";
    private Button accountCreationButton;
    private Button signInButton;
    private LoginButton facebookLoginButton;
    private CallbackManager callbackManager;
    private GoogleSignInClient mGoogleSignInClient;
    private final String googleOauthClientId = "771187473797-icqfl2it4lgsapok8pkm0a0gr1pi3gnh.apps.googleusercontent.com";

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

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        loginGoogle(account);
        loginFacebook();
    }

    private void loginFacebook() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        if(isLoggedIn)
            loadMapsActivity();
    }

    private void loginGoogle(GoogleSignInAccount account) {
        if(account == null)
        {
            return;
        }

        loadMapsActivity();
    }

    private void initializeGoogleLoginButton() {
        SignInButton signInButton = findViewById(R.id.button_sign_in_google);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setColorScheme(SignInButton.COLOR_DARK);
        signInButton.setOnClickListener(this);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void initializeRegularSigninButton() {
        signInButton = findViewById(R.id.button_sign_in);

        signInButton.setOnClickListener(this);
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
        facebookLoginButton = findViewById(R.id.button_sign_in_facebook);
        facebookLoginButton.setPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                loadMapsActivity();
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

    private void loadMapsActivity()
    {
        Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
        startActivity(intent);
    }

    public void onClickRegularLoginButton(View view){
        loadMapsActivity();
    }

    public void onClickFacebookLoginButton(View view) {
        // TODO: Login with facebook
    }

    public void onClickGoogleLoginButton(View view) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, REQUEST_GOOGLE_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {
            case REQUEST_GOOGLE_SIGN_IN:
                // The Task returned from this call is always completed, no need to attach
                // a listener.
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
                break;
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.button_sign_in:
                onClickRegularLoginButton(view);
                break;
            case R.id.button_sign_in_google:
                onClickGoogleLoginButton(view);
                break;
            case R.id.button_sign_in_facebook:
                onClickFacebookLoginButton(view);
                break;
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            loginGoogle(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            loginGoogle(null);
        }
    }
}
