package com.gmail.moreau1006.mikael.attendancemanager.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.gmail.moreau1006.mikael.attendancemanager.Adapter.InvitedPlayersAdapter;
import com.gmail.moreau1006.mikael.attendancemanager.DAO.MatchsDAO;
import com.gmail.moreau1006.mikael.attendancemanager.DAO.PlayersDAO;
import com.gmail.moreau1006.mikael.attendancemanager.Model.Match;
import com.gmail.moreau1006.mikael.attendancemanager.Model.Player;
import com.gmail.moreau1006.mikael.attendancemanager.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MatchActivity extends AppCompatActivity {

    private ListView invitedPlayersListView;
    private Match match;
    private MatchsDAO matchsDAO;
    private PlayersDAO playersDAO;

    private TextView match_textview_date;
    private TextView match_textview_dateRdv;
    private TextView match_textview_opponent;
    private TextView match_textview_home;
    private TextView match_textview_team;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        match_textview_date = (TextView) findViewById(R.id.show_match_textview_date);
        match_textview_dateRdv = (TextView) findViewById(R.id.show_match_textview_dateRdv);
        match_textview_opponent = (TextView) findViewById(R.id.show_match_textview_opponent);
        match_textview_home = (TextView) findViewById(R.id.show_match_textview_home);
        match_textview_team = (TextView) findViewById(R.id.show_match_textview_team);

        matchsDAO = new MatchsDAO(this);
        matchsDAO.open();
        playersDAO = new PlayersDAO(this);
        playersDAO.open();

        invitedPlayersListView = (ListView) findViewById(R.id.invitedPlayersListView);

        // get match from previous activity
        match = (Match) getIntent().getSerializableExtra(ListMatchsActivity.EXTRA_MATCH);

        DateFormat dateFormat = new SimpleDateFormat("EEEE, d MMM yyyy à HH:mm", Locale.FRENCH);

        // Using DateFormat format method we can create a string
        // representation of a date with the defined format.
        String dateMatch = dateFormat.format(match.getDateMatch());
        String dateRdv = dateFormat.format(match.getDateRdv());

        match_textview_date.setText(dateMatch);
        match_textview_dateRdv.setText(dateRdv);
        match_textview_opponent.setText(match.getOpponent());

        String home;
        if(match.isHome()){
            home = "Domicile";
        }else{
            home = "Exterieur";
        }

        match_textview_home.setText(home);
        if(match.getTeam() == null){
            match_textview_team.setText("Equipe suprimée");
        }else {
            match_textview_team.setText(match.getTeam().getName());
        }

        updateListView();
    }

    private void updateListView(){
        List<Player> players = playersDAO.getPlayersByMatch(match);
        match.setInvitedPlayers(players);
        InvitedPlayersAdapter adapter = new InvitedPlayersAdapter(MatchActivity.this, match.getInvitedPlayers());
        invitedPlayersListView.setAdapter(adapter);
    }

    public void addInvitedPlayers(View view){
        //TODO
        Intent intent = new Intent(this, AddInvitedPlayerActivity.class);
        intent.putExtra(ListMatchsActivity.EXTRA_MATCH,match);
        startActivityForResult(intent, ListMatchsActivity.CREATE_MATCH_RESQUEST_CODE);
    }

    /**
     * Create an alert dialog to change player's attendance.
     * @param view
     */
    public void editAttendance(View view){

        // on récupère l'index de la liste
        int index = invitedPlayersListView.indexOfChild((View) view.getParent());
        // On récupère le bon joueur
        final Player player = match.getInvitedPlayers().get(index);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MatchActivity.this);
        alertDialog.setTitle("Editer la présence");

        final RadioButton attendantRadioButton = new RadioButton(MatchActivity.this);
        final RadioButton noAttendantRadioButton = new RadioButton(MatchActivity.this);
        final RadioButton unknownRadioButton = new RadioButton(MatchActivity.this);

        if(player.isAttendant() == null){
            unknownRadioButton.setChecked(true);
        }else if(player.isAttendant()){
            attendantRadioButton.setChecked(true);
        }else if(!player.isAttendant()){
            noAttendantRadioButton.setChecked(true);
        }

        attendantRadioButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view1) {
                        unknownRadioButton.setChecked(false);
                        noAttendantRadioButton.setChecked(false);
                    }
                }
        );
        noAttendantRadioButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view1) {
                        unknownRadioButton.setChecked(false);
                        attendantRadioButton.setChecked(false);
                    }
                }
        );
        unknownRadioButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view1) {
                        attendantRadioButton.setChecked(false);
                        noAttendantRadioButton.setChecked(false);
                    }
                }
        );

        GradientDrawable attendantSquare = (GradientDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.square, null);
        GradientDrawable noAttendantSquare = (GradientDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.square, null);
        GradientDrawable unknownSquare = (GradientDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.square, null);

        attendantSquare.setColor(Color.GREEN);
        noAttendantSquare.setColor(Color.RED);
        unknownSquare.setColor(Color.GRAY);

        View attendantView = new View(MatchActivity.this);
        View noAttendantView = new View(MatchActivity.this);
        View unknownView = new View(MatchActivity.this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(30,30);

        attendantView.setLayoutParams(params);
        noAttendantView.setLayoutParams(params);
        unknownView.setLayoutParams(params);

        attendantView.setBackground(attendantSquare);
        noAttendantView.setBackground(noAttendantSquare);
        unknownView.setBackground(unknownSquare);

        Context context = view.getContext();
        LinearLayout verticalLayout = new LinearLayout(context);
        verticalLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout attendantHorizontalLayout = new LinearLayout(context);
        attendantHorizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
        attendantHorizontalLayout.addView(attendantView);
        attendantHorizontalLayout.addView(attendantRadioButton);
        TextView attendantTextView = new TextView(MatchActivity.this);
        attendantTextView.setText("Présent");
        attendantHorizontalLayout.addView(attendantTextView);

        LinearLayout noAttendantHorizontalLayout = new LinearLayout(context);
        noAttendantHorizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
        noAttendantHorizontalLayout.addView(noAttendantView);
        noAttendantHorizontalLayout.addView(noAttendantRadioButton);
        TextView noAttendantTextView = new TextView(MatchActivity.this);
        noAttendantTextView.setText("Absent");
        noAttendantHorizontalLayout.addView(noAttendantTextView);

        LinearLayout unknownHorizontalLayout = new LinearLayout(context);
        unknownHorizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
        unknownHorizontalLayout.addView(unknownView);
        unknownHorizontalLayout.addView(unknownRadioButton);
        TextView unknownTextView = new TextView(MatchActivity.this);
        unknownTextView.setText("Indéterminé");
        unknownHorizontalLayout.addView(unknownTextView);

        verticalLayout.addView(attendantHorizontalLayout);
        verticalLayout.addView(noAttendantHorizontalLayout);
        verticalLayout.addView(unknownHorizontalLayout);

        alertDialog.setView(verticalLayout);

        alertDialog.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(attendantRadioButton.isChecked()){
                            player.setAttendant(true);
                        }else if(noAttendantRadioButton.isChecked()){
                            player.setAttendant(false);
                        }else if(unknownRadioButton.isChecked()){
                            player.setAttendant(null);
                        }

                        try{
                            match = matchsDAO.updateInvitation(match,player);
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        updateListView();
                    }
                });

        alertDialog.setNegativeButton("Annuler",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if(requestCode == ListMatchsActivity.CREATE_MATCH_RESQUEST_CODE){
            if(resultCode==RESULT_OK){
                updateListView();
            }
        }
        super.onActivityResult (requestCode, resultCode, data);
    }

    private void updateAttendanceFromSMS(){
        // https://stackoverflow.com/questions/10870230/read-all-sms-from-a-particular-sender
        // http://www.itcuties.com/android/read-sms/
        // http://pulse7.net/android/read-sms-message-inbox-sent-draft-android/

        final String SMS_URI_ALL = "content://sms/";

        // all sms
        Uri message = Uri.parse(SMS_URI_ALL);
    }
}
