package com.gmail.moreau1006.mikael.attendancemanager.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gmail.moreau1006.mikael.attendancemanager.Model.Player;
import com.gmail.moreau1006.mikael.attendancemanager.Adapter.PlayerAdapter;
import com.gmail.moreau1006.mikael.attendancemanager.DAO.PlayersDAO;
import com.gmail.moreau1006.mikael.attendancemanager.R;
import com.gmail.moreau1006.mikael.attendancemanager.Model.Team;

import java.util.List;

public class ListPlayersActivity extends AppCompatActivity {

    static final int PICK_CONTACT_REQUEST = 1;


    private ListView playersListView;
    private PlayersDAO playersDAO;
    private Team team;
    private List<Player> players;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_players);

        playersListView = (ListView) findViewById(R.id.PlayersListView);

        playersDAO = new PlayersDAO(this);
        playersDAO.open();

        // On récupère l'équipe de la liste
        team = (Team) getIntent().getSerializableExtra(ListTeamsActivity.EXTRA_TEAM);

        // On récupère tous les joueurs de l'équipe
        players = playersDAO.getPlayersByTeam(team);

        PlayerAdapter adapter = new PlayerAdapter(ListPlayersActivity.this, players);
        playersListView.setAdapter(adapter);
    }

    public void addPlayer(View view) {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
        startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
    }

    public void deletePlayer(View view) {

        int firstListItemPosition = playersListView.getFirstVisiblePosition();

        // on récupère l'index de la liste
        int index = playersListView.indexOfChild((View)view.getParent()) + firstListItemPosition;

        // On récupère la bonne équipe de la liste
        final Player player = players.get(index);

        // l'alert pour confirmer
        AlertDialog alertDialog = new AlertDialog.Builder(ListPlayersActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Supprimer le contact " + player.getName() + " ?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // On supprime le joueur
                        playersDAO.deletePlayer(player);

                        // On récupère tous les joueurs de l'équipe
                        players = playersDAO.getPlayersByTeam(team);
                        PlayerAdapter adapter = new PlayerAdapter(ListPlayersActivity.this, players);
                        playersListView.setAdapter(adapter);
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Annuler",
                new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PICK_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.

                // Get the URI that points to the selected contact
                Uri contactUri = data.getData();
                // We only need the NUMBER column, because there will be only one row in the result
                String[] projection = {Phone.NORMALIZED_NUMBER,Phone.DISPLAY_NAME};

                // Perform the query on the contact to get the NUMBER column
                // We don't need a selection or sort order (there's only one result for the given URI)
                // CAUTION: The query() method should be called from a separate thread to avoid blocking
                // your app's UI thread. (For simplicity of the sample, this code doesn't do that.)
                // Consider using CursorLoader to perform the query.
                Cursor cursor = getContentResolver()
                        .query(contactUri, projection, null, null, null);
                cursor.moveToFirst();

                // Retrieve the phone number from the NUMBER column
                int column = cursor.getColumnIndex(Phone.NORMALIZED_NUMBER);
                String number = cursor.getString(column);
                if(number==null){
                    number="";
                }

                column = cursor.getColumnIndex(Phone.DISPLAY_NAME);
                String name = cursor.getString(column);
                if(name==null){
                    name="";
                }

                // on vérifie que le contact n'as pas déjà été ajouté
                boolean alreadyAdded = false;
                for (int i = 0; i < players.size(); i++) {
                    if(players.get(i).getNumberPhone().equals(number)){
                        alreadyAdded = true;
                        break;
                    }
                }
                if(!alreadyAdded){
                    Player player = playersDAO.createPlayer(name, number, team.getId());
                }

                // On récupère tous les joueurs de l'équipe
                players = playersDAO.getPlayersByTeam(team);
                PlayerAdapter adapter = new PlayerAdapter(ListPlayersActivity.this, players);
                playersListView.setAdapter(adapter);
            }
        }
    }

    public void onClickOk(View view){
        setResult(Activity.RESULT_OK);
        finish();
    }
}
