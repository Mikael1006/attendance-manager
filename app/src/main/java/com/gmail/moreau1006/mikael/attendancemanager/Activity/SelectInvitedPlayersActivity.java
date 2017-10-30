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
import java.util.ArrayList;
import java.util.List;

public class SelectInvitedPlayersActivity extends AppCompatActivity {

    private ListView playersListView;
    private PlayersDAO playersDAO;
    private List<Player> players;
    private Match match;
    private List<Player> invitedPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_players);

        playersListView = (ListView) findViewById(R.id.SelectPlayersListView);

        playersDAO = new PlayersDAO(this);
        playersDAO.open();

        // get match from previous activity
        match = (Match) getIntent().getSerializableExtra(ListMatchsActivity.EXTRA_MATCH);

        // On récupère tous les joueurs de l'équipe
        players = playersDAO.getPlayersByTeam(match.getTeam());

        SelectPlayerAdapter adapter = new SelectPlayerAdapter(SelectInvitedPlayersActivity.this, players);
        playersListView.setAdapter(adapter);

        invitedPlayers = new ArrayList<Player>();
        match.setInvitedPlayers(invitedPlayers);
    }

    public void checkPlayer(View view) {

        int firstListItemPosition = playersListView.getFirstVisiblePosition();

       // on récupère l'index de la liste
       int index = playersListView.indexOfChild((View) view.getParent()) + firstListItemPosition;

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

        Intent intent = new Intent(SelectInvitedPlayersActivity.this, WriteSmsActivity.class);
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
        super.onActivityResult (requestCode, resultCode, data);
    }
}
