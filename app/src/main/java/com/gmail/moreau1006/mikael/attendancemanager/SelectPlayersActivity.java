package com.gmail.moreau1006.mikael.attendancemanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class SelectPlayersActivity extends AppCompatActivity {

    private ListView playersListView;
    private PlayersDAO playersDAO;
    private Team team;
    private List<Player> players;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_players);

        playersListView = (ListView) findViewById(R.id.SelectPlayersListView);

        playersDAO = new PlayersDAO(this);
        playersDAO.open();

        // On récupère l'équipe de la liste
        team = (Team) getIntent().getSerializableExtra(SelectTeamActivity.EXTRA_TEAM);

        // On récupère tous les joueurs de l'équipe
        players = playersDAO.getPlayersByTeam(team);

        SelectPlayerAdapter adapter = new SelectPlayerAdapter(SelectPlayersActivity.this, players);
        playersListView.setAdapter(adapter);
    }
}
