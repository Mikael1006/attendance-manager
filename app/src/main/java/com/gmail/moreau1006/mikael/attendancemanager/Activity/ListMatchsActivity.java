package com.gmail.moreau1006.mikael.attendancemanager.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.gmail.moreau1006.mikael.attendancemanager.Adapter.TeamAdapter;
import com.gmail.moreau1006.mikael.attendancemanager.Model.Match;
import com.gmail.moreau1006.mikael.attendancemanager.Adapter.MatchAdapter;
import com.gmail.moreau1006.mikael.attendancemanager.DAO.MatchsDAO;
import com.gmail.moreau1006.mikael.attendancemanager.Model.Player;
import com.gmail.moreau1006.mikael.attendancemanager.Model.Team;
import com.gmail.moreau1006.mikael.attendancemanager.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class ListMatchsActivity extends AppCompatActivity {

    public static final String EXTRA_MATCH = "com.gmail.moreau1006.mikael.attendancemanager.MATCH";
    public static final int CREATE_MATCH_RESQUEST_CODE = 1000;
    public static final int MATCH_ACITIVITY_RESQUEST_CODE = 1001;

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

    private void updateListView(){
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
        startActivityForResult(intent, CREATE_MATCH_RESQUEST_CODE);
    }

    public void showMatch(View view) {
        //https://developer.android.com/training/basics/firstapp/starting-activity.html

        // on récupère l'index de la liste
        int index = matchsListView.indexOfChild((View)view.getParent());
        // On récupère la bonne équipe de la liste
        Match match = matchs.get(index);

        Intent intent = new Intent(this, MatchActivity.class);
        intent.putExtra(ListMatchsActivity.EXTRA_MATCH,match);
        startActivityForResult(intent, MATCH_ACITIVITY_RESQUEST_CODE);
    }

    public void deleteMatch(View view) {

        // on récupère l'index de la liste
        int index = matchsListView.indexOfChild((View)view.getParent());

        // On récupère la bonne équipe de la liste
        final Match match = matchs.get(index);

        DateFormat dateFormat = new SimpleDateFormat("EEEE, d MMM yyyy à HH:mm", Locale.FRENCH);
        String dateMatch = dateFormat.format(match.getDateMatch());

        // confirm ?
        AlertDialog alertDialog = new AlertDialog.Builder(ListMatchsActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Supprimer le match contre " + match.getOpponent() + " du " + dateMatch + " ?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        try{
                            matchsDAO.deleteMatch(match);
                        }catch (Exception e){
                            e.printStackTrace();
                        }

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

    // When we have finished to create a match,
    // we update the list of matchs
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if(requestCode==ListMatchsActivity.CREATE_MATCH_RESQUEST_CODE){
            if(resultCode==RESULT_OK){
                updateListView();
            }
        }else if(requestCode == ListMatchsActivity.MATCH_ACITIVITY_RESQUEST_CODE){
            updateListView();
        }
        super.onActivityResult (requestCode, resultCode, data);
    }
}
