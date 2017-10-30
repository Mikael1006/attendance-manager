package com.gmail.moreau1006.mikael.attendancemanager.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import com.gmail.moreau1006.mikael.attendancemanager.Adapter.SelectPlayerAdapter;
import com.gmail.moreau1006.mikael.attendancemanager.DAO.PlayersDAO;
import com.gmail.moreau1006.mikael.attendancemanager.Model.Match;
import com.gmail.moreau1006.mikael.attendancemanager.Model.Player;
import com.gmail.moreau1006.mikael.attendancemanager.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AddInvitedPlayerActivity extends AppCompatActivity {

    public static final int ADD_INVITED_PLAYERS_RESQUEST_CODE = 1005;
    public static final String EXTRA_INVITED_PLAYERS = "com.gmail.moreau1006.mikael.attendancemanager.INVITED_PLAYERS";

    private ListView playersListView;
    private PlayersDAO playersDAO;
    private List<Player> players;
    private Match match;
    private ArrayList<Player> invitedPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_invited_player);
        setContentView(R.layout.activity_select_players);

        playersListView = (ListView) findViewById(R.id.SelectPlayersListView);

        playersDAO = new PlayersDAO(this);
        playersDAO.open();

        // get match from previous activity
        match = (Match) getIntent().getSerializableExtra(ListMatchsActivity.EXTRA_MATCH);

        // On récupère tous les joueurs de l'équipe qui ne sont pas invité au match
        players = playersDAO.getNotInvitedPlayersInMatch(match);

        SelectPlayerAdapter adapter = new SelectPlayerAdapter(AddInvitedPlayerActivity.this, players);
        playersListView.setAdapter(adapter);

        invitedPlayers = new ArrayList<Player>();
    }

    public void checkPlayer(View view) {
        // on récupère l'index de la liste
        int index = playersListView.indexOfChild((View) view.getParent());

        // On récupère le bon joueur
        Player player = players.get(index);

        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        if (checked){
            invitedPlayers.add(player);
        }else{
            invitedPlayers.remove(player);
        }
    }

    public void validateSelectPlayers(View view){

        Intent intent = new Intent(AddInvitedPlayerActivity.this, WriteSmsForNewInvitedPlayersActivity.class);
        intent.putExtra(AddInvitedPlayerActivity.EXTRA_INVITED_PLAYERS, invitedPlayers);
        intent.putExtra(ListMatchsActivity.EXTRA_MATCH,match);
        startActivityForResult(intent, AddInvitedPlayerActivity.ADD_INVITED_PLAYERS_RESQUEST_CODE);
    }

    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if(requestCode==AddInvitedPlayerActivity.ADD_INVITED_PLAYERS_RESQUEST_CODE){
            if(resultCode==RESULT_OK){
                setResult(RESULT_OK);
                finish();
            }
        }
        super.onActivityResult (requestCode, resultCode, data);
    }
}
