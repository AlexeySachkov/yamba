package com.intel.asachkov.yamba;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StatusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
    }

    public void openStarwarsCinemaTicket(View view) {
        Intent openStarwars = new Intent();
        openStarwars.setAction(Intent.ACTION_VIEW);
        openStarwars.setData(Uri.parse("http://www.cinemapark.ru/films/schedule/2699"));
        startActivity(openStarwars);
    }

    public void openSecondActivity(View view) {
        Intent secondActivity = new Intent(this, SecondActivity.class);

        startActivity(secondActivity);
    }
}