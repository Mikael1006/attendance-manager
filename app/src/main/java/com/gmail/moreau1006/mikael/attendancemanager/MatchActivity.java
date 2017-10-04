package com.gmail.moreau1006.mikael.attendancemanager;

import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MatchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
    }

    public void ValidateMatch(View view) {

    }

    public void selectTeam(View view) {
        Intent intent = new Intent(this, SelectTeamActivity.class);
        startActivity(intent);
    }
}
