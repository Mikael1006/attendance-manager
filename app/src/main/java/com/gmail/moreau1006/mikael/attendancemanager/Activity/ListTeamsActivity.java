package com.gmail.moreau1006.mikael.attendancemanager.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

import com.gmail.moreau1006.mikael.attendancemanager.Model.Player;
import com.gmail.moreau1006.mikael.attendancemanager.DAO.PlayersDAO;
import com.gmail.moreau1006.mikael.attendancemanager.R;
import com.gmail.moreau1006.mikael.attendancemanager.Model.Team;
import com.gmail.moreau1006.mikael.attendancemanager.Adapter.TeamAdapter;
import com.gmail.moreau1006.mikael.attendancemanager.DAO.TeamsDAO;

import java.util.List;

public class ListTeamsActivity extends AppCompatActivity {

    public static final String EXTRA_TEAM = "com.gmail.moreau1006.mikael.attendancemanager.TEAM";

    private ListView teamsListView;
    private List<Team> teams;
    private TeamsDAO teamsDAO;
    private PlayersDAO playersDAO;
    private EditText teamNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_teams);

        // https://stackoverflow.com/questions/4149415/onscreen-keyboard-opens-automatically-when-activity-starts
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        teamsListView = (ListView) findViewById(R.id.TeamsListView);

        teamNameEditText = (EditText) findViewById(R.id.team_name_editText);

        teamsDAO = new TeamsDAO(this);
        teamsDAO.open();

        playersDAO = new PlayersDAO(this);
        playersDAO.open();

        updateListView();

    }

    private void updateListView(){
        teams = teamsDAO.getAllTeams();
        TeamAdapter adapter = new TeamAdapter(ListTeamsActivity.this, teams);
        teamsListView.setAdapter(adapter);
    }

    public void editTeam(View view){
        //https://developer.android.com/training/basics/firstapp/starting-activity.html

        int firstListItemPosition = teamsListView.getFirstVisiblePosition();

        // on récupère l'index de la liste
        int index = teamsListView.indexOfChild((View)view.getParent()) + firstListItemPosition;
        // On récupère la bonne équipe de la liste
        Team team = teams.get(index);

        Intent intent = new Intent(this, ListPlayersActivity.class);
        intent.putExtra(EXTRA_TEAM,team);
        startActivity(intent);
    }

    public void deleteTeam(final View view){

        // on récupère l'index de la liste
        int index = teamsListView.indexOfChild((View)view.getParent());

        // On récupère la bonne équipe de la liste
        final Team team = teams.get(index);

        // l'alert pour confirmer
        AlertDialog alertDialog = new AlertDialog.Builder(ListTeamsActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Supprimer l'équipe " + team.getName() + " ?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        try{
                            teamsDAO.deleteTeam(team);
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        // update List view
                        updateListView();

                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Annuler",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void addTeam(View view){

        String text = teamNameEditText.getText().toString();

        // Si le champ n'est pas vide
        if (!text.isEmpty()){
            teamsDAO.createTeam(text);

            // On rempli la liste
            teams = teamsDAO.getAllTeams();
            TeamAdapter adapter = new TeamAdapter(ListTeamsActivity.this, teams);
            teamsListView.setAdapter(adapter);

            // Vide le textEdit
            teamNameEditText.setText("");

            // Ferme le clavier
            // https://stackoverflow.com/questions/2342620/how-to-hide-keyboard-after-typing-in-edittext-in-android
            Context context = teamNameEditText.getContext();
            InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void onClickOk(View view){
        setResult(Activity.RESULT_OK);
        finish();
    }
}
