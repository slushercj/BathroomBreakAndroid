package com.slusher.android.bathroombreak;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class AccountCreationActivity extends AppCompatActivity {
    private Button createAccountButton;
    private ImageButton backButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_creation);

        initializeFonts();

        createAccountButton = findViewById(R.id.button_sign_in);
        backButton = findViewById(R.id.button_back);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(AccountCreationActivity.this, "Account creation clicked", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    private void initializeFonts() {
        // initialize the font manager
        Typeface iconFont = FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME);
        FontManager.setIcon(findViewById(R.id.icon_name), iconFont);
        FontManager.setIcon(findViewById(R.id.icon_email), iconFont);
        FontManager.setIcon(findViewById(R.id.icon_password), iconFont);
        FontManager.setIcon(findViewById(R.id.icon_confirm_password), iconFont);
        FontManager.setIcon(findViewById(R.id.button_back), iconFont);
        FontManager.setIcon(findViewById(R.id.button_camera), iconFont);
    }
}
