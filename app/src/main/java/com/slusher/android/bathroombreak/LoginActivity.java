package com.slusher.android.bathroombreak;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private final int REQUEST_CODE_CREATE_ACCOUNT = 1;
    private Button accountCreationButton;
    private Button signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeFonts();

        // initialize the buttons
        accountCreationButton = findViewById(R.id.button_create_account);
        signInButton = findViewById(R.id.button_create);

        // set listeners
        accountCreationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, AccountCreationActivity.class);
                startActivity(intent);
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(LoginActivity.this, "Sign-in clicked", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    private void initializeFonts() {
        // initialize the font manager
        Typeface iconFont = FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(findViewById(R.id.linear_layout_enter_email), iconFont);
        FontManager.markAsIconContainer(findViewById(R.id.linear_layout_enter_password), iconFont);
    }
}
