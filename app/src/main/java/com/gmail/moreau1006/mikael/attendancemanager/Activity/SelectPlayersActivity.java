package com.gmail.moreau1006.mikael.attendancemanager.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;

import com.gmail.moreau1006.mikael.attendancemanager.Model.Match;
import com.gmail.moreau1006.mikael.attendancemanager.Model.Player;
import com.gmail.moreau1006.mikael.attendancemanager.DAO.PlayersDAO;
import com.gmail.moreau1006.mikael.attendancemanager.R;
import com.gmail.moreau1006.mikael.attendancemanager.Adapter.SelectPlayerAdapter;
import com.gmail.moreau1006.mikael.attendancemanager.Model.Team;

import java.util.ArrayList;
import java.util.List;

public class SelectPlayersActivity extends AppCompatActivity {

    public static final String EXTRA_SELECTED_PLAYERS = "com.gmail.moreau1006.mikael.attendancemanager.SELECTED_PLAYERS";
    private ListView playersListView;
    private PlayersDAO playersDAO;
    private Team team;
    private List<Player> players;
    private Match match;
    private List<Player> selectedPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_players);

        playersListView = (ListView) findViewById(R.id.SelectPlayersListView);

        playersDAO = new PlayersDAO(this);
        playersDAO.open();

        // get match from previous activity
        match = (Match) getIntent().getSerializableExtra(SelectDateMatchActivity.EXTRA_MATCH);

        // On récupère tous les joueurs de l'équipe
        players = playersDAO.getPlayersByTeam(match.getTeam());

        SelectPlayerAdapter adapter = new SelectPlayerAdapter(SelectPlayersActivity.this, players);
        playersListView.setAdapter(adapter);

        selectedPlayers = match.getPlayers();
        selectedPlayers = new ArrayList<Player>();
    }

    public void checkPlayer(View view) {
       // on récupère l'index de la liste
       int index = playersListView.indexOfChild((View) view.getParent());

        // On récupère le bon joueur
       Player player = players.get(index);

       // Is the view now checked?
       boolean checked = ((CheckBox) view).isChecked();

       if (checked){
           selectedPlayers.add(player);
       }else{
           selectedPlayers.remove(player);
       }
    }
    public void validateSelectPlayers(View view){

        Intent intent = new Intent(SelectPlayersActivity.this, WriteSmsActivity.class);
        intent.putExtra(SelectDateMatchActivity.EXTRA_MATCH,match);
        intent.putExtra(EXTRA_SELECTED_PLAYERS,(ArrayList<Player>) selectedPlayers);
        startActivityForResult(intent, SelectDateMatchActivity.RESQUEST_CODE);
    }

    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if(requestCode==SelectDateMatchActivity.RESQUEST_CODE){
            if(resultCode==RESULT_OK){
                setResult(RESULT_OK);
                finish();
            }
        }
        super.onActivityResult (requestCode, resultCode, data);
    }
}
