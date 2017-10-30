package com.gmail.moreau1006.mikael.attendancemanager.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.gmail.moreau1006.mikael.attendancemanager.Model.Match;
import com.gmail.moreau1006.mikael.attendancemanager.DAO.PlayersDAO;
import com.gmail.moreau1006.mikael.attendancemanager.R;
import com.gmail.moreau1006.mikael.attendancemanager.Adapter.SelectTeamAdapter;
import com.gmail.moreau1006.mikael.attendancemanager.Model.Team;
import com.gmail.moreau1006.mikael.attendancemanager.DAO.TeamsDAO;

import java.util.List;

public class SelectTeamActivity extends AppCompatActivity {

    private static final int EDIT_TEAM_REQUEST_CODE = 1003;
    private ListView selectTeamListView;
    private List<Team> teams;
    private TeamsDAO teamsDAO;
    private PlayersDAO playersDAO;
    private Match match;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_team);

        selectTeamListView = (ListView) findViewById(R.id.SelectTeamListView);

        match = (Match) getIntent().getSerializableExtra(ListMatchsActivity.EXTRA_MATCH);

        teamsDAO = new TeamsDAO(this);
        teamsDAO.open();

        playersDAO = new PlayersDAO(this);
        playersDAO.open();

        updateListView();
    }

    private void updateListView(){
        // On rempli la liste
        teams = teamsDAO.getAllTeams();
        SelectTeamAdapter adapter = new SelectTeamAdapter(SelectTeamActivity.this, teams);
        selectTeamListView.setAdapter(adapter);
    }

    public void selectTeam(View view){
        //https://developer.android.com/training/basics/firstapp/starting-activity.html

        int firstListItemPosition = selectTeamListView.getFirstVisiblePosition();

        // on récupère l'index de la liste
        int index = selectTeamListView.indexOfChild((View)view.getParent()) + firstListItemPosition;
        // On récupère la bonne équipe de la liste
        Team team = teams.get(index);

        // match == null
        match.setTeam(team);

        Intent intent = new Intent(this, SelectInvitedPlayersActivity.class);
        intent.putExtra(ListMatchsActivity.EXTRA_MATCH,match);
        startActivityForResult(intent, ListMatchsActivity.CREATE_MATCH_RESQUEST_CODE);
    }

    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if(requestCode==ListMatchsActivity.CREATE_MATCH_RESQUEST_CODE){
            if(resultCode==RESULT_OK){
                setResult(RESULT_OK);
                finish();
            }
        }
        if(requestCode==SelectTeamActivity.EDIT_TEAM_REQUEST_CODE){
            updateListView();
        }
        super.onActivityResult (requestCode, resultCode, data);
    }

    public void editTeam(View view){
        Intent intent = new Intent(this, ListTeamsActivity.class);
        startActivityForResult(intent,SelectTeamActivity.EDIT_TEAM_REQUEST_CODE);
    }
}
