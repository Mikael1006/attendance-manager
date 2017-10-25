package com.gmail.moreau1006.mikael.attendancemanager.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.gmail.moreau1006.mikael.attendancemanager.Adapter.InvitedPlayersAdapter;
import com.gmail.moreau1006.mikael.attendancemanager.DAO.PlayersDAO;
import com.gmail.moreau1006.mikael.attendancemanager.Model.Match;
import com.gmail.moreau1006.mikael.attendancemanager.Model.Player;
import com.gmail.moreau1006.mikael.attendancemanager.R;

import java.util.List;

public class MatchActivity extends AppCompatActivity {

    private ListView invitedPlayersListView;
    private List<Player> invitedPlayers;
    private Match match;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        invitedPlayersListView = (ListView) findViewById(R.id.invitedPlayersListView);

        // get match from previous activity
        match = (Match) getIntent().getSerializableExtra(ListMatchsActivity.EXTRA_MATCH);

        invitedPlayers = match.getInvitedPlayers();

        InvitedPlayersAdapter adapter = new InvitedPlayersAdapter(MatchActivity.this, invitedPlayers);
        invitedPlayersListView.setAdapter(adapter);
    }
}
