package com.gmail.moreau1006.mikael.attendancemanager.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.gmail.moreau1006.mikael.attendancemanager.Model.Match;
import com.gmail.moreau1006.mikael.attendancemanager.Adapter.MatchAdapter;
import com.gmail.moreau1006.mikael.attendancemanager.DAO.MatchsDAO;
import com.gmail.moreau1006.mikael.attendancemanager.Model.Player;
import com.gmail.moreau1006.mikael.attendancemanager.R;

import java.util.Collections;
import java.util.List;

public class ListMatchsActivity extends AppCompatActivity {

    public static final String EXTRA_MATCH = "com.gmail.moreau1006.mikael.attendancemanager.MATCH";
    public static final int RESQUEST_CODE = 1000;

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

        try{
            matchs = matchsDAO.getAllMatchs();
        }catch (Exception e){
            e.printStackTrace();
        }


        MatchAdapter adapter = new MatchAdapter(ListMatchsActivity.this, matchs);
        matchsListView.setAdapter(adapter);
    }

    public void createMatch(View view) {
        Match match = new Match();
        Intent intent = new Intent(this, SelectDateMatchActivity.class);
        intent.putExtra(ListMatchsActivity.EXTRA_MATCH,match);
        startActivityForResult(intent, RESQUEST_CODE);
    }

    public void showMatch(View view) {
        //https://developer.android.com/training/basics/firstapp/starting-activity.html

        // on récupère l'index de la liste
        int index = matchsListView.indexOfChild((View)view.getParent());
        // On récupère la bonne équipe de la liste
        Match match = matchs.get(index);

        Intent intent = new Intent(this, MatchActivity.class);
        intent.putExtra(ListMatchsActivity.EXTRA_MATCH,match);
        startActivity(intent);
    }

    public void deleteMatch(View view) {
        //TODO
    }

    // When we have finished to create a match,
    // we update the list of matchs
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if(requestCode==ListMatchsActivity.RESQUEST_CODE){
            if(resultCode==RESULT_OK){
                try{
                    matchs = matchsDAO.getAllMatchs();
                }catch (Exception e){
                    e.printStackTrace();
                }


                MatchAdapter adapter = new MatchAdapter(ListMatchsActivity.this, matchs);
                matchsListView.setAdapter(adapter);
            }
        }
        super.onActivityResult (requestCode, resultCode, data);
    }
}
