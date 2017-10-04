package com.gmail.moreau1006.mikael.attendancemanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

public class ListMatchsActivity extends AppCompatActivity {

    private ListView matchsListView;
    private MatchsDAO matchsDAO;
    private List<Match> matchs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_matchs);

        matchsListView = (ListView) findViewById(R.id.MatchsListView);

        matchsDAO = new MatchsDAO(this);
        matchsDAO.open();

        matchs = matchsDAO.getAllMatchs();
        //matchs = matchsDAO.getAllFutureMatchs();

        MatchAdapter adapter = new MatchAdapter(ListMatchsActivity.this, matchs);
        matchsListView.setAdapter(adapter);
    }
}
