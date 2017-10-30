package com.gmail.moreau1006.mikael.attendancemanager.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.gmail.moreau1006.mikael.attendancemanager.Model.Match;
import com.gmail.moreau1006.mikael.attendancemanager.Adapter.MatchAdapter;
import com.gmail.moreau1006.mikael.attendancemanager.DAO.MatchsDAO;
import com.gmail.moreau1006.mikael.attendancemanager.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ListMatchsActivity extends AppCompatActivity {

    public static final String EXTRA_MATCH = "com.gmail.moreau1006.mikael.attendancemanager.MATCH";
    public static final int CREATE_MATCH_RESQUEST_CODE = 1000;
    public static final int MATCH_ACITIVITY_RESQUEST_CODE = 1001;

    private ListView matchsListView;
    private MatchsDAO matchsDAO;
    private List<Match> matchs;
    private Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_matchs);

        matchsListView = (ListView) findViewById(R.id.MatchsListView);
        spinner = (Spinner) findViewById(R.id.list_matchs_spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> SpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.type_list_matchs, R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        SpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(SpinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                updateListView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        matchsDAO = new MatchsDAO(this);
        matchsDAO.open();

        updateListView();
    }

    private void updateListView(){
        int pos = spinner.getSelectedItemPosition();
        try{
            if(pos == 0){
                matchs = matchsDAO.getAllFutursMatchs();
            }else if(pos == 1){
                matchs = matchsDAO.getAllMatchs();
            }
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
