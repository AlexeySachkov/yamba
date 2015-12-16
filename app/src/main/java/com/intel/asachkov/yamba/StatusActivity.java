package com.intel.asachkov.yamba;

import android.app.NotificationManager;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class StatusActivity extends AppCompatActivity implements TextWatcher {

    private Button mPostStatusButton;
    private EditText mStatusEditText;
    private TextView mCharactersLeftTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        mPostStatusButton = (Button) findViewById(R.id.postStatusButton);
        mStatusEditText = (EditText) findViewById(R.id.statusEditText);
        mCharactersLeftTextView = (TextView) findViewById(R.id.charactersLeftTextView);

        mStatusEditText.addTextChangedListener(this);
        mStatusEditText.setText(getIntent().getStringExtra(StatusUpdateService.EXTRA_MESSAGE));
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        int charactersLeft = 140 - s.length();

        mCharactersLeftTextView.setText(String.valueOf(charactersLeft));

        if (charactersLeft < 0) {
            mCharactersLeftTextView.setTextColor(getResources().getColor(R.color.colorAccent));
        } else {
            mCharactersLeftTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    public void postStatusButtonOnClick(View view) {
        String status = mStatusEditText.getText().toString();

        if (status.length() <= 140) {

            Toast.makeText(this, "Try to send message: " + status, Toast.LENGTH_SHORT).show();

            Intent postStatus = new Intent(this, StatusUpdateService.class);
            postStatus.putExtra(StatusUpdateService.EXTRA_MESSAGE, status);

            startService(postStatus);

            this.finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_status, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                startService(new Intent(this, RefreshService.class));
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        this.finish();
    }
}