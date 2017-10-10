package com.gmail.moreau1006.mikael.attendancemanager.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.gmail.moreau1006.mikael.attendancemanager.Model.Match;
import com.gmail.moreau1006.mikael.attendancemanager.R;

public class MatchActivity extends AppCompatActivity {

    public static final String EXTRA_MATCH = "com.gmail.moreau1006.mikael.attendancemanager.MATCH";

    private Match match;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        match = new Match();
    }

    public void ValidateMatch(View view) {

    }

    public void selectTeam(View view) {
        Intent intent = new Intent(this, SelectTeamActivity.class);
        intent.putExtra(EXTRA_MATCH,match);
        startActivity(intent);
    }
}
